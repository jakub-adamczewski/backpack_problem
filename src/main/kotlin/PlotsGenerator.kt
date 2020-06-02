import scientifik.plotly.Plotly
import scientifik.plotly.makeFile
import scientifik.plotly.trace
import kotlin.system.measureTimeMillis

//list of sizes of lists of containers or capacities
private val listForPlots = listOf(
    1,
    10,
    20,
    30,
    40,
    50,
    60,
    70,
    80,
    90,
    100,
    110,
    120,
    130,
    140,
    150
)

private const val multiplyFactor = 90


fun generatePlots() {
    val constCapacity = listForPlots.last().timesMultiplyFactor()
    val constCapacityShips = listForPlots.map { Ship(constCapacity, true, it.timesMultiplyFactor()) }
    generateTimesAndQualityFactorPlots(listForPlots.map { it.timesMultiplyFactor() }, constCapacityShips, constCapacity, true)

    val constContainersNumber = listForPlots.last().timesMultiplyFactor()
    val constContainersNumberShips = listForPlots.map { Ship(it.timesMultiplyFactor(), true, constContainersNumber) }
    generateTimesAndQualityFactorPlots(listForPlots.map { it.timesMultiplyFactor() }, constContainersNumberShips, constContainersNumber, false)

}

private fun generateTimesAndQualityFactorPlots(x: List<Int>, shipInstances: List<Ship>, const: Int, constCapacity: Boolean) {

    val greedyAlgorithmTimes = mutableListOf<Long>()
    val optimalAlgorithmTimes = mutableListOf<Long>()
    val greedyAlgorithmResults = mutableListOf<List<Container>>()
    val optimalAlgorithmResults = mutableListOf<List<Container>>()


    shipInstances.forEach { shipInstance ->
        greedyAlgorithmTimes.add(measureTimeMillis { greedyAlgorithmResults.add(shipInstance.getPackedContainersWithGreedyAlgorithm()) })
        optimalAlgorithmTimes.add(measureTimeMillis { optimalAlgorithmResults.add(shipInstance.getPackedContainersWithOptimalAlgorithm()) })
    }

    val qualityFactorsList = mutableListOf<Float>()
    for (i in 0 until greedyAlgorithmTimes.size) {
        qualityFactorsList.add(
            (optimalAlgorithmResults[i].valuesSum() - greedyAlgorithmResults[i].valuesSum()) / optimalAlgorithmResults[i].valuesSum().toFloat()
        )
    }

    val timesPlot = Plotly.plot2D {
        trace(x, greedyAlgorithmTimes) {
            name = "greedy algorithm"
        }
        trace(x, optimalAlgorithmTimes) {
            name = "optimal algorithm"
        }

        layout {
            val titlePart = if(constCapacity) "capacity of $const" else "containers number of $const"
            title = "Comparison of greedy and optimal algorithms times for constant $titlePart"
            xaxis {
                title = if (constCapacity )"containers numbers" else "capacities"
            }
            yaxis {
                title = "times of algorithms processing"
            }
        }
    }
    timesPlot.makeFile()



    val qualityFactorPlot = Plotly.plot2D {
        trace(x, qualityFactorsList) {
            name = "quality factor"
        }

        layout {
            title = "Quality factor for formula: (D_opt - D_gr) / D_opt) for constant ${
            if (constCapacity) "capacity" else "containers number"
            }"
            xaxis {
                title = if(constCapacity) "containers numbers" else "capacities"
            }
            yaxis {
                title = "quality factors"
            }
        }
    }
    qualityFactorPlot.makeFile()
}

fun List<Container>.valuesSum(): Int {
    return this.sumBy { it.value }
}

fun Int.timesMultiplyFactor(): Int{
    return this * multiplyFactor
}