/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

import benchmarks.dsl.Scenario
import benchmarks.dsl.Step
import benchmarks.dsl.Tasks
import benchmarks.evaluation.AbstractBenchmarksProgressListener
import benchmarks.evaluation.gradle.GradlePhasesMetrics
import benchmarks.evaluation.results.*
import benchmarks.utils.Either
import benchmarks.utils.TimeInterval
import benchmarks.utils.max
import benchmarks.utils.min
import kotlin.math.max
import kotlin.math.min

class SimpleLoggingBenchmarkListener : AbstractBenchmarksProgressListener() {
    private class AggregatedMetric private constructor(
        val minTime: TimeInterval,
        val maxTime: TimeInterval,
        val minPercentage: Double,
        val maxPercentage: Double
    ) {
        constructor(time: TimeInterval, percentage: Double) : this(time, time, percentage, percentage)

        fun plus(other: AggregatedMetric): AggregatedMetric = AggregatedMetric(
            minTime = min(minTime, other.minTime),
            maxTime = max(maxTime, other.maxTime),
            minPercentage = min(minPercentage, other.minPercentage),
            maxPercentage = max(maxPercentage, other.maxPercentage)
        )
    }

    private var aggregatedMetrics: MetricsContainer<AggregatedMetric>? = null
    private var stepNum = 0

    override fun scenarioStarted(scenario: Scenario) {
        stepNum = 0
        p("Scenario '${scenario.name}'")
    }

    override fun scenarioFinished(scenario: Scenario, result: Either<ScenarioResult>) {
        p("==============")
    }

    override fun stepStarted(step: Step) {
        stepNum++
        p("Step #$stepNum")
    }

    override fun taskExecutionStarted(tasks: Array<Tasks>) {
        withIndent {
            p("Executing tasks: ${tasks.joinToString(", ") { "'" + it.path + "'" }}")
        }
    }

    override fun cleanupStarted() {
        p("Cleaning up after last scenario")
    }

    override fun cleanupFinished() {
        p("Cleanup finished")
    }

    override fun stepFinished(step: Step, result: Either<StepResult>) {
        withIndent {
            when (result) {
                is Either.Failure -> {
                    p("Step failed: ${result.reason}")
                }
                is Either.Success<StepResult> -> {
                    if (!step.isMeasured) {
                        p("Step is not measured!")
                        return
                    }

                    val stepResult = result.value
                    printStepResult(stepResult)
                }
            }
        }
    }

    override fun allFinished() {
        p()
        p()
        p("All runs:")
        aggregatedMetrics!!.walkTimeMetrics(
            fn = { metric, value ->
                value.apply {
                    p(
                        "$metric: ${minTime.asMs} - ${maxTime.asMs} ms, ${minPercentage.shortStr}% - ${maxPercentage.shortStr}%"
                    )
                }

            },
            onEnter = { indentLevel++ },
            onExit = { indentLevel-- }
        )
    }

    private val Double.shortStr
        get() = String.format("%.1f", this)

    private fun printStepResult(result: StepResult) {
        val timeMetrics = result.buildResult.timeMetrics
        val wholeBuild = timeMetrics.getMetric(GradlePhasesMetrics.GRADLE_BUILD.name) as? ValueMetric<TimeInterval>
        val wholeBuildMs = wholeBuild!!.value.asMs.toDouble()

        timeMetrics.walkTimeMetrics(
            fn = { metric, time ->
                val percentage = time.asMs.toDouble() / wholeBuildMs * 100
                if (time.asNs > 0) {
                    p("$metric: ${time.asMs} ms (${percentage.shortStr}%)")
                }
            },
            onEnter = { indentLevel++ },
            onExit = { indentLevel-- }
        )

        val current = timeMetrics.map { time -> AggregatedMetric(time, time.asMs.toDouble() / wholeBuildMs * 100) }
        aggregatedMetrics = aggregatedMetrics?.let { prev ->
            prev.plus(current) { v1, v2 -> v1.plus(v2) }
        } ?: current
    }

    private val indentStr = "    "
    private var indentLevel = 0

    private fun p(string: String = "") {
        repeat(indentLevel) { print(indentStr) }
        println(string)
    }

    private inline fun withIndent(fn: () -> Unit) {
        indentLevel++
        try {
            fn()
        } finally {
            indentLevel--
        }
    }
}