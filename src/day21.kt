import java.io.File
import java.util.*
import kotlin.math.*

fun main(args: Array<String>) {
    val start = System.nanoTime()
    day21part2()
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day21part1() {
    var p1Pos =  5 // 6, zero based
    var p2Pos =  0 // 1, zero based
    var p1Score = 0
    var p2Score = 0

    val die = DeterministicDie()

    do {
        val move1 = die.roll() + die.roll() + die.roll()
        p1Pos = (p1Pos + move1) % 10
        p1Score += (p1Pos + 1)

        if (p1Score < 1000) {
            val move2 = die.roll() + die.roll() + die.roll()
            p2Pos = (p2Pos + move2) % 10
            p2Score += (p2Pos + 1)
        }
        println("$p1Score at $p1Pos , $p2Score at $p2Pos , ${die.rolls} rolls")
    } while (p1Score < 1000 && p2Score < 1000)

    println("$p1Score at $p1Pos , $p2Score at $p2Pos , ${die.rolls} rolls")
    println(min(p1Score, p2Score) * die.rolls)
}

fun day21part2() {
    val initialState = GameState(PlayerState(5, 0), PlayerState( 0, 0))

    var p1Wins = 0L
    var p2Wins = 0L

    val stateCounts = mutableMapOf<GameState, Long>()
    stateCounts[initialState] = 1L

    while (stateCounts.isNotEmpty()) {
        // take the next state, progress it, and then add the progressed states
        // to the map (incrementing the count if it is already found).
        // If the state has a winner, add to the winner tally and don't add it back.
        val state = stateCounts.minByOrNull { it.key.p1State.score }!!
        val countsOfPreviousState = stateCounts[state.key] ?: 0L
        stateCounts.remove(state.key)

        val p1NextStates = state.key.p1State.progress()
        val p2NextStates = state.key.p2State.progress()

        for (p1 in p1NextStates) {
            if (p1.isWinning()) {
                p1Wins += countsOfPreviousState
            } else {
                for (p2 in p2NextStates) {
                    if (p2.isWinning()) {
                        p2Wins += countsOfPreviousState
                    } else {
                        val nextGameState = GameState(p1, p2)
                        val count = stateCounts[nextGameState] ?: 0L
                        stateCounts[nextGameState] = count + countsOfPreviousState
                    }
                }
            }
        }
    }

    println("p1: $p1Wins , p2: $p2Wins : ${max(p1Wins, p2Wins)}")
}


data class PlayerState(val pos: Int, val score: Int) {
    fun progress() : List<PlayerState> {
        val states = mutableListOf<PlayerState>()
        for (a in 1..3) {
            for (b in 1..3) {
                for (c in 1..3) {
                    states.add(progressForRolls(a, b, c))
                }
            }
        }
        return states
    }

    private fun progressForRolls(r1: Int, r2: Int, r3: Int) : PlayerState {
        val move = r1+r2+r3
        val nextPos = (pos + move) % 10
        val nextScore = score + nextPos + 1
        return PlayerState(nextPos, nextScore)
    }

    fun isWinning() : Boolean {
        return score >= 21
    }
}

data class GameState(val p1State: PlayerState, val p2State: PlayerState)

class DeterministicDie() {
    var lastRoll = -1
    var rolls = 0L

    fun roll() : Int {
        rolls++
        lastRoll = (lastRoll+1) % 100
        return lastRoll + 1
    }
}

