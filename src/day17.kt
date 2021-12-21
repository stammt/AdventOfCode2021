fun main(args: Array<String>) {
//    val input = File("/Users/stammt/Documents/2021aoc/day17input.txt").readLines().first()


//    val input = "target area: x=20..30, y=-10..-5"
    val input = "target area: x=79..137, y=-176..-117"

    // 1225 too low

    val start = System.nanoTime()
    day17part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day17part1(input: String) {
    val targetArea = input.drop("target area: ".length).split(", ").map(::parseRange).let{it[0] to it[1]}

    println(targetArea)

    val xVelocityRange = findVelocityRange(targetArea.first, ::sumUpTo)
    println("X velocity range $xVelocityRange")
//    val yVelocityRange = findVelocityRange(targetArea.second, ::gravity)
//    println("Y velocity range $yVelocityRange")

    var validVelocities = mutableListOf<Pair<Int, Int>>()
    var highest = Integer.MIN_VALUE
    for (vx in xVelocityRange!!) {
        for (vy in -500 until 500) {
//            println("Checking velocity $vx, $vy")
            var localHighest = Integer.MIN_VALUE
            var hitTarget = false
            val xSteps = sumUpTo(vx).toMutableList()
            val ySteps = gravity(vy, targetArea.second.minOrNull()!!)
            while (xSteps.size < ySteps.size) { xSteps.add(xSteps.last()) }
            while (xSteps.size > ySteps.size) { xSteps.removeLast() }
            if (vy == -4) {
                println("vy -4 $xSteps $ySteps")
            }
//            println("Checking steps $xSteps , $ySteps")
            for (step in xSteps.indices) {
                if (vx == 6 && vy == 9) {
                    println("6,9 ${xSteps[step]}, ${ySteps[step]}")
                }
                if (ySteps[step] > localHighest) localHighest = ySteps[step]
                if (inTargetArea(xSteps[step], ySteps[step], targetArea)) {
//                    println("HIT $step ${xSteps[step]},${ySteps[step]}")
                    hitTarget = true
//                    println("Found good velocity ${xSteps[step]},${ySteps[step]}")
                } else {
//                    println("MISS $step ${xSteps[step]},${ySteps[step]}")
                }
            }
            if (hitTarget) {
                if (localHighest > highest) highest = localHighest
                println("Hit target with $vx , $vy : $localHighest : $highest")
                validVelocities.add(vx to vy)
            }
        }
    }

    println("highest: $highest")
    println("valid count: ${validVelocities.size}")
    // 588 too low
}

fun inTargetArea(x: Int, y: Int, targetArea: Pair<IntRange, IntRange>) : Boolean {
    return targetArea.first.contains(x) && targetArea.second.contains(y)
}

fun findVelocityRange(targetRange: IntRange, fn: (Int) -> List<Int>) : IntRange? {
    // find x velocity range that can land in the target
    var minXVelocity = 0
    var found = false
    while (!found && minXVelocity < 100) {
        println("checking for min x ${minXVelocity + 1} ${fn(minXVelocity + 1)} against $targetRange")
        if (fn(++minXVelocity).intersect(targetRange).isNotEmpty()) found = true
    }
    if (!found) {
        println("Couldn't find min x velocity!")
        return null
    }
//    found = false
//    var maxXVelocity = minXVelocity
//    while (!found && maxXVelocity < 100) {
//        println("checking for max x ${maxXVelocity+1} ${fn(maxXVelocity + 1)} against $targetRange")
////        if (fn(++maxXVelocity).intersect(targetRange).isEmpty()) {
//        if (fn(++maxXVelocity).count{targetRange.contains(it)} == 0) {
//            println("no intersection")
//            found = true
//        }
//    }
//    if (!found) {
//        println("Couldn't find max x velocity!")
//        return null
//    }

    return minXVelocity .. targetRange.maxOrNull()!!
}

fun gravity(v: Int, min: Int = -200) : List<Int> {
    val steps = mutableListOf<Int>()
    var stepVelocity = v
    steps.add(0)
    while (stepVelocity >= 0) {
        steps.add(steps.last() + stepVelocity)
        stepVelocity--
    }
//    steps.add(0)
    while (steps.last() >= min) {
        steps.add(steps.last() + stepVelocity)
        stepVelocity--
    }
//    println("Steps for y velocity $v $steps")
    return steps
}

fun sumUpTo(v: Int) : List<Int> {
    return List(v) {index ->
        var acc = 0
        for (i in 0 until index) {
            acc += (v - i)
        }
        acc
    }
}

fun parseRange(input: String) : IntRange {
    // x=10..20
    return input.split("=")[1].split("..").map(String::toInt).let{it[0] .. it[1]}
}

