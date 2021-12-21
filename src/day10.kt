import java.io.File
import kotlin.math.floor

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day10input.txt").readLines()

//    val input = ("[({(<(())[]>[[{[]{<()<>>\n" +
//            "[(()[<>])]({[<{<<[]>>(\n" +
//            "{([(<{}[<>[]}>{[]{[(<()>\n" +
//            "(((({<>}<{<{<>}{[]{[]{}\n" +
//            "[[<[([]))<([[{}[[()]]]\n" +
//            "[{[{({}]{}}([{[{{{}}([]\n" +
//            "{<[[]]>}<{[{[{[]{()[[[]\n" +
//            "[<(<(<(<{}))><([]([]()\n" +
//            "<{([([[(<>()){}]>(<<{{\n" +
//            "<{([{{}}[<[[[<>{}]]]>[]]").split("\n")
    val start = System.nanoTime()
    day10part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day10part1(input: List<String>) {
    var errorSum = 0L
    var incompleteScores = mutableListOf<Long>()

    val delimiters = mutableSetOf(
        Delimiter('(', ')', 3, 1),
        Delimiter('[', ']', 57, 2),
        Delimiter('{', '}', 1197, 3),
        Delimiter('<', '>', 25137, 4)
    )
    for (line in input) {
        val openers = mutableListOf<Char>()
        var invalid = false
        for (c in line) {
            if (delimiters.any { it.opener == c }) {
                openers.add(c)
            } else {
                val delimiter = delimiters.firstOrNull { it.closer == c }
                if (delimiter != null) {
                    if (openers.isEmpty() || openers.last() != delimiter.opener) {
                        errorSum += delimiter.errorScore
                        invalid = true
                        break
                    } else {
                        openers.removeLast()
                    }
                }
            }
        }

        if (!invalid) {
            // unwind the openers and build the score
            var totalScore = 0L
            while (openers.isNotEmpty()) {
                var c = openers.removeLast()
                val delimiter = delimiters.firstOrNull { it.opener == c }
                if (delimiter != null) {
                    totalScore = (totalScore * 5) + delimiter.incompleteScore
                }
            }
            incompleteScores.add(totalScore)
        }
    }

    incompleteScores.sort()
    val incompleteScore = incompleteScores[floor(incompleteScores.size / 2.0).toInt()]

    println("$errorSum errors, $incompleteScore incompletes ")
}

class Delimiter(val opener: Char, val closer: Char, val errorScore: Long, val incompleteScore: Long)
