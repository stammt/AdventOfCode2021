import java.io.File
import java.util.*
import kotlin.math.ceil
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.max

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day18input.txt").readLines()

//    val input = ("[1,2]\n" +
//            "[[1,2],3]").split("\n")
//    val input = ("[[[[[9,8],1],2],3],4]").split("\n")
//    val input = ("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").split("\n")
//    val input = ("[[[[4,3],4],4],[7,[[8,4],9]]]\n" +
//            "[1,1]").split("\n")

//    val input = ("[1,1]\n" +
//            "[2,2]\n" +
//            "[3,3]\n" +
//            "[4,4]\n" +
//            "[5,5]\n" +
//            "[6,6]").split("\n")

//    val input = ("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n" +
//            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\n" +
//            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\n" +
//            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\n" +
//            "[7,[5,[[3,8],[1,4]]]]\n" +
//            "[[2,[2,2]],[8,[8,1]]]\n" +
//            "[2,9]\n" +
//            "[1,[[[9,3],9],[[9,0],[0,7]]]]\n" +
//            "[[[5,[7,4]],7],1]\n" +
//            "[[[[4,2],2],6],[8,7]]").split("\n")

//    val input = ("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
//            "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
//            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
//            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
//            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
//            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
//            "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
//            "[[9,3],[[9,9],[6,[4,9]]]]\n" +
//            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
//            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]").split("\n")
    val start = System.nanoTime()
    day18part2(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day18part1(input: List<String>) {
    val snns = input.map { parseSnn(it, null)}
    var sum = snns[0]
    for (i in 1 until snns.size) {
        sum = addSnn(sum, snns[i])
        reduce(sum)
    }
    println(sum)
    println("magnitude: ${sum.magnitude()}")
}

fun day18part2(input: List<String>) {
    val snns = input.map { parseSnn(it, null)}
    println(snns)

    var max = 0L
    for (i in snns.indices) {
        for (j in snns.indices) {
            if (i != j) {
                val sum = addSnn(snns[i], snns[j])
                reduce(sum)
                val mag = sum.magnitude()
                if (mag > max) {
                    max = mag
                }
            }
        }
    }

    println("magnitude: $max")
}

// build a list of numbers across the tree from left to right
fun buildNumbers(snn: Snn, numbers: MutableList<SnnNumber>) {
    if (snn is SnnNumber) {
        numbers.add(snn)
    } else if (snn is SnnPair) {
        buildNumbers(snn.left!!, numbers)
        buildNumbers(snn.right!!, numbers)
    }
}

fun reduce(snn: Snn) {
    var exploded = false
    var split = false
    val reduceNumbers = mutableListOf<SnnNumber>()
    buildNumbers(snn, reduceNumbers)
//    println("reduceNumbers for $snn $reduceNumbers")
    // keep trying to explode or split until we can't do any more
    do {
        exploded = false
        split = false
        exploded = explode(snn, 0, reduceNumbers)
        if (!exploded) {
            split = split(reduceNumbers)
        }
//        println("Reduced $exploded $split to $snn ($reduceNumbers)")
    } while (exploded || split)
}

fun split(numbers: MutableList<SnnNumber>) : Boolean {
    val toSplit = numbers.firstOrNull { it.value >= 10 }
    if (toSplit != null) {
        val pair = SnnPair(toSplit.parent, null, null)
        pair.left = SnnNumber(pair, floor(toSplit.value / 2.0).toInt())
        pair.right = SnnNumber(pair, ceil(toSplit.value / 2.0).toInt())
        (toSplit.parent as SnnPair).replaceChild(toSplit, pair)

        val index = numbers.indexOf(toSplit)
        numbers.remove(toSplit)
        numbers.add(index, pair.right as SnnNumber)
        numbers.add(index, pair.left as SnnNumber)

        return true
    }
    return false
}

fun explode(snn: Snn, depth: Int, numbers: MutableList<SnnNumber>) : Boolean {
    if (snn is SnnPair) {
        if (depth == 4) {
//            println("Exploding $snn")
            assert(snn.left is SnnNumber && snn.right is SnnNumber)
            val leftIndex = numbers.indexOf(snn.left)
            if (leftIndex > 0) {
                numbers[leftIndex-1].value += (snn.left as SnnNumber).value
            }
            val rightIndex = numbers.indexOf(snn.right)
            if (rightIndex < numbers.size - 1) {
                numbers[rightIndex+1].value += (snn.right as SnnNumber).value
            }
            numbers.remove(snn.left)
            numbers.remove(snn.right)

            val zero = SnnNumber(snn.parent!!, 0)
            numbers.add(leftIndex, zero)
            (snn.parent!! as SnnPair).replaceChild(snn, zero)
            return true
        } else {
            return explode(snn.left!!, depth + 1, numbers) || explode(snn.right!!, depth + 1, numbers)
        }
    }
    return false
}

fun parseSnn(input: String, parent: Snn? = null) : Snn {
    if (input[0] == '[') {
        var comma = -1
        var opens = 0
        for (i in 1 until input.length) {
            if (input[i] == '[') opens++
            else if (input[i] == ']') opens--
            else if (input[i] == ',' && opens == 0) {
                comma = i;
                break
            }
        }
        val (left, right) = input.dropLast(1).let { s ->
            s.substring(1 until comma) to s.substring(comma+1 )
        }
        val pair = SnnPair(parent, null, null)
        pair.left = parseSnn(left, pair)
        pair.right = parseSnn(right, pair)
        return pair
    } else {
        return SnnNumber(parent, input.toInt())
    }
}

/**
 * This just adds, does not reduce.
 */
fun addSnn(first: Snn, second: Snn) : Snn {
    val pair = SnnPair(null, null, null)
    pair.left = first.deepCopy(pair)
    pair.right = second.deepCopy(pair)
    return pair
}

abstract class Snn(var parent: Snn?) {
    abstract fun magnitude() : Long
    abstract fun deepCopy(parent: Snn) : Snn
}
class SnnPair(parent: Snn?, var left: Snn?, var right: Snn?) : Snn(parent) {
    fun replaceChild(oldChild: Snn, newChild: Snn) {
        if (left == oldChild) {
            left = newChild
        } else if (right == oldChild) {
            right = newChild
        } else {
            println("!!! didn't have $oldChild to replace!")
        }
    }

    override fun deepCopy(parent: Snn) : Snn {
        val copy = SnnPair(parent, null, null)
        copy.left = left!!.deepCopy(copy)
        copy.right = right!!.deepCopy(copy)
        return copy
    }

    override fun magnitude(): Long {
        return (3 * left!!.magnitude()) + (2 * right!!.magnitude())
    }

    override fun toString(): String {
        return "[$left , $right]"
    }
}
class SnnNumber(parent: Snn?, var value: Int) : Snn(parent) {
    override fun magnitude(): Long {
        return value.toLong()
    }
    override fun deepCopy(parent: Snn) : Snn {
        return SnnNumber(parent, value)
    }

    override fun toString(): String {
        return value.toString()
    }
}