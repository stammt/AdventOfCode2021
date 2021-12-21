import java.io.File
import java.util.*
import kotlin.math.*

fun main(args: Array<String>) {
    val fileinput = File("/Users/stammt/Documents/2021aoc/day20input.txt").readLines()

    val sampleinput = ("..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
            "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
            ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
            ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
            ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
            "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
            "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n" +
            "\n" +
            "#..#.\n" +
            "#....\n" +
            "##..#\n" +
            "..#..\n" +
            "..###").split("\n")
    val start = System.nanoTime()
    day20part1(fileinput)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day20part1(input: List<String>) {
    val enhancement = input[0]

    println(enhancement)
    println(enhancement.length)

    val imageInput = input.drop(2)
    val lightPixels = mutableSetOf<Pair<Int, Int>>()
    for (y in imageInput.indices) {
        val line = imageInput[y]
        for (x in line.indices) {
            if (line[x] == '#') lightPixels.add(x to y)
        }
    }

//    println(lightPixels)

    for (reps in 0 until 50) {
        val width = lightPixels.maxByOrNull { it.first }!!.first
        val height = lightPixels.maxByOrNull { it.second }!!.second

        println("rep $reps $width x $height")
//        printImage(lightPixels)

        val outOfBoundsIsLight = (reps != 0) && (reps%2 != 0) && (enhancement[0] == '#')
        if (outOfBoundsIsLight) {
            println("adding virtual border for $reps")
            // add a virtual border of lit pixels
            for (y in -2 .. height+2) {
                lightPixels.add(-1 to y)
                lightPixels.add(-2 to y)
                lightPixels.add(width+1 to y)
                lightPixels.add(width+2 to y)
            }
            for (x in -2 .. width+2) {
                lightPixels.add(x to -1)
                lightPixels.add(x to -2)
                lightPixels.add(x to height+1)
                lightPixels.add(x to height+2)
            }
        }

        val enhancedPixels = mutableSetOf<Pair<Int, Int>>()
        for (y in -1..height+1) {
            for (x in -1..width+1) {
                val i = getEnhancementIndex(x to y, lightPixels)
                val replace = enhancement[i]
                if (replace == '#') {
//                    if (x == -2 || y == -2) {
//                        println("replacing ${x to y} with '#' from $i")
//                    }
                    enhancedPixels.add(x to y)
                }
            }
        }

        lightPixels.clear()
        lightPixels.addAll(enhancedPixels.map{ (it.first + 1 to it.second + 1)})
    }


    println("\nDone")
    printImage(lightPixels)

    // try 5622

    // 5551 too low
    // 5956 too high
    // 5851 too high
    // not 5875
    println("lit: ${lightPixels.size}")
}

fun getEnhancementIndex(pixel: Pair<Int, Int>, lightPixels: Set<Pair<Int, Int>>) : Int {
    var s = ""
    for (y in -1..1) {
        for (x in -1..1) {
            s += if (lightPixels.contains(pixel.first + x to pixel.second + y)) "1" else "0"
        }
    }
    return Integer.parseInt(s, 2)
}

fun printImage(lightPixels: Set<Pair<Int, Int>>) {
    val width = lightPixels.maxByOrNull { it.first }!!.first
    val height = lightPixels.maxByOrNull { it.second }!!.second

    var s = ""
    for (y in 0..height) {
        var line = ""
        for (x in 0..width) {
            line += if (lightPixels.contains(x to y)) '#' else '.'
        }
        s += line + "\n"
    }
    println(s)
}
