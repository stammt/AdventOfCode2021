import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day01input.txt").readLines().map{it.toInt()}

    val windows = MutableList(input.size) { 0 }
    println("input size is ${input.size}")
    for ((index, value) in input.withIndex()) {
        if (index > input.size - 3) {
            println("breaking at index $index, windows size is ${windows.size}")
            break
        }

        windows[index] += value
        windows[index + 1] += value
        windows[index + 2] += value
    }

    var increased = 0
    var last = -1

    windows.forEach { i ->
        if (last != -1) {
            if (i > last) {
                increased++
            }
        }
        last = i
    }

    println("Windows increased $increased out of ${input.size}")

}

fun part1(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day01input.txt").readLines().map{it.toInt()}
    var increased = 0
    var last = -1

    input.forEach { i ->
        if (last != -1) {
            if (i > last) {
                increased++
            }
        }
        last = i
    }

    println("Increased $increased out of ${input.size}")

}