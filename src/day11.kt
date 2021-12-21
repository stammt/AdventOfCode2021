import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day11input.txt").readLines()

//    val input = ("5483143223\n" +
//            "2745854711\n" +
//            "5264556173\n" +
//            "6141336146\n" +
//            "6357385478\n" +
//            "4167524645\n" +
//            "2176841721\n" +
//            "6882881134\n" +
//            "4846848554\n" +
//            "5283751526").split("\n")
//    val input = ("11111\n" +
//            "19991\n" +
//            "19191\n" +
//            "19991\n" +
//            "11111").split("\n")

    val start = System.nanoTime()
    day11part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day11part1(input: List<String>) {
    val grid = Grid<Int>(input, String::toInt)

    var flashes = 0
    for (i in 1..1000) {
        grid.update { it + 1 }

        val flashers = grid.filterPositions { it > 9 }.toMutableList()
        while (flashers.isNotEmpty()) {
            flashes += flashers.size
            flashers.forEach { flasher ->
                grid.updateAtPositions(listOf(flasher)) { 0 }
                grid.updateAtPositions(grid.getAdjacentPositions(flasher.first, flasher.second) { it > 0 }) { it + 1 }
            }

            flashers.clear()
            flashers.addAll(grid.filterPositions { it > 9 })

            val flashed = grid.filterPositions { it == 0 }
            if (flashed.size == grid.size()) {
                println("all flashing at $i")
                break
            }
        }
    }

    println(flashes)
}

