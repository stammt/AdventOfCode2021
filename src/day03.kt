import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day03input.txt").readLines()

    day03part2(input)
}

fun day03part2(lines: List<String>) {
    var pos = 0
    var o2:String? = null

    var remainingLines = lines.toMutableList()
    do {
        var oneCount = remainingLines.count { e -> e[pos] == '1' }
        var zeroCount = remainingLines.count { e -> e[pos] == '0' }
        var keep = if (oneCount >= zeroCount) '1' else '0'
        remainingLines.removeIf { e -> e[pos] != keep}
        if (remainingLines.size == 1) o2 = remainingLines[0]
        ++pos
    } while (pos < 12 && o2 == null)

    pos = 0
    var co2:String? = null
    remainingLines = lines.toMutableList()
    do {
        var oneCount = remainingLines.count { e -> e[pos] == '1' }
        var zeroCount = remainingLines.count { e -> e[pos] == '0' }
        var keep = if (oneCount < zeroCount) '1' else '0'
        remainingLines.removeIf { e -> e[pos] != keep}
        if (remainingLines.size == 1) co2 = remainingLines[0]
        ++pos
    } while (pos < 12 && co2 == null)

    if (o2 == null || co2 == null) {
        println("null!")
    } else {
        val o2Val = o2.toInt(2)
        val co2Val = co2.toInt(2)
        println("oxygen $o2 ($o2Val) CO2 $co2 ($co2Val) value ${o2Val * co2Val}")
    }
}

fun day03part1(lines: List<String>) {
    val ones = MutableList(12) { 0 }
    lines.forEach { b ->
        b.forEachIndexed { i, c ->
            if (c == '1') {
                ones[i] = ones[i] + 1
            }
        }
    }

    var gamma = mutableListOf<Char>()
    ones.forEach{ it ->
        if (it > lines.size / 2) {
            gamma.add('1')
        } else {
            gamma.add('0')
        }
    }
    val g = String(gamma.toCharArray()).toInt(2)

    var epsilon = gamma.map { if (it == '1') '0' else '1' }
    val e = String(epsilon.toCharArray()).toInt(2)


    println("g $g e $e p ${g * e}")
}