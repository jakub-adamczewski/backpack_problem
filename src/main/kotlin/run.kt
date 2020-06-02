import kotlin.system.measureTimeMillis

fun main() {
    val allTime = measureTimeMillis { generatePlots() }
    println("Time of plots generation: ${allTime / 1000F} sec.")
}


fun test() {
    val problem = Ship(5)

    problem.addContainer(60, 5)
    problem.addContainer(50, 3)
    problem.addContainer(70, 4)
    problem.addContainer(30, 2)

    problem.printContainersList(true)

    val greedyContainersList = problem.getPackedContainersWithGreedyAlgorithm()
    greedyContainersList.print2DListByLines("Greedy containers:")

    val optimalContainersList = problem.getPackedContainersWithOptimalAlgorithm(true)
    optimalContainersList.print2DListByLines("Optimal containers:")
}

fun List<Container>.print2DListByLines(title: String = "Containers:") {
    println(title)
    for (line in this) {
        println(line)
    }
    println()
}