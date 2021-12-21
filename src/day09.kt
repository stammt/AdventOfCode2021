import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day09input.txt").readLines()

//    val input = ("2199943210\n" +
//            "3987894921\n" +
//            "9856789892\n" +
//            "8767896789\n" +
//            "9899965678").split("\n")
    val start = System.nanoTime()
    day09part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day09part1(input: List<String>) {
//    val grid = input.map { it.map{ c -> c.toString().toInt() }}
    val grid = mutableListOf<List<Int>>()
    for (x in input[0].indices) {
        grid.add(input.map{ it[x].toString().toInt() })
    }

    val basinSizes = mutableListOf<Int>()
    var sum = 0
    for (x in grid.indices) {
        for (y in grid[x]!!.indices) {
            if (isLowPoint(x, y, grid)) {
//                println("low point $x , $y (${grid[x][y]})")
                sum += (1 + grid[x][y])
                val basinSize = basinSize(Pair(x, y), grid)
                basinSizes.add(basinSize)
//                println("low point $x , $y (${grid[x][y]}) basin size $basinSize\n\n")
            }
        }
    }
    println(basinSizes)

    val sortedBasinSizes = basinSizes.sorted()
    val topThree = sortedBasinSizes.subList(sortedBasinSizes.size - 3, sortedBasinSizes.size)
    println(topThree)
    val answer = topThree[0] * topThree[1] * topThree[2]

    println(answer)
}

fun basinSize(pos: Pair<Int, Int>, grid: List<List<Int>>, checked: MutableList<Pair<Int, Int>> = mutableListOf()) : Int {
    // Go in each direction until we hit a number that is lower than the previous,
    // or a '9'
    checked.add(pos)

    val w = countBasinInDirection("W", pos, grid,  checked)
    if (w != null) {
        val wv = basinSize(w, grid, checked)
    }
    val n = countBasinInDirection("N", pos, grid, checked)
    if (n != null) {
        basinSize(n, grid, checked)
    }
    val s = countBasinInDirection("S", pos, grid, checked)
    if (s != null) {
        basinSize(s, grid, checked)
    }
    val e = countBasinInDirection("E", pos, grid, checked)
    if (e != null) {
        basinSize(e, grid, checked)
    }

//    println("basin size from $pos is $c or ${checked.size}")
    return checked.size
}

fun countBasinInDirection(dir: String, pos: Pair<Int, Int>, grid: List<List<Int>>, checked: List<Pair<Int, Int>>) : Pair<Int, Int>? {
//    println("Checking $dir from $pos")
    val nextPos = step(dir, pos.first, pos.second, grid)
        ?:
        return null
    if (checked.contains(nextPos)) {
        return null
    }

    val v = grid[pos.first][pos.second]
    val nextV = grid[nextPos.first][nextPos.second]
    if (nextV == 9 || nextV < v) {
//        println("$nextPos stopping $v -> $nextV at count $count")
        return null
    }
    return nextPos // countBasinInDirection(dir, nextPos, grid, count + 1)
}

fun step(dir: String, x: Int, y: Int, grid: List<List<Int>>) : Pair<Int, Int>? {
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

fun isValidPos(x: Int, y: Int, grid: List<List<Int>>) : Boolean {
    return x >= 0 && y >= 0 && x < grid.size && y < grid[0].size
}

fun isLowPoint(x: Int, y: Int, grid: List<List<Int>>) : Boolean {
    val v = grid[x][y]

    return isLessThan(v, x-1, y-1, grid)
            && isLessThan(v, x-1, y, grid)
            && isLessThan(v, x-1, y+1, grid)
            && isLessThan(v, x, y-1, grid)
            && isLessThan(v, x, y+1, grid)
            && isLessThan(v, x+1, y-1, grid)
            && isLessThan(v, x+1, y, grid)
            && isLessThan(v, x+1, y+1, grid)
}

fun isLessThan(v: Int, x: Int, y: Int, grid: List<List<Int>>) : Boolean {
    // neighbor is not valid
    if (x < 0 || y < 0) return true
    if (x > grid.size - 1) return true
    if (y > grid[x].size - 1) return true

    return v < grid[x][y]
}

