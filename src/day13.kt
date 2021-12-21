import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day13input.txt").readLines()

//
//    val input = ("6,10\n" +
//            "0,14\n" +
//            "9,10\n" +
//            "0,3\n" +
//            "10,4\n" +
//            "4,11\n" +
//            "6,0\n" +
//            "6,12\n" +
//            "4,1\n" +
//            "0,13\n" +
//            "10,12\n" +
//            "3,4\n" +
//            "3,0\n" +
//            "8,4\n" +
//            "1,10\n" +
//            "2,14\n" +
//            "8,10\n" +
//            "9,0\n" +
//            "\n" +
//            "fold along y=7\n" +
//            "fold along x=5").split("\n")

    val start = System.nanoTime()
    day13part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day13part1(input: List<String>) {
    val gridEnd = input.indexOfFirst { it.trim().isEmpty() }
    var grid = input.subList(0, gridEnd).map { it.split(",").map(String::toInt).let{c -> c[0] to c[1] }}.toMutableSet()

//    println(gridToString(grid))

    input.subList(gridEnd+1, input.size).forEach { instruction ->
        val fold = instruction.substring("fold along ".length)
        val (axis, value) = fold.split("=").let { it[0] to it[1].toInt() }

        if (axis == "x") {
            grid.filter{ it.first > value }.forEach { point ->
                grid.add(value - (point.first - value) to point.second)
                grid.remove(point)
            }
        } else if (axis == "y") {
            grid.filter{ it.second > value }.forEach { point ->
                grid.add(point.first to value - (point.second - value))
                grid.remove(point)
            }
        }

        println("After $fold ${grid.size}")
    }
    println(gridToString(grid))
}

fun gridToString(grid: Set<Pair<Int, Int>>) : String {
    val width = grid.maxOfOrNull { it.first } ?: 0
    val height = grid.maxOfOrNull { it.second } ?: 0
    var s = ""
    for (y in 0 .. height) {
        for (x in 0 .. width) {
            s += (if (grid.contains(x to y)) '#' else ' ')
        }
        s += "\n"
    }
    return s
}