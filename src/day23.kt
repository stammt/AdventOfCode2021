import java.io.File
import java.util.*
import kotlin.math.min

fun main(args: Array<String>) {
    val start = System.nanoTime()

    val sampleinput = ("#############\n" +
            "#...........#\n" +
            "###B#C#B#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")

    val realinput = ("#############\n" +
            "#...........#\n" +
            "###D#C#B#C###\n" +
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
val costPerStep = mapOf("A" to 1, "B" to 10, "C" to 100, "D" to 1000)

var globalDebug = false

fun day23part1(input: List<String>) {
    // where each room is in the hallway
    val doors = mapOf<Int, Int>(2 to 0, 4 to 1, 6 to 2, 8 to 4)

    val debuginput3 = ("#############\n" +
            "#...B.......#\n" +
            "###B#C#.#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")
    val debug3 = parseMapState(debuginput3)

    val debuginput4 = ("#############\n" +
            "#...B.......#\n" +
            "###B#.#C#D###\n" +
            "  #A#D#C#A#\n" +
            "  #########").split("\n")
    val debug4 = parseMapState(debuginput4)

    val debuginput5 = ("#############\n" +
            "#...B.D.....#\n" +
            "###B#.#C#D###\n" +
            "  #A#.#C#A#\n" +
            "  #########").split("\n")
    val debug5 = parseMapState(debuginput5)

    val debuginput6 = ("#############\n" +
            "#.....D.....#\n" +
            "###B#.#C#D###\n" +
            "  #A#B#C#A#\n" +
            "  #########").split("\n")
    val debug6 = parseMapState(debuginput6)

    // setup initial map
    val initialState = parseMapState(input)

    val q = mutableListOf(initialState)
    val completedStates = mutableSetOf<MapState>()

    // map of state -> states that state can transition to, with cost
    val transitions = mutableMapOf<MapState, List<Pair<MapState, Int>>>()

    // for a map state, list of states that can transition to it
    val backLinks = mutableMapOf<MapState, MutableList<Pair<MapState, Int>>>()

    var r = 0

    while (q.isNotEmpty() /*&& ++r < 500*/) {
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
                if (currentState.rooms[i]!![0] != null) {
                    val pod = currentState.rooms[i]!![0]!!
                    val costPerStep = costPerStep[pod]!!
                    val targetRoom = targetRooms[pod]!!
                    var moved = false
                    // Only try to move if this is not the target room.
                    if (targetRoom != i) {
                        val stepsToTargetRoom = currentState.canReachRoomFromRoom(i, targetRoom)
//                        println("Moving top $pod from room $i slot 1 to room $targetRoom $stepsToTargetRoom")
                        if (stepsToTargetRoom != -1) {
                            val slotInTargetRoom = if (currentState.rooms[targetRoom]!![1] == null) 1 else 0
                            val stepsIntoTargetRoom = if (slotInTargetRoom == 0) 1 else 2
                            val cost = (costPerStep * stepsToTargetRoom) + (costPerStep * stepsIntoTargetRoom)
                            val nextState = currentState.copy()
                            nextState.rooms[i]!![0] = null
                            nextState.rooms[targetRoom]!![slotInTargetRoom] = pod
                            nextStates.add(nextState to cost)
                        }
                    }

                    // for the top slot, even if the pod is in the right room, if the room is not
                    // complete we must move it into the hallway
                    val potentialSteps = currentState.getPotentialHallwayPositionsFromRoom(i)
                    potentialSteps.forEach { step ->
//                            println("Moving top $pod from room $i to hallway $step")
                        val cost = costPerStep + (step.second * costPerStep)
                        val nextState = currentState.copy()
                        nextState.rooms[i]!![0] = null
                        nextState.hallway[step.first] = pod
                        nextStates.add(nextState to cost)
                    }
                } else if (currentState.rooms[i]!![1] != null) {
                    val pod = currentState.rooms[i]!![1]!!
                    val costPerStep = costPerStep[pod]!!
                    // If this pod is not in the right room, see if they can get to the right room
                    val targetRoom = targetRooms[pod]!!
                    if (targetRoom != i) {
                        val stepsToTargetRoom = currentState.canReachRoomFromRoom(i, targetRoom)
//                        println("Moving bottom $pod from room $i slot 1 to room $targetRoom $stepsToTargetRoom")
                        if (stepsToTargetRoom != -1) {
                            val slotInTargetRoom = if (currentState.rooms[targetRoom]!![1] == null) 1 else 0
                            val stepsIntoTargetRoom = if (slotInTargetRoom == 0) 1 else 2
                            val cost =
                                (costPerStep) + (costPerStep * stepsToTargetRoom) + (costPerStep * stepsIntoTargetRoom)
                            val nextState = currentState.copy()
                            nextState.rooms[i]!![1] = null
                            nextState.rooms[targetRoom]!![slotInTargetRoom] = pod
                            nextStates.add(nextState to cost)
                        }
                        // also try to send them into the hallway
                        val potentialSteps = currentState.getPotentialHallwayPositionsFromRoom(i)
                        potentialSteps.forEach { step ->
//                                println("Moving bottom $pod from room $i to hallway $step ")
                            val cost = (costPerStep * 2) + (step.second * costPerStep)
                            val nextState = currentState.copy()
                            nextState.rooms[i]!![1] = null
                            nextState.hallway[step.first] = pod
                            nextStates.add(nextState to cost)
                        }
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
//                    println("Moving $pod from hallway $i to room $targetRoom ")

                    val slotInTargetRoom = if (currentState.rooms[targetRoom]!![1] == null) 1 else 0
                    val stepsIntoTargetRoom = if (slotInTargetRoom == 0) 1 else 2
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

        globalDebug = false

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


fun parseMapState(input: List<String>) : MapState {
    // map of the hallway
    val hallway = arrayOfNulls<String?>(11)
    // map of rooms
    val rooms = mapOf<Int, Array<String?>>(
        0 to arrayOfNulls(2),
        1 to arrayOfNulls(2),
        2 to arrayOfNulls(2),
        3 to arrayOfNulls(2)
    )

    input[1].drop(1).dropLast(1).forEachIndexed { i, c ->
        if (c != '.') hallway[i] = c.toString()
    }
    input[2].drop(3).dropLast(3).split("#").forEachIndexed { index, s ->
        if (s != ".") {
            rooms[index]!![0] = s
        }
    }
    input[3].drop(3).dropLast(1).split("#").forEachIndexed { index, s ->
        if (s != ".") {
            rooms[index]!![1] = s
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
        return rooms[0]!![0] == "A" && rooms[0]!![1] == "A"
                && rooms[1]!![0] == "B" && rooms[1]!![1] == "B"
                && rooms[2]!![0] == "C" && rooms[2]!![1] == "C"
                && rooms[3]!![0] == "D" && rooms[3]!![1] == "D"
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
        for (i in start-1 downTo 0) {
            steps += 1
            if (hallway[i] != null) break
            if (!isDoor(i)) positions.add(i to steps)
        }
        steps = 0
        for (i in start+1 until hallway.size) {
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
        if (rooms[index]!![0] != null) {
            if (globalDebug)
                println("Slot 0 occupied in room $index")
            return false
        }
        if (rooms[index]!![1] != null) {
            val slot1Target = targetRooms[rooms[index]!![1]!!]!!
            if (slot1Target != index) {
                if (globalDebug)
                    println("Slot 1 occupied with wrong type ${rooms[index]!![1]!!} in room $index")
                return false
            }
        }
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
        s += "##${rooms[0]!![0] ?: '.'}#${rooms[1]!![0] ?: '.'}#${rooms[2]!![0] ?: '.'}#${rooms[3]!![0] ?: '.'}##\n"
        s += "##${rooms[0]!![1] ?: '.'}#${rooms[1]!![1] ?: '.'}#${rooms[2]!![1] ?: '.'}#${rooms[3]!![1] ?: '.'}##\n"
        s += "  #########\n"
        return s
    }
}
