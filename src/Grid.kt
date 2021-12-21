
// Creates a grid based on chars in each line, no delimiters
class Grid<T>(val grid: MutableList<MutableList<T>>) {

    constructor(input: List<String>, transform: (String) -> T) : this(mutableListOf<MutableList<T>>()) {
        for (x in input[0].indices) {
            grid.add(input.map { it[x].toString() }.map(transform).toMutableList())
        }
    }

    fun size() : Int {
        return grid.size * grid[0].size
    }

    fun update(transform: (T) -> T) {
        for (x in grid.indices) {
            for (y in grid.indices) {
                grid[x][y] = transform(grid[x][y])
            }
        }
    }

    fun updateAtPositions(positions: List<Pair<Int, Int>>, transform: (T) -> T) {
        positions.forEach {
            grid[it.first][it.second] = transform(grid[it.first][it.second])
        }
    }

    fun filterPositions(predicate: (T) -> Boolean) : List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()
        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (predicate(grid[x][y])) {
                    positions.add(Pair(x, y))
                }
            }
        }
        return positions
    }

    fun getValue(x: Int, y: Int) : T {
        return grid[x][y]
    }

    fun setValue(x: Int, y: Int, v: T) {
        grid[x][y] = v
    }

    fun getAdjacentPositions(x: Int, y: Int, includeDiagonals: Boolean = true, predicate: (T) -> Boolean = { true }) : List<Pair<Int, Int>> {
        val positions = mutableListOf<Pair<Int, Int>>()
        step("W", x, y)?.takeIf{predicate(grid[it.first][it.second])}?.let { positions.add(it) }
        step("N", x, y)?.takeIf{predicate(grid[it.first][it.second])}?.let { positions.add(it) }
        step("S", x, y)?.takeIf{predicate(grid[it.first][it.second])}?.let { positions.add(it) }
        step("E", x, y)?.takeIf{predicate(grid[it.first][it.second])}?.let { positions.add(it) }
        if (includeDiagonals) {
            step("NE", x, y)?.takeIf { predicate(grid[it.first][it.second]) }?.let { positions.add(it) }
            step("SW", x, y)?.takeIf { predicate(grid[it.first][it.second]) }?.let { positions.add(it) }
            step("NW", x, y)?.takeIf { predicate(grid[it.first][it.second]) }?.let { positions.add(it) }
            step("SE", x, y)?.takeIf { predicate(grid[it.first][it.second]) }?.let { positions.add(it) }
        }
        return positions
    }

    fun step(dir: String, x: Int, y: Int) : Pair<Int, Int>? {
        val pos = when (dir) {
            "NW" -> Pair(x-1, y-1)
            "W" -> Pair(x-1, y)
            "SW" -> Pair(x-1, y+1)
            "N" -> Pair(x, y-1)
            "S" -> Pair(x, y+1)
            "NE" -> Pair(x+1, y-1)
            "E" -> Pair(x+1, y)
            "SE" -> Pair(x+1, y+1)
            else -> null
        }
        if (pos == null) {
            println("invalid direction $dir")
            return null
        }
        return if (isValidPos(pos.first, pos.second, grid)) pos else null
    }

    private fun isValidPos(x: Int, y: Int, grid: List<List<T>>) : Boolean {
        return x >= 0 && y >= 0 && x < grid.size && y < grid[0].size
    }

    override fun toString() : String {
        var s = ""
        for (y in 0 until grid[0].size) {
            for (x in grid.indices) {
                s += grid[x][y]
            }
            s += '\n'
        }
        return s
    }
}