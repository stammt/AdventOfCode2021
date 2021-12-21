import java.io.File

fun main(args: Array<String>) {
    val input = File("/Users/stammt/Documents/2021aoc/day12input.txt").readLines()

//    val input = ("dc-end\n" +
//            "HN-start\n" +
//            "start-kj\n" +
//            "dc-start\n" +
//            "dc-HN\n" +
//            "LN-dc\n" +
//            "HN-end\n" +
//            "kj-sa\n" +
//            "kj-HN\n" +
//            "kj-dc").split("\n")

    val start = System.nanoTime()
    day12part1(input)
    val end = System.nanoTime()
    val time = (end - start) / 1_000_000
    println("took $time ms")
}

fun day12part1(input: List<String>) {
//    val graph = input.map {line ->
//        line.split("-").let{it.first() to it.last()}
//    }

    val nodes = mutableMapOf<String, Node>()
    for (edge in input) {
        val one = edge.split("-")[0]
        val two = edge.split("-")[1]

        var n1 = nodes[one]
        if (n1 == null) {
            n1 = Node(one, mutableListOf())
            nodes[one] = n1
        }
        var n2 = nodes[two]
        if (n2 == null) {
            n2 = Node(two, mutableListOf())
            nodes[two] = n2
        }

        n1.neighbors.add(n2)
        n2.neighbors.add(n1)
    }

    val start = nodes["start"]
    val paths = mutableSetOf<String>()
    search(start!!, mutableSetOf(start), null, "start", paths)

    // 91533
    println("${paths.size} paths")
}

fun search(node: Node, visited: MutableSet<Node>, canRevisitSmallNode: String?, path: String, paths: MutableSet<String>) {
    node.neighbors.forEach { next ->
//        println("path $path checking node ${next.label}")
        val nextPath = path + "-${next.label}"
        if (next.label == "end") {
//            println("Ending path $nextPath")
            paths.add(nextPath)
        } else if (next.isLarge() || !visited.contains(next)) {
            // don't add to a global visited set, just track for this path
            val pathVisited = mutableSetOf<Node>()
            pathVisited.addAll(visited)

            // For part 2:
            // if this is a small node and we haven't yet picked a small node to revisit,
            // search the next node twice: Once with this node picked but not visited, and once with this node
            // marked visited.
            if (!next.isStartOrEnd() && !next.isLarge() && !visited.contains(next) && canRevisitSmallNode == null) {
                search(next, pathVisited, next.label, nextPath, paths)

                pathVisited.add(next)
                search(next, pathVisited, null,  nextPath, paths)
            } else {
                pathVisited.add(next)
                search(next, pathVisited, canRevisitSmallNode,  nextPath, paths)
            }
        }
    }
}

class Node(val label: String, val neighbors: MutableList<Node>) {
    fun isLarge() : Boolean {
        return label.all{it.isUpperCase()}
    }
    fun isStartOrEnd() : Boolean {
        return label == "start" || label == "end"
    }
}