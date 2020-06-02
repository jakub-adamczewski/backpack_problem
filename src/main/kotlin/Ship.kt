import kotlin.random.Random

class Ship(
    private val capacity: Int, createRandomItems: Boolean = false, private var allContainersNumber: Int = 0
) {

    private val allContainersList = mutableListOf<Container>()
    private var newestContainerId = 0

    init {
        if (createRandomItems) createRandomContainers()
    }

    private fun containersListSortedByProfit() =
        allContainersList.sortedBy { container -> container.profitFactor }.reversed()

    private fun createRandomContainers() {
        for (i in 0 until allContainersNumber) {
            allContainersList.add(
                Container(
                    newestContainerId++,
                    Random.nextInt(1, capacity * 5),
                    Random.nextInt(1, capacity / 2)
                )
            )
        }
    }

    fun addContainer(value: Int, weight: Int) {
        allContainersList.add(
            Container(newestContainerId++, value, weight)
        )
    }

    fun getContainer(id: Int): Container? {
        return allContainersList.find { it.id == id }
    }

    fun printContainersList(profitSorted: Boolean = false) {
        println("All containers list: ")

        val listToPrint = if (profitSorted) containersListSortedByProfit() else allContainersList
        for (container in listToPrint) {
            println(container.toString())
        }
        println()
    }

    fun getPackedContainersWithGreedyAlgorithm(): List<Container> {
        var localCapacity = capacity
        val packedContainers = mutableListOf<Container>()
        for (container in containersListSortedByProfit()) {
            if (container.weight <= localCapacity) {
                packedContainers.add(container)
                localCapacity -= container.weight
                if (localCapacity == 0) break
            }
        }
        return packedContainers.toList()
    }

    fun getPackedContainersWithOptimalAlgorithm(printTable: Boolean = false): List<Container> {
        val table = mutableListOf<MutableList<Int>>()
        val containersCount = allContainersList.size
        for (i in 0 until containersCount) {
            table.add(mutableListOf())
        }

        for (i in 0 until containersCount) {
            val currentContainer = allContainersList[i]
            for (j in 0..capacity) {
                table[i].add(
                    if (currentContainer.weight > j) {
                        if (i == 0) 0
                        else table[i - 1][j]
                    } else {
                        when {
                            i == 0 -> currentContainer.value
                            (currentContainer.value + table[i - 1][j - currentContainer.weight]) > table[i - 1][j] -> currentContainer.value + table[i - 1][j - currentContainer.weight]
                            else -> table[i - 1][j]
                        }
                    }
                )
            }
        }

        if (printTable) {
            println("Optima algorithm table:")
            for (line in table) {
                println(line)
            }
            println()
        }

        val packedContainers = mutableListOf<Container>()
        var currentI = table.size - 1
        var currentJ = table.last().size - 1

        while (currentJ > 0) {
            when {
                currentI == 0 -> {
                    if(table[currentI][currentJ] > 0) packedContainers.add(allContainersList[currentI])
                    currentJ = 0
                }
                table[currentI][currentJ] == table[currentI][currentJ - 1] -> currentJ -= 1
                table[currentI][currentJ] == table[currentI - 1][currentJ] -> currentI -= 1
                else -> {
                    val currentContainer = allContainersList[currentI]
                    packedContainers.add(currentContainer)
                    currentI -= 1
                    currentJ -= currentContainer.weight
                }
            }
        }
        return packedContainers
    }

}

data class Container(val id: Int, val value: Int, val weight: Int, val profitFactor: Float = value / weight.toFloat())

