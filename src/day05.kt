import java.io.File
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day05input.txt").readLines()

//    val input = ("0,9 -> 5,9\n" +
//            "8,0 -> 0,8\n" +
//            "9,4 -> 3,4\n" +
//            "2,2 -> 2,1\n" +
//            "7,0 -> 7,4\n" +
//            "6,4 -> 2,0\n" +
//            "0,9 -> 2,9\n" +
//            "3,4 -> 1,4\n" +
//            "0,0 -> 8,8\n" +
//            "5,5 -> 8,2").split("\n")
    val start = System.nanoTime()
    day05part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}


fun day05part1(input: List<String>) {
    val lines = input.map { Line(it) }
    val pointCounts = mutableMapOf<Pair<Int, Int>, Int>()

    // filter by isHorizontal and isVertical for part 1
    lines.forEach {
        val points = it.getAllPoints()
        points.forEach { p ->
            val count = pointCounts[p] ?: 0
            pointCounts[p] = count + 1
        }
    }

    val answer = pointCounts.filterValues { it >= 2 }.size
    println("answer $answer")
}

class Line(numbers: String) {
    private val parts = numbers.split(" -> ").map(String::trim)
    val p1 = Pair(parts[0].split(',')[0].toInt(), parts[0].split(',')[1].toInt())
    val p2 = Pair(parts[1].split(',')[0].toInt(), parts[1].split(',')[1].toInt())

    fun isHorizontal() : Boolean {
        return p1.second == p2.second
    }
    fun isVertical() : Boolean {
        return p1.first == p2.first
    }

    fun getAllPoints() : List<Pair<Int, Int>> {
        val points = mutableListOf(p1)
        val dx = if (p1.first < p2.first) 1 else if (p1.first > p2.first) -1 else 0
        val dy = if (p1.second < p2.second) 1 else if (p1.second > p2.second) -1 else 0

        var next = Pair(p1.first, p1.second)
        do {
            next = Pair(next.first + dx, next.second + dy)
            points.add(next)
        } while (points.last() != p2)
        return points
    }
}