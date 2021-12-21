import java.io.File
import java.util.*
import kotlin.math.max

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day16input.txt").readLines().first()

//    val input = "D2FE28" // literal
//    val input = "38006F45291200" // op
//    val input = "880086C3E88112" //

    val start = System.nanoTime()
    day16part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day16part1(input: String) {

    // map to a binary string
    val bin = input.map { h ->
        String.format("%4s", Integer.toBinaryString(h.toString().toInt(16))).replace(" ".toRegex(), "0")
    }.joinToString(separator = "")
    println(bin)

    val packet = parsePacket(bin)
    println(packet)
    println("eval: ${packet.eval()}")
}


fun parsePacket(input: String) : Packet {
    val version = Integer.parseInt(input.take(3), 2)
    val type = Integer.parseInt(input.drop(3).take(3), 2)
    if (type ==4)
        return parseLiteralPacket(version, input.drop(6))
    else
        return parseOperatorPacket(version, type, input.drop(6))
}

fun parseOperatorPacket(version: Int, type: Int, input: String) : OperatorPacket {
    val subpacketLengthType = input.take(1)
    var inputLength = 6
    val children = mutableListOf<Packet>()

    if (subpacketLengthType == "0") {
        val subpacketLength = Integer.parseInt(input.drop(1).take(15), 2)
        inputLength += 16 + subpacketLength

        while (children.sumOf { it.inputLength } < subpacketLength) {
            val child = parsePacket(input.drop(1 + 15 + children.sumOf { it.inputLength }))
            children.add(child)
        }
    } else {
        val subpacketCount = Integer.parseInt(input.drop(1).take(11), 2)
        inputLength += 12
        for (i in 0 until subpacketCount) {
            val child = parsePacket(input.drop(1 + 11 + children.sumOf { it.inputLength }))
            children.add(child)
        }
        inputLength += children.sumOf { it.inputLength }
    }

    return OperatorPacket(version, inputLength, type, children)
}

fun parseLiteralPacket(version: Int, input: String) : LiteralPacket {
    var foundLast = false
    var valueString = ""
    var inputLength = 0
    var groups = 0
    while (!foundLast) {
        val s = input.drop(groups * 5).take(5)
        foundLast = s.take(1) == "0"
        valueString += s.drop(1)
        groups++
    }

    // add the length of the type/version header
    inputLength = (groups * 5) + 6

    val value = valueString.toLong(2)
    return LiteralPacket(version, inputLength, value)
}

abstract class Packet(val version: Int, val inputLength: Int) {
    open fun getVersionSum() : Int {
        return version
    }

    abstract fun eval() : Long
}
class LiteralPacket(version: Int, inputLength: Int, val value: Long) : Packet(version, inputLength) {
    override fun eval(): Long {
        return value
    }

    override fun toString() : String {
        return "Literal $value"
    }
}

class OperatorPacket(version: Int, inputLength: Int, val type: Int, val children: List<Packet>)
    : Packet(version, inputLength) {
    override fun getVersionSum() : Int {
        return version + children.sumOf { it.getVersionSum() }
    }

    override fun toString() : String {
        return "Op $type on $children"
    }

    override fun eval(): Long {
        return when (type) {
            0 -> children.sumOf { it.eval() }
            1 -> children.fold(1L) { acc, elt -> acc * elt.eval() }
            2 -> children.minOf { it.eval() }
            3 -> children.maxOf { it.eval() }
            5 -> if (children[0]!!.eval() > children[1]!!.eval()) 1 else 0
            6 -> if (children[0]!!.eval() < children[1]!!.eval()) 1 else 0
            7 -> if (children[0]!!.eval() == children[1]!!.eval()) 1 else 0
            else -> 0L
        }
    }
}
