import java.io.File
import java.lang.Math.abs
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day07input.txt").readLines()[0]

//    val input = "16,1,2,0,4,2,7,1,2,14"
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
    day06part2(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}


fun day06part1(input: String) {
    val initial = input.split(',').map(String::toInt)
    val max = initial.maxOrNull() ?: 0
    val dist = MutableList(max + 1) { 0 }
    initial.forEach {
        dist[it] += 1
    }


    var best = -1
    var bestFuel = -1
    dist.indices.forEach { target ->
        var fuel = 0
        dist.indices.forEach {
            val distance = abs(it - target)
            fuel += (distance * dist[it])
        }
        if (bestFuel == -1 || fuel < bestFuel) {
            best = target
            bestFuel = fuel
        }
    }

    println("$bestFuel from $best")
}


fun day06part2(input: String) {
    val initial = input.split(',').map(String::toInt)
    val max = initial.maxOrNull() ?: 0
    val dist = MutableList(max + 1) { 0 }
    initial.forEach {
        dist[it] += 1
    }


    var best = -1
    var bestFuel = -1
    dist.indices.forEach { target ->
        var fuel = 0
        dist.indices.forEach {
            val distance = abs(it - target)
            fuel += (part2Fuel((distance)) * dist[it])
        }
        if (bestFuel == -1 || fuel < bestFuel) {
            best = target
            bestFuel = fuel
        }
    }

    println("$bestFuel from $best")
}


fun part2Fuel(dist: Int) : Int {
    return (dist * (dist + 1)) / 2
}
