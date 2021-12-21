import java.io.File
import java.util.*
import kotlin.math.*

fun main(args: Array<String>) {
    val fileinput = File("/Users/stammt/Documents/2021aoc/day19input.txt").readLines()

    val twoscanners = ("--- scanner 0 ---\n" +
            "404,-588,-901\n" +
            "528,-643,409\n" +
            "-838,591,734\n" +
            "390,-675,-793\n" +
            "-537,-823,-458\n" +
            "-485,-357,347\n" +
            "-345,-311,381\n" +
            "-661,-816,-575\n" +
            "-876,649,763\n" +
            "-618,-824,-621\n" +
            "553,345,-567\n" +
            "474,580,667\n" +
            "-447,-329,318\n" +
            "-584,868,-557\n" +
            "544,-627,-890\n" +
            "564,392,-477\n" +
            "455,729,728\n" +
            "-892,524,684\n" +
            "-689,845,-530\n" +
            "423,-701,434\n" +
            "7,-33,-71\n" +
            "630,319,-379\n" +
            "443,580,662\n" +
            "-789,900,-551\n" +
            "459,-707,401\n" +
            "\n" +
            "--- scanner 1 ---\n" +
            "686,422,578\n" +
            "605,423,415\n" +
            "515,917,-361\n" +
            "-336,658,858\n" +
            "95,138,22\n" +
            "-476,619,847\n" +
            "-340,-569,-846\n" +
            "567,-361,727\n" +
            "-460,603,-452\n" +
            "669,-402,600\n" +
            "729,430,532\n" +
            "-500,-761,534\n" +
            "-322,571,750\n" +
            "-466,-666,-811\n" +
            "-429,-592,574\n" +
            "-355,545,-477\n" +
            "703,-491,-529\n" +
            "-328,-685,520\n" +
            "413,935,-424\n" +
            "-391,539,-444\n" +
            "586,-435,557\n" +
            "-364,-763,-893\n" +
            "807,-499,-711\n" +
            "755,-354,-619\n" +
            "553,889,-390").split("\n")

    val fullsample = ("--- scanner 0 ---\n" +
            "404,-588,-901\n" +
            "528,-643,409\n" +
            "-838,591,734\n" +
            "390,-675,-793\n" +
            "-537,-823,-458\n" +
            "-485,-357,347\n" +
            "-345,-311,381\n" +
            "-661,-816,-575\n" +
            "-876,649,763\n" +
            "-618,-824,-621\n" +
            "553,345,-567\n" +
            "474,580,667\n" +
            "-447,-329,318\n" +
            "-584,868,-557\n" +
            "544,-627,-890\n" +
            "564,392,-477\n" +
            "455,729,728\n" +
            "-892,524,684\n" +
            "-689,845,-530\n" +
            "423,-701,434\n" +
            "7,-33,-71\n" +
            "630,319,-379\n" +
            "443,580,662\n" +
            "-789,900,-551\n" +
            "459,-707,401\n" +
            "\n" +
            "--- scanner 1 ---\n" +
            "686,422,578\n" +
            "605,423,415\n" +
            "515,917,-361\n" +
            "-336,658,858\n" +
            "95,138,22\n" +
            "-476,619,847\n" +
            "-340,-569,-846\n" +
            "567,-361,727\n" +
            "-460,603,-452\n" +
            "669,-402,600\n" +
            "729,430,532\n" +
            "-500,-761,534\n" +
            "-322,571,750\n" +
            "-466,-666,-811\n" +
            "-429,-592,574\n" +
            "-355,545,-477\n" +
            "703,-491,-529\n" +
            "-328,-685,520\n" +
            "413,935,-424\n" +
            "-391,539,-444\n" +
            "586,-435,557\n" +
            "-364,-763,-893\n" +
            "807,-499,-711\n" +
            "755,-354,-619\n" +
            "553,889,-390\n" +
            "\n" +
            "--- scanner 2 ---\n" +
            "649,640,665\n" +
            "682,-795,504\n" +
            "-784,533,-524\n" +
            "-644,584,-595\n" +
            "-588,-843,648\n" +
            "-30,6,44\n" +
            "-674,560,763\n" +
            "500,723,-460\n" +
            "609,671,-379\n" +
            "-555,-800,653\n" +
            "-675,-892,-343\n" +
            "697,-426,-610\n" +
            "578,704,681\n" +
            "493,664,-388\n" +
            "-671,-858,530\n" +
            "-667,343,800\n" +
            "571,-461,-707\n" +
            "-138,-166,112\n" +
            "-889,563,-600\n" +
            "646,-828,498\n" +
            "640,759,510\n" +
            "-630,509,768\n" +
            "-681,-892,-333\n" +
            "673,-379,-804\n" +
            "-742,-814,-386\n" +
            "577,-820,562\n" +
            "\n" +
            "--- scanner 3 ---\n" +
            "-589,542,597\n" +
            "605,-692,669\n" +
            "-500,565,-823\n" +
            "-660,373,557\n" +
            "-458,-679,-417\n" +
            "-488,449,543\n" +
            "-626,468,-788\n" +
            "338,-750,-386\n" +
            "528,-832,-391\n" +
            "562,-778,733\n" +
            "-938,-730,414\n" +
            "543,643,-506\n" +
            "-524,371,-870\n" +
            "407,773,750\n" +
            "-104,29,83\n" +
            "378,-903,-323\n" +
            "-778,-728,485\n" +
            "426,699,580\n" +
            "-438,-605,-362\n" +
            "-469,-447,-387\n" +
            "509,732,623\n" +
            "647,635,-688\n" +
            "-868,-804,481\n" +
            "614,-800,639\n" +
            "595,780,-596\n" +
            "\n" +
            "--- scanner 4 ---\n" +
            "727,592,562\n" +
            "-293,-554,779\n" +
            "441,611,-461\n" +
            "-714,465,-776\n" +
            "-743,427,-804\n" +
            "-660,-479,-426\n" +
            "832,-632,460\n" +
            "927,-485,-438\n" +
            "408,393,-506\n" +
            "466,436,-512\n" +
            "110,16,151\n" +
            "-258,-428,682\n" +
            "-393,719,612\n" +
            "-211,-452,876\n" +
            "808,-476,-593\n" +
            "-575,615,604\n" +
            "-485,667,467\n" +
            "-680,325,-822\n" +
            "-627,-443,-432\n" +
            "872,-547,-609\n" +
            "833,512,582\n" +
            "807,604,487\n" +
            "839,-516,451\n" +
            "891,-625,532\n" +
            "-652,-548,-490\n" +
            "30,-46,-14").split("\n")

    val xy = ("--- scanner 0 ---\n" +
            "0,2,0\n" +
            "4,1,0\n" +
            "3,3,0\n" +
            "\n" +
            "--- scanner 1 ---\n" +
            "-1,-1,0\n" +
            "-5,0,0\n" +
            "-2,1,0").split("\n")
    val start = System.nanoTime()
    day19part1(fileinput)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day19part1(input: List<String>) {
    val scanners = mutableListOf<Scanner>()
    var probes: MutableList<Probe>? = null
    for (i in 0 until input.size) {
        if (input[i].isEmpty() && probes != null) {
            scanners.add(Scanner(probes.toList()))
            println("adding scanner")
        } else if (input[i].startsWith("---")) {
            probes = mutableListOf()
            println("starting new list of probes for ${input[i]}")
        } else if (probes != null) {
            val (x, y, z) = input[i].split(",").map(String::toInt)
            probes.add(Probe(x, y, z))
        }
    }
    if (probes != null) {
        scanners.add(Scanner(probes))
    }

    val rotations = listOf(
        Rotation(1, 1, 1),
        Rotation(1, 1, -1),
        Rotation(1, -1, 1),
        Rotation(1, -1, -1),
        Rotation(-1, 1, 1),
        Rotation(-1, 1, -1),
        Rotation(-1, -1, 1),
        Rotation(-1, -1, -1),
    )
    val orientations = listOf(
        XyzOrientation(),
        XzyOrientation(),
        YxzOrientation(),
        YzxOrientation(),
        ZxyOrientation(),
        ZyxOrientation()
    )

    println("${scanners.size} scanners")

    // should hit with rotation 5, orientation x,y,z
//    println("this should match")
//    val r = rotations[5]
//    val o = XyzOrientation()
//    val updated = rotateScanner(scanners[1], r, o)
//    val dx = 68
//    val dy = -1246
//    val dz = -43
//    val count = countAligningProbes(dx, dy, dz, scanners[0], updated)
//    println("should give 12: $count")

    // brute force:
    // for each pair of scanners, assume a pair of probes match. Find the translation for the second scanner
    // and see if 12 other probes match, for each orientation.
    val overlaps = mutableSetOf<Overlap>()
    for (i in 0 until scanners.size) {
        val scanner = scanners[i]
        for (j in 0 until scanners.size) {
            if (i == j) continue

//            println("*** testing against scanner $i")
            var testScanner = scanners[j]

            var foundOverlap = false

            for (rotation in rotations) {
                if (foundOverlap) continue
//                println("Checking rotation $rotation")
                for (orientation in orientations) {
                    if (foundOverlap) continue
//                    println("Checking orientation ${orientation.javaClass}")
                    val rotated = rotateScanner(testScanner, rotation, orientation)
                    for (a in 0 until scanner.probes.size) {
                        for (b in 0 until testScanner.probes.size) {
                            if (foundOverlap) continue

                            var testProbes = Pair(scanner.probes[a], rotated.probes[b])
                            val dx = testProbes.first.x - testProbes.second.x
                            val dy = testProbes.first.y - testProbes.second.y
                            val dz = testProbes.first.z - testProbes.second.z

                            val count = countAligningProbes(dx, dy, dz, scanner, rotated)
                            if (count >= 12) {
                                //                            println("Counted matches $count from scanner $i for $dx, $dy, $dz based on probes $a and $b")
                                overlaps.add(Overlap(i, j, rotation, orientation, dx, dy, dz))
                                foundOverlap = true
                            }
                        }
                    }
                }
            }
        }
    }
    println(overlaps)

    println("\n\n all overlaps \n\n")
    for (p in overlaps.toList().sortedBy { it.a }) {
        println(p)
    }


    // build a list of all probe points relative to scanner 0
    // start with scanner 0's points
    val allProbes = mutableSetOf<Probe>()
    allProbes.addAll(scanners[0].probes)

    // for each scanner, find a path of transformations back to scanner 0
    val overlapsToZero = overlaps.filter { it.a == 0 }
    for (o in overlapsToZero) {
        val transformed = getTransformedScanners(o, setOf(0), scanners, overlaps)
        for (t in transformed) {
            allProbes.addAll(t.probes)
        }
    }

    println("\n\nAll Probes:")
    for (p in allProbes.sortedBy { it.x }) {
        println(p)
    }

    println("ended up with ${allProbes.size}")

    println("********* part 2 *************")
    val origins = mutableListOf<Scanner>()
    for (i in scanners.indices) {
        val origin = Probe(0, 0, 0)
        origins.add(Scanner(listOf(origin), i))
    }
    val transformed = mutableListOf<Scanner>()
    for (o in overlapsToZero) {
        transformed.addAll(getTransformedScanners(o, setOf(0), origins, overlaps))
    }
//    val originList = transformed.toList().sortedBy { it.originalIndex }
//    println("${originList.size} vs ${scanners.size}\n")

//    val distances = mutableListOf<Pair<Pair<Int, Int>, Int>>()
    var max = 0
    for (i in transformed.indices) {
        println("Checking against origin ${transformed[i]}")
        for (j in transformed.indices) {
//            if (i == j) continue
            val o1 = transformed[i].probes.first()
            val o2 = transformed[j].probes.first()
            val dist = abs(o2.x - o1.x) + abs(o2.y - o1.y) + abs(o2.z - o1.z)
//            distances.add((i to j) to dist)
            if (dist > max) {
                println("Found new max distance $dist between ${transformed[i].originalIndex} and ${transformed[j].originalIndex}")
                max = dist
            }
        }
    }

//    println(distances.sortedBy { it.second })

    // 7326 too low
    println(max)

}

fun getTransformedScanners(target: Overlap, visited: Set<Int>, scanners: List<Scanner>, overlaps: Set<Overlap>) : Set<Scanner> {
    val transformed = mutableSetOf<Scanner>()
    val s = applyOverlap(scanners[target.b], target)
    val v = visited.toMutableSet()
    v.add(target.a)
    transformed.add(s)
    val overlapsToTarget = overlaps.filter { it.a == target.b && !v.contains(target.b) }
    for (o in overlapsToTarget) {
        val n = getTransformedScanners(o, v, scanners, overlaps)
        for (ns in n) {
            transformed.add(applyOverlap(ns, target))
        }
    }
//    println("returning scanners for overlap from ${target.b} to ${target.a}")
    return transformed
}

fun applyOverlap(scanner: Scanner, overlap: Overlap) : Scanner {
    val r = rotateScanner(scanner, overlap.rotation, overlap.orientation)
    val probes = mutableListOf<Probe>()
    for (p in r.probes) {
        probes.add(Probe(p.x + overlap.dx, p.y + overlap.dy, p.z + overlap.dz))
    }
    return Scanner(probes, scanner.originalIndex)
}

fun countAligningProbes(dx: Int, dy: Int, dz: Int, scanner: Scanner, testScanner: Scanner) : Int {
    val matches = mutableListOf<Pair<Int, Int>>()
    for (i in scanner.probes.indices) {
        for (j in testScanner.probes.indices) {
            if (scanner.probes[i].x == (testScanner.probes[j].x + dx) &&
                scanner.probes[i].y == (testScanner.probes[j].y + dy) &&
                scanner.probes[i].z == (testScanner.probes[j].z + dz)) {
//                println("Matched probe $i to $j (${scanner.probes[i]} to ${testScanner.probes[j]}) $dx, $dy, $dz")
                matches.add(i to j)
            }
        }
    }
    return matches.size
}

fun rotateScanner(scanner: Scanner, rotation: Rotation, orientation: Orientation) : Scanner {
    val probes = mutableListOf<Probe>()
    for (probe in scanner.probes) {
        val oriented = orientation.probe(probe)
        probes.add(Probe(oriented.x * rotation.x, oriented.y * rotation.y, oriented.z * rotation.z))
    }
    return Scanner(probes, scanner.originalIndex)
}


data class Rotation(val x: Int, val y: Int, val z: Int)
data class Scanner(val probes: List<Probe>, var originalIndex: Int = -1)
//data class Transform(val orientation: Orientation, val rotation: Rotation, val dx: Int, val dy: Int, val dz: Int) {
//    fun apply(scanner: Scanner) : Scanner {
//        val probes = mutableListOf<Probe>()
//        for (probe in scanner.probes) {
//            val oriented = orientation.probe(probe)
//            probes.add(Probe((oriented.x * rotation.x) + dx,
//                (oriented.y * rotation.y) + dy,
//                (oriented.z * rotation.z) + dz))
//        }
//        return Scanner(probes, scanner.originalIndex)
//    }
//}

data class Probe(val x: Int, val y: Int, val z: Int)
data class Overlap(val a: Int, val b: Int, val rotation: Rotation, val orientation: Orientation, val dx: Int, val dy: Int, val dz: Int)
abstract class Orientation {
    abstract fun probe(original: Probe): Probe
}
class XyzOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.x, original.y, original.z)
    }
}
class XzyOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.x, original.z, original.y)
    }
}
class YxzOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.y, original.x, original.z)
    }
}
class YzxOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.y, original.z, original.x)
    }
}
class ZxyOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.z, original.x, original.y)
    }
}
class ZyxOrientation(): Orientation() {
    override fun probe(original: Probe): Probe {
        return Probe(original.z, original.y, original.x)
    }
}
