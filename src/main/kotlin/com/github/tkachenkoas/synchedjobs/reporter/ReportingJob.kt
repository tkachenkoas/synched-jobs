package com.github.tkachenkoas.synchedjobs.reporter

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.statistics.HistogramDataset
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.text.DecimalFormat


@Component
class ReportingJob(
    private val executedOperationRepository: ExecutedOperationRepository
) {

    private var iteration: Int = 1

    @Scheduled(fixedRate = 500)
    fun doTheDirtyJob() {
        val results = executedOperationRepository.findAll()
        if (results.isEmpty()) {
            println("No recorded results yet")
            return
        }
        println("Total recorded results: ${results.size}")

        val doubleArray = results.map { it.elapsed.toDouble() }.toDoubleArray()

        val dataset = HistogramDataset()
        dataset.addSeries(
            "runs", doubleArray,
            1000,
            10_000.toDouble(), 60_000.toDouble()
        )

        val histogram = ChartFactory.createHistogram(
            "Operations executed over time",
            "Count", "time",
            dataset
        )

        val formattedIteration = "%02d".format(iteration)
        ChartUtils.saveChartAsPNG(File("./histograms/histogram_${formattedIteration}.png"),
            histogram, 1350, 400)
        iteration++
    }

}