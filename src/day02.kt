import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day02input.txt").readLines()

    day02part2(input)
}

fun day02part2(lines: List<String>) {
    var pos = 0
    var depth = 0
    var aim = 0

    lines.forEach { command ->
        val dir = command.split(' ')[0]
        val amount = command.split(' ')[1].toInt()

        when (dir) {
            "forward" -> {
                pos += amount
                depth += (aim * amount)
            }
            "up" -> aim -= amount
            "down" -> aim += amount
            else -> println("unknown dir $dir")
        }
    }

    println("horizontal $pos depth $depth answer ${pos * depth}")
}

fun day02part1(lines: List<String>) {
    var pos = 0
    var depth = 0

    lines.forEach { command ->
        val dir = command.split(' ')[0]
        val amount = command.split(' ')[1].toInt()

        when (dir) {
            "forward" -> pos += amount
            "up" -> depth -= amount
            "down" -> depth += amount
            else -> println("unknown dir $dir")
        }
    }

    println("horizontal $pos depth $depth answer ${pos * depth}")
}