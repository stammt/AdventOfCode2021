import java.io.File
import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
//    val input = File("/Users/stammt/Documents/2021aoc/day15input.txt").readLines()

    val input = ("1163751742\n" +
            "1381373672\n" +
            "2136511328\n" +
            "3694931569\n" +
            "7463417111\n" +
            "1319128137\n" +
            "1359912421\n" +
            "3125421639\n" +
            "1293138521\n" +
            "2311944581").split("\n")
    val start = System.nanoTime()
    day15part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day15part1(input: List<String>) {
    val grid = repeatGrid(Grid(input, String::toInt))

    val compareByDistance: Comparator<Pair<Pair<Int, Int>, Long>> = compareBy { it.second }
    val Q = PriorityQueue(compareByDistance)

    // initialize distances with the source node
    val distances = mutableMapOf<Pair<Int, Int>, Long>()
    distances[0 to 0] = 0L
    Q.add(Pair(0 to 0, 0L))

    // initialize previous node map
    val prev = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()

    val target = (grid.grid.size-1 to grid.grid[0].size-1)

    while (Q.isNotEmpty()) {
        val u = Q.remove()

        val neighbors = grid.getAdjacentPositions(u.first.first, u.first.second, includeDiagonals = false)
        neighbors.forEach{neighbor ->
            val alt = u.second + grid.getValue(neighbor.first, neighbor.second)
            if (alt < (distances[neighbor] ?: Long.MAX_VALUE)) {
                distances[neighbor] = alt
                prev[neighbor] = u.first
                Q.removeIf { it.first == neighbor }
                Q.add(neighbor to alt)
            }
        }
    }

    // add the path back to the source, not including the source
    val path = mutableListOf<Pair<Int, Int>>()
    var sum = 0L
    var u = target
    while (u != Pair(0, 0)) {
        path.add(0, u)
        sum += grid.getValue(u.first, u.second)
        u = prev[u]!!
    }
    println("Sum $sum for $path")
}

fun repeatGrid(grid: Grid<Int>) : Grid<Int> {
    val tiled = mutableListOf<MutableList<Int>>()
    val tileWidth = grid.grid.size
    val tileHeight = grid.grid[0].size

    for (tilex in 0..4) {
        for (tiley in 0..4) {
            for (x in 0 until tileWidth) {
                val tiledx = (tilex * tileWidth) + x
                if (tiledx >= tiled.size) tiled.add(mutableListOf())

                for (y in 0 until tileHeight) {
                    val tiledy = (tiley * tileHeight) + y
                    val v = if (tilex == 0 && tiley == 0) grid.getValue(x, y) else {
                        grid.getValue(x, y) + tilex + tiley
                    }
                    tiled[tiledx].add(if (v <= 9) v else v - 9)
                }
            }
        }
    }

    return Grid(tiled)
}