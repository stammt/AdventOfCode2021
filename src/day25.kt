import java.io.File
import java.util.*
import kotlin.math.*

fun main(args: Array<String>) {
    val fileinput = File("/Users/stammt/Documents/2021aoc/day25input.txt").readLines()

    val sampleinput = ("v...>>.vv>\n" +
            ".vv>>.vv..\n" +
            ">>.>v>...v\n" +
            ">>v>>.>.v.\n" +
            "v>v.vv.v..\n" +
            ">.>>..v...\n" +
            ".vv..>.>v.\n" +
            "v.v..>>v.v\n" +
            "....v..v.>").split("\n")
    val start = System.nanoTime()
    day25part1(fileinput)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day25part1(input: List<String>) {
    val headEast = mutableSetOf<Pair<Int, Int>>()
    val headSouth = mutableSetOf<Pair<Int, Int>>()

    for (y in input.indices) {
        val line = input[y]
        for (x in line.indices) {
            if (line[x] == '>') headEast.add(x to y)
            else if (line[x] == 'v') headSouth.add(x to y)
        }
    }

    var mapHeight = input.size
    var mapWidth = input[0].length

//    println("Initial $mapWidth x $mapHeight")
//    printGrid(mapWidth, mapHeight, headEast, headSouth)


    var moved = false
    var steps = 1
    do {
        moved = false

        val nextHeadEast = mutableSetOf<Pair<Int, Int>>()
        for (c in headEast) {
            val nextPos = if (c.first == (mapWidth - 1)) (0 to c.second) else (c.first + 1 to c.second)
            if (headSouth.contains(nextPos) || headEast.contains(nextPos)) {
                nextHeadEast.add(c)
            } else {
                nextHeadEast.add(nextPos)
                moved = true
            }
        }

        val nextHeadSouth = mutableSetOf<Pair<Int, Int>>()
        for (c in headSouth) {
            val nextPos = if (c.second == (mapHeight - 1)) (c.first to 0) else (c.first to c.second + 1)
            if (headSouth.contains(nextPos) || nextHeadEast.contains(nextPos)) {
                nextHeadSouth.add(c)
            } else {
                nextHeadSouth.add(nextPos)
                moved = true
            }
        }

        headSouth.clear()
        headSouth.addAll(nextHeadSouth)
        headEast.clear()
        headEast.addAll(nextHeadEast)

        if (moved) {
            steps++
        }
    } while (moved)

    printGrid(mapWidth, mapHeight, headEast, headSouth)

    println("$steps steps")

}

fun printGrid(mapWidth: Int, mapHeight: Int, headEast: Set<Pair<Int, Int>>, headSouth: Set<Pair<Int, Int>>) {
    var s = ""
    for (y in 0 until mapHeight) {
        for (x in 0 until mapWidth) {
            s += if (headSouth.contains(x to y)) 'v' else if (headEast.contains(x to y)) '>' else '.'
        }
        s += "\n"
    }
    println(s)
}

