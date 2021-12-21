import java.io.File
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day06input.txt").readLines()[0]

//    val input = "3,4,3,1,2"
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


fun day05part1(input: String) {
    val intAges = input.split(',').map(String::toInt).groupingBy { it }.eachCount().toMutableMap()
    val ages = mutableMapOf<Int, Long>()
    intAges.forEach {(k, v) ->
        ages[k] = v.toLong()
    }
    val days = 256

    println(ages)
    for (i in 1..days) {
        val newFish = ages[0]
        ages[0] = 0

        for (t in 1..8) {
            val count = ages[t] ?: 0
            ages[t] = 0
            ages[t-1] = (ages[t-1] ?: 0) + count
        }

        ages[6] = (ages[6] ?: 0) +  (newFish ?: 0)
        ages[8] = newFish ?: 0

//        println("day $i ${countFish(ages)} : $ages")
    }
    println("${countFish(ages)}")

}

fun countFish(ages: Map<Int, Long>) : Long {
    var count = 0L
    ages.forEach { (k, v) ->
        count += v
    }
    return count
}

