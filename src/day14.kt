import java.io.File
import kotlin.math.max

fun main(args: Array<String>) {
//    val input = File("/Users/stammt/Documents/2021aoc/day14input.txt").readLines()

    val input = ("NNCB\n" +
            "\n" +
            "CH -> B\n" +
            "HH -> N\n" +
            "CB -> H\n" +
            "NH -> C\n" +
            "HB -> C\n" +
            "HC -> B\n" +
            "HN -> C\n" +
            "NN -> C\n" +
            "BH -> H\n" +
            "NC -> B\n" +
            "NB -> B\n" +
            "BN -> B\n" +
            "BB -> N\n" +
            "BC -> B\n" +
            "CC -> N\n" +
            "CN -> C").split("\n")

    val start = System.nanoTime()
    day14part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day14part1(input: List<String>) {
    var template = input[0]
    val rules = input.subList(2, input.size).associate { line ->
        line.split(" -> ").let { (it[0][0] to it[0][1]) to it[1][0] }
    }

    var pairs = template.mapIndexed { index, s ->
        s to (if (index < template.length - 1) template[index+1] else null) }
    var pairCounts = pairs.associateWith { p -> pairs.count{ it == p }.toLong() }

    val steps = 40
    for (i in 0 until steps) {
        pairCounts = pairCounts.flatMap { entry ->
            if (entry.key.second == null) {
                listOf(entry.key to entry.value)
            } else {
                val insert = rules[entry.key]!!
                val p1 = entry.key.first to insert
                val p2 = insert to entry.key.second
                listOf(p1 to entry.value, p2 to entry.value)
            }
        }.groupingBy {
            it.first
        }.fold(0L) { acc, element ->
            acc + element.second
        }
    }

    // build a histogram of character counts based on the first character of each pair
    val histogram = pairCounts.keys.groupingBy { it.first }.fold(0L) { acc, element ->
        acc + pairCounts[element]!! }
    val max = histogram.maxByOrNull { c -> c.value!!  }
    val min = histogram.minByOrNull { c -> c.value!! }
    println("diff: ${max!!.value!! - min!!.value!!}")
}
