import java.io.File
import java.util.regex.Pattern

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day04input.txt").readLines()

//    val input = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n" +
//            "\n" +
//            "22 13 17 11  0\n" +
//            " 8  2 23  4 24\n" +
//            "21  9 14 16  7\n" +
//            " 6 10  3 18  5\n" +
//            " 1 12 20 15 19\n" +
//            "\n" +
//            " 3 15  0  2 22\n" +
//            " 9 18 13 17  5\n" +
//            "19  8  7 25 23\n" +
//            "20 11 10 24  4\n" +
//            "14 21 16 12  6\n" +
//            "\n" +
//            "14 21 17 24  4\n" +
//            "10 16 15  9 19\n" +
//            "18  8 23 26 20\n" +
//            "22 11 13  6  5\n" +
//            " 2  0 12  3  7"
    val start = System.nanoTime()
    day04part2(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}


fun day04part2(lines: List<String>) {
    val numbersToPlay = lines[0].split(',').map(String::toInt)
    val boardsAsStrings = lines.subList(1, lines.size).chunked(6) { it.subList(1, 6).joinToString(" ") }

    val boardLists = boardsAsStrings.map{it.trim().split(Pattern.compile("\\s+")).map(String::trim).map(String::toInt)}
    val boards = boardLists.map{BingoBoard(it)}
    val winningBoards = mutableListOf<BingoBoard>()

    numbersToPlay.forEach {
        boards.filter {board -> !winningBoards.contains(board) }.forEach { board ->
            val marked = board.mark(it)
            if (marked && board.winning) {
                winningBoards.add(board)
            }
        }
    }

    val lastWinner = winningBoards.last()
    val sum = lastWinner.sumOfUnmarkedNumbers()
    println("$sum * ${lastWinner.winningNumber} = ${sum * lastWinner.winningNumber}")
}

class BingoBoard(val numbers: List<Int>) {
    private val marked = MutableList(5) { MutableList(5) { false } }
    var winningNumber = -1
    var winning = false

    fun mark(value: Int) : Boolean {
        val idx = numbers.indexOf(value)
        if (idx != -1) {
            marked[idx / 5][idx % 5] = true
            if (checkWinning() && !winning) {
                winning = true
                winningNumber = value
            }
            return true
        }
        return false
    }

    fun sumOfUnmarkedNumbers() : Int {
        var sum = 0
        for (i in numbers.indices) {
            if (!marked[i/5][i%5]) sum += numbers[i]
        }
        return sum
    }

    private fun checkWinning() : Boolean {
        // check rows
        if (marked.any { it.filter {x-> x }.size == 5 }) return true;

        // check cols
        for (i in 0..4) {
            if (marked.all { it[i] }) return true
        }

        return false
    }
}