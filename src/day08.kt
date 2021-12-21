import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day08input.txt").readLines()

//    val input = listOf( "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
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
    day08part2(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}


fun day08part1(input: List<String>) {
    val digits = input.map {
        it.split(" | ")[1].split(' ')
    }

    val unique = setOf(2, 4, 3, 7)
    val allDigits = digits.flatten()
    val count = allDigits.count { unique.contains(it.length) }
    println("$count")
}

fun day08part2(input: List<String>) {
    val correct = mapOf(
        0 to setOf('a', 'b', 'c', 'e', 'f', 'g'),
        1 to setOf('c', 'f'),
        2 to setOf('a', 'c', 'd', 'e', 'g'),
        3 to setOf('a', 'c', 'd', 'f', 'g'),
        4 to setOf('b', 'c', 'd', 'f'),
        5 to setOf('a', 'b', 'd', 'f', 'g'),
        6 to setOf('a', 'b', 'd', 'e', 'f', 'g'),
        7 to setOf('a', 'c', 'f'),
        8 to setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        9 to setOf('a', 'b', 'c', 'd', 'f', 'g'),
    )

    // pair of input -> digits
    val lines = input.map {
        Pair(it.split(" | ")[0].split(' '),
            it.split(" | ")[1].split(' '))
    }
    var sum = 0
    lines.forEach {
        val inputs = it.first
        val digits = it.second

        val potential = mutableMapOf<Char, MutableSet<Char>>()

        val one = inputs.firstOrNull { i -> i.length == 2 }
        if (one != null) {
            potential[one[0]] = correct[1]!!.toMutableSet()
            potential[one[1]] = correct[1]!!.toMutableSet()
        }

        val seven = inputs.firstOrNull { i -> i.length == 3 }
        if (seven != null) {
            // two of these should already be in the potential map
            val a = seven.firstOrNull { c -> !potential.contains(c) }
            if (a != null)
                potential[a] = mutableSetOf('a')
        }

        val four = inputs.firstOrNull { i -> i.length == 4 }
        if (four != null) {
            // two of these should already exist, the other two are b or d
            four.filter { c -> !potential.contains(c) }.forEach { c ->
                potential[c] = mutableSetOf('b', 'd')
            }
        }

        val eight = inputs.firstOrNull { i -> i.length == 7 }
        if (eight != null) {
            // five of these should already exist, the other two are e or g
            eight.filter { c -> !potential.contains(c) }.forEach { c ->
                potential[c] = mutableSetOf('e', 'g')
            }
        }

        // 6 has six segments, and only has one of c,f
        val potentialCorF = potential.filterValues { v -> v == mutableSetOf('c', 'f') }.keys
        val six = inputs.firstOrNull { i -> i.length == 6 && i.count{ c -> potentialCorF.contains(c)} == 1}
        if (six != null) {
            val f = six.firstOrNull { c-> one!!.contains(c)}
            if (f != null) {
                val p = potential[f]
                potential[f] = mutableSetOf('f')
                val other = one!!.firstOrNull { x -> x != f}
                if (other != null)
                    potential[other] = mutableSetOf('c')
            }
        }

        // 0 has six segments, and only has one of b,d
        val potentialBorD = potential.filterValues { v -> v == mutableSetOf('b', 'd') }.keys
        val zero = inputs.firstOrNull { i -> i.length == 6 && i.count{ c -> potentialBorD.contains(c)} == 1}
        if (zero != null) {
            val f = zero.firstOrNull { c-> potentialBorD.contains(c)}
            if (f != null) {
                val p = potential[f]
                potential[f] = mutableSetOf('b')
                val other = potentialBorD.firstOrNull { x -> x != f}
                if (other != null)
                    potential[other] = mutableSetOf('d')
            }
        }

        // 3 has six segments, and only has one of e,g
        val potentialEorG = potential.filterValues { v -> v == mutableSetOf('e', 'g') }.keys
        val three = inputs.firstOrNull { i -> i.length == 5 && i.count{ c -> potentialEorG.contains(c)} == 1}
        if (three != null) {
            val f = three.firstOrNull { c-> potentialEorG.contains(c)}
            if (f != null) {
                val p = potential[f]
                potential[f] = mutableSetOf('g')
                val other = potentialEorG.firstOrNull { x -> x != f}
                if (other != null)
                    potential[other] = mutableSetOf('e')
            }
        }

//        println(potential)
        val unknowns = potential.filterValues { v -> v.size > 1 }.keys
        if (unknowns.isNotEmpty()) {
            println("Unknowns: $unknowns")
        }

        // now go through the digits
        val numbers = mutableListOf<Int>()
        for (digit in digits) {
            val corrected = mutableSetOf<Char>()
            digit.forEach { c ->
                potential[c]!!.firstOrNull()?.let { it1 -> corrected.add(it1) }
            }
            val n = correct.filterValues { v -> v == corrected }.keys.firstOrNull()
            if (n != null) {
                numbers.add(n)
            }
        }

        sum += (numbers[0] * 1000) + (numbers[1] * 100) + (numbers[2] * 10) + numbers[3]
    }

    println("sum $sum")
}
