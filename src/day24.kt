import java.io.File
import java.lang.IllegalArgumentException
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

fun main(args: Array<String>) {
    val fileinput = File("/Users/stammt/Documents/2021aoc/day24input.txt").readLines()

    val sample1 = ("inp x\n" +
            "mul x -1").split("\n")

    val sample2 = ("inp z\n" +
            "inp x\n" +
            "mul z 3\n" +
            "eql z x").split("\n")

    val sample3 = ("inp w\n" +
            "mul w -1\n" +
            "mod w 2\n" +
            "div x -2").split("\n")

    val bin = ("inp w\n" +
            "add z w\n" +
            "mod z 2\n" +
            "div w 2\n" +
            "add y w\n" +
            "mod y 2\n" +
            "div w 2\n" +
            "add x w\n" +
            "mod x 2\n" +
            "div w 2\n" +
            "mod w 2").split("\n")

    val start = System.nanoTime()
//    testBin(bin)
    day24part1(fileinput)
//    testMod()
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

//fun testBin(input: List<String>) {
//    val alu = Alu()
//    val commands = input.map(::parseAluOp)
//    alu.run(commands, "7")
//    println(alu)
//}

fun testMod() {
    val alu = Alu()
    alu.setValue("x", "16")

    val mod = ModCommand("mod", "x", "26")
    println(alu)
    alu.run(listOf(mod), "")
    println(alu)
}

fun day24part1(input: List<String>) {
    val commands = input.map(::parseAluOp)
    val alu = Alu()

    // checked 99..99 down to 99999990298388
    // checked 13579999999999 down to 13579884821274
    var modelNumber = 99999999999999 // 13579246899999 // 13579944959999 //13579969733699 // 13579989700000 // 99999999698387L // 99999999999999L
    var done = false
    var i = 0
    while (modelNumber > 0L && (++i < 2)) {
        if ((i % 100000) == 0) {
            println("checking $modelNumber")
        }
        modelNumber--
        var s = modelNumber.toString().padStart(14, '0')
//        if (s[0] == '0') break


        var skipZero = 0
        val startSkip = modelNumber
        while (s.contains('0')) {
            skipZero++
            modelNumber--
            s = modelNumber.toString().padStart(14, '0')
        }
//        if (skipZero > 999) {
//            println("Skipped $skipZero $startSkip to $modelNumber")
//        }
//        println("testing $s")
        alu.run(commands, s)
        if (alu.getValue("z") == 0L) {
            println("valid: $s $alu")
            break
//            done = true
        }

//        var i = modelNumber.size - 1
//        if (modelNumber[i] > 1) {
//            modelNumber[i] = modelNumber[i] - 1
//        } else {
//            while (i >= 0 && modelNumber[i] == 1) {
//                modelNumber[i] = 9
//                i--
//            }
//            if (i < 0) done = true else {
//                modelNumber[i] = modelNumber[i] - 1
//            }
//        }
    }

    // 99999999698387 too high
}

fun parseAluOp(s: String) : AluCommand {
    val segments = s.split(" ")
    val op = segments[0]
    val p1 = segments[1]
    val p2 = if (segments.size > 2) segments[2] else null

    return when (op) {
        "inp" -> InputCommand(op, p1)
        "add" -> AddCommand(op, p1, p2!!)
        "mul" -> MulCommand(op, p1, p2!!)
        "div" -> DivCommand(op, p1, p2!!)
        "mod" -> ModCommand(op, p1, p2!!)
        "eql" -> EqlCommand(op, p1, p2!!)
        else -> throw IllegalArgumentException(op)
    }
}

class Alu() {
    private var w: Long = 0L
    private var x: Long = 0L
    private var y: Long = 0L
    private var z: Long = 0L

    fun run(ops: List<AluCommand>, s: String) {
        val input = ProgramInput(s)
        w = 0L
        x = 0L
        y = 0L
        z = 0L
        for (op in ops) {
            op.run(this, input)
            println("$op -> $this")
        }
    }

    fun getValue(v: String) : Long {
        return when (v) {
            "w" -> w
            "x" -> x
            "y" -> y
            "z" -> z
            else -> v.toLong()
        }
    }

    fun setValue(register: String, v: String) {
        val value = getValue(v)
        when (register) {
            "w" -> w = value
            "x" -> x = value
            "y" -> y = value
            "z" -> z = value
            else -> println("unknown register $register")
        }
    }

    override fun toString(): String {
        return "[ALU: w=$w, x=$x, y=$y, z=$z]"
    }
}

class ProgramInput(val input:String) {
    var i = -1

    fun read() : String {
        return input[++i].toString()
    }
}

abstract class AluCommand(val op: String, val p1: String, val p2: String) {
    abstract fun run(alu: Alu, input: ProgramInput)

    override fun toString(): String {
        return "($op $p1 $p2)"
    }
}

class InputCommand(op: String, p1: String): AluCommand(op, p1,"") {
    override fun run(alu: Alu, input: ProgramInput) {
        alu.setValue(p1, input.read())
    }
}

class AddCommand(op: String, p1: String, p2: String): AluCommand(op, p1, p2) {
    override fun run(alu: Alu, input: ProgramInput) {
        val v1 = alu.getValue(p1)
        val v2 = alu.getValue(p2)
        alu.setValue(p1, (v1 + v2).toString())
    }
}

class MulCommand(op: String, p1: String, p2: String): AluCommand(op, p1, p2) {
    override fun run(alu: Alu, input: ProgramInput) {
        val v1 = alu.getValue(p1)
        val v2 = alu.getValue(p2)
        alu.setValue(p1, (v1 * v2).toString())
    }
}

class DivCommand(op: String, p1: String, p2: String): AluCommand(op, p1, p2) {
    override fun run(alu: Alu, input: ProgramInput) {
        val a = alu.getValue(p1)
        val b = alu.getValue(p2)
        val result = a.toDouble() / b.toDouble()
        val rounded = if (result > 0) floor(result) else ceil(result)
//        println("$a / $b is ${rounded.toInt()} (from $result)")
        alu.setValue(p1, (rounded.toInt()).toString())
    }
}

class ModCommand(op: String, p1: String, p2: String): AluCommand(op, p1, p2) {
    override fun run(alu: Alu, input: ProgramInput) {
        val v1 = alu.getValue(p1)
        val v2 = alu.getValue(p2)
        alu.setValue(p1, (v1 % v2).toString())
    }
}

class EqlCommand(op: String, p1: String, p2: String): AluCommand(op, p1, p2) {
    override fun run(alu: Alu, input: ProgramInput) {
        val v1 = alu.getValue(p1)
        val v2 = alu.getValue(p2)
        alu.setValue(p1, if (v1 == v2) "1" else "0")
    }
}

