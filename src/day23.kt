import java.io.File
import java.util.*
import kotlin.math.min

fun main(args: Array<String>) {
    val start = System.nanoTime()

    val sampleinput = ("#############\n" +
            "#...........#\n" +
            "###B#C#B#D###\n" +
            "  #D#C#B#A#\n" +
            "  #D#B#A#C#\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")

    val sampleinput1 = ("#############\n" +
            "#...........#\n" +
            "###B#C#B#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")

    val realinput = ("#############\n" +
            "#...........#\n" +
            "###D#C#B#C###\n" +
            "  #D#C#B#A#\n" +
            "  #D#B#A#C#\n" +
            "  #D#A#A#B#\n" +
            "  #########").split("\n")

    val debuginput1 = ("#############\n" +
            "#D..B.C.B.A.#\n" +
            "###.#.#.#.###\n" +
            "  #A#D#C#.#\n" +
            "  #########").split("\n")

    val debuginput2 = ("#############\n" +
            "#...........#\n" +
            "###B#C#B#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")

    val debuginput3 = ("#############\n" +
            "#...B.......#\n" +
            "###B#C#.#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")

    day23part1(realinput)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

val targetRooms = mapOf("A" to 0, "B" to 1, "C" to 2, "D" to 3)
val targetTypeForRoom = mapOf(0 to "A", 1 to "B", 2 to "C", 3 to "D")
val costPerStep = mapOf("A" to 1, "B" to 10, "C" to 100, "D" to 1000)

var globalDebug = false

fun day23part1(input: List<String>) {

    // setup initial map
    val initialState = parseMapState(input, 4)
    println("initial: $initialState")

    val q = mutableListOf(initialState)
    val completedStates = mutableSetOf<MapState>()

    // map of state -> states that state can transition to, with cost
    val transitions = mutableMapOf<MapState, List<Pair<MapState, Int>>>()

    // for a map state, list of states that can transition to it
    val backLinks = mutableMapOf<MapState, MutableList<Pair<MapState, Int>>>()

    var r = 0

    while (q.isNotEmpty() /*&& ++r < 250*/) {
        // build all potential next states, and add them to the queue
        val currentState = q.removeAt(0)
        if (q.size % 1000 == 0) {
            println("q size is ${q.size}, completed ${completedStates.size}")
        }


        // for each room, try to move the top amphipod to it's target room. If there is no path, move to
        // any potential spot in the hallway.
        val nextStates = mutableListOf<Pair<MapState, Int>>()
        for (i in 0..3) {
            // If the room is complete, skip it
            if (!currentState.roomIsComplete(i)) {
//                println("room $i is not complete")
                val roomTop = currentState.rooms[i]!!.indexOfFirst { it != null }
                if (roomTop != -1) {
                    val pod = currentState.rooms[i]!![roomTop]!!
                    val costPerStep = costPerStep[pod]!!
                    val targetRoom = targetRooms[pod]!!
                    // Only try to move if this is not the target room.
                    if (targetRoom != i) {
                        val stepsToTargetRoom = currentState.canReachRoomFromRoom(i, targetRoom)
//                        println("Moving top $pod from room $i slot 1 to room $targetRoom $stepsToTargetRoom")
                        if (stepsToTargetRoom != -1) {
                            val slotInTargetRoom = currentState.rooms[targetRoom]!!.indexOfLast { it == null }
                            val stepsIntoTargetRoom = slotInTargetRoom + 1
                            val cost = (costPerStep * roomTop) + (costPerStep * stepsToTargetRoom) + (costPerStep * stepsIntoTargetRoom)
                            val nextState = currentState.copy()
                            nextState.rooms[i]!![roomTop] = null
                            nextState.rooms[targetRoom]!![slotInTargetRoom] = pod
                            nextStates.add(nextState to cost)
                        }
                    }

                    // for the top slot, even if the pod is in the right room, if the room is not
                    // complete we must move it into the hallway
                    val potentialSteps = currentState.getPotentialHallwayPositionsFromRoom(i)
                    potentialSteps.forEach { step ->
//                            println("Moving top $pod from room $i to hallway $step")
                        val cost = (costPerStep * roomTop) + (step.second * costPerStep)
                        val nextState = currentState.copy()
                        nextState.rooms[i]!![roomTop] = null
                        nextState.hallway[step.first] = pod
                        nextStates.add(nextState to cost)
                    }
                }
            }
        }

        // for each pod in a hallway, try move to its target room
        for (i in currentState.hallway.indices) {
            if (currentState.hallway[i] != null) {
                val pod = currentState.hallway[i]!!
                val costPerStep = costPerStep[pod]!!

                val targetRoom = targetRooms[pod]!!
                val stepsToTargetRoom = currentState.canReachRoomFromHallway(i, targetRoom)
//                println("Checked to send ${pod.type} from hallway $i to target room $targetRoom == $stepsToTargetRoom")
                if (stepsToTargetRoom != -1) {
//                    println("Moving $pod from hallway $i to room $targetRoom in $currentState")

                    val slotInTargetRoom = currentState.rooms[targetRoom]!!.indexOfLast { it == null }
                    val stepsIntoTargetRoom = slotInTargetRoom + 1
                    val cost = (costPerStep * stepsToTargetRoom) + (costPerStep * stepsIntoTargetRoom)
                    val nextState = currentState.copy()
                    nextState.hallway[i] = null
                    nextState.rooms[targetRoom]!![slotInTargetRoom] = pod
                    nextStates.add(nextState to cost)
//                    println("Gives next state $nextState")
                }
            }
        }

//        println("${nextStates.size} next states from $currentState")
        if (globalDebug) {
            println("Transitions from $currentState are ${nextStates}")
        }

        nextStates.forEach {
            // if we don't already have transitions from the next state, add it to the queue
            val nextState = it.first
            if (nextState.isComplete()) {
                completedStates.add(nextState)
            } else if (transitions[nextState] == null && !q.contains(nextState)) {
                q.add(nextState)
            }

            val backs = backLinks[nextState] ?: mutableListOf()
            backs.add(currentState to it.second)
        }

        // add transitions for the current state
        transitions[currentState] = nextStates.toList()
//        println("have ${transitions.size} transitions")

//        completedStates.addAll(nextStates.filter { it.isComplete() })

//        globalDebug = false

//        if (debug) {
//            println("Potential next states: $nextStates\n")
//            println("Completed states: $completedStates")
//            println("----------\n\n")
//        }
    }

    println("Completed states: ${completedStates.size}")

    val stateCosts = mutableMapOf<MapState, Long>()
    getMinCostToAllStates(initialState, 0L, stateCosts, transitions)
    val minCost = stateCosts[completedStates.first()]

    println("min cost: $minCost")
}

fun getMinCostToAllStates(currentState: MapState, currentCost: Long, stateCosts: MutableMap<MapState, Long>, allTransitions: Map<MapState, List<Pair<MapState, Int>>>) {
    val transitions = allTransitions[currentState] ?: return

    for (t in transitions) {
        val nextState = t.first
        val currentCostToNextState = stateCosts[nextState] ?: Long.MAX_VALUE
        val nextCost = currentCost + t.second
        if (nextCost < currentCostToNextState) {
            stateCosts[nextState] = min(currentCost + t.second, currentCostToNextState)
            getMinCostToAllStates(nextState, currentCost + t.second, stateCosts, allTransitions)
        }
    }
}


fun parseMapState(input: List<String>, roomDepth: Int) : MapState {
    // map of the hallway
    val hallway = arrayOfNulls<String?>(11)
    // map of rooms
    val rooms = mapOf<Int, Array<String?>>(
        0 to arrayOfNulls(roomDepth),
        1 to arrayOfNulls(roomDepth),
        2 to arrayOfNulls(roomDepth),
        3 to arrayOfNulls(roomDepth)
    )

    input[1].drop(1).dropLast(1).forEachIndexed { i, c ->
        if (c != '.') hallway[i] = c.toString()
    }

    for (d in 0 until roomDepth) {
        input[d + 2].dropWhile { it == ' ' || it == '#' }.dropLastWhile { it == ' ' || it == '#' }.split("#").forEachIndexed { index, s ->
            if (s != ".") {
                rooms[index]!![d] = s
            }
        }
    }
    return MapState(hallway, rooms)
}

data class MapState(val hallway: Array<String?>, val rooms: Map<Int, Array<String?>>) {
    override fun equals(other: Any?): Boolean {
        if (other !is MapState) return false

        return hallway.contentEquals(other.hallway) &&
                rooms[0].contentEquals(other.rooms[0]) &&
                rooms[1].contentEquals(other.rooms[1]) &&
                rooms[2].contentEquals(other.rooms[2]) &&
                rooms[3].contentEquals(other.rooms[3])
    }

    override fun hashCode(): Int {
        return hallway.contentHashCode() +
                rooms[0].contentHashCode() +
                rooms[1].contentHashCode() +
                rooms[2].contentHashCode()+
                rooms[3].contentHashCode()
    }

    fun isComplete() : Boolean {
        return rooms[0]?.filterNot { it == "A" }!!.isEmpty() &&
                rooms[1]?.filterNot { it == "B" }!!.isEmpty() &&
                rooms[2]?.filterNot { it == "C" }!!.isEmpty() &&
                rooms[3]?.filterNot { it == "D" }!!.isEmpty()
    }

    fun copy() : MapState {
        val hallwayCopy = hallway.copyOf()
        val roomsCopy = mutableMapOf<Int, Array<String?>>()
        for (i in 0..3) {
            roomsCopy[i] = rooms[i]!!.copyOf()
        }
        return MapState(hallwayCopy, roomsCopy)
    }

    // return pair of hallway position to number of steps
    fun getPotentialHallwayPositionsFromRoom(room: Int) : List<Pair<Int, Int>> {
        val start = when (room) {
            0 -> 2
            1 -> 4
            2 -> 6
            3 -> 8
            else -> 12
        }
        val positions = mutableListOf<Pair<Int, Int>>()
        var steps = 0
        for (i in start downTo 0) {
            steps += 1
            if (hallway[i] != null) break
            if (!isDoor(i)) positions.add(i to steps)
        }
        steps = 0
        for (i in start until hallway.size) {
            steps += 1
            if (hallway[i] != null) break
            if (!isDoor(i)) positions.add(i to steps)
        }
        return positions
    }

    // return pair of room position to number of steps
    fun canReachRoomFromRoom(fromRoom: Int, toRoom: Int) : Int {
        // if the room is occupied with a pod that is not in its target room, we can't go
        // there yet
        if (!canEnterRoom(toRoom)) return -1

        val start = when (fromRoom) {
            0 -> 2
            1 -> 4
            2 -> 6
            3 -> 8
            else -> 12
        }
        val end = when (toRoom) {
            0 -> 2
            1 -> 4
            2 -> 6
            3 -> 8
            else -> 12
        }
        var success = false
        var steps = 0
        if (start < end) {
            for (i in start .. end) {
                steps += 1
                if (hallway[i] != null) break
                if (i == end) success = true
            }
        } else if (start > end) {
            for (i in start downTo end) {
                steps += 1
                if (hallway[i] != null) break
                if (i == end) success = true
            }
        }
        return if (success) steps else -1
    }

    // return pair of room position to number of steps
    fun canReachRoomFromHallway(start: Int, toRoom: Int) : Int {
        // if the room is occupied with a pod that is not in its target room, we can't go
        // there yet
        if (!canEnterRoom(toRoom)) return -1

        val end = when (toRoom) {
            0 -> 2
            1 -> 4
            2 -> 6
            3 -> 8
            else -> 12
        }
        var success = false
        var steps = 0
        if (start < end) {
            for (i in start+1 .. end) {
                steps += 1
                if (hallway[i] != null) {
//                    println("hallway $i occupied (${hallway[i]}")
                    break
                }
                if (i == end) success = true
            }
        } else if (start > end) {
            for (i in start-1 downTo end) {
                steps += 1
                if (hallway[i] != null) break
                if (i == end) success = true
            }
        }
        if (globalDebug) {
            println("Can enter room $toRoom from $start : $success $steps")
        }
        return if (success) steps else -1
    }

    fun canEnterRoom(index: Int) : Boolean {
        // if the room is occupied with a pod that is not in its target room, we can't go
        // there yet
        val type = targetTypeForRoom[index]!!

        if (rooms[index]!!.any {it != null && it != type }) return false
        return true
    }

    fun isDoor(index: Int) : Boolean {
        return index == 2 || index == 4 || index == 6 || index == 8
    }

    fun roomIsComplete(index: Int) : Boolean {
        val room = rooms[index]!!
        val type = when (index) {
            0 -> "A"
            1 -> "B"
            2 -> "C"
            3 -> "D"
            else -> "X"
        }
        return room[0] == type && room[1] == type
    }

    override fun toString(): String {
        var s = "isComplete ${isComplete()}\n"
        s += hallway.map { it ?: '.' }.joinToString("") + "\n"
        for (d in 0 until rooms[0]!!.size) {
            s += "##${rooms[0]!![d] ?: '.'}#${rooms[1]!![d] ?: '.'}#${rooms[2]!![d] ?: '.'}#${rooms[3]!![d] ?: '.'}##\n"
        }
        s += "  #########\n"
        return s
    }
}
