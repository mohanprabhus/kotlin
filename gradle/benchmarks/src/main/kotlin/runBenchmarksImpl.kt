/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

import benchmarks.dsl.Suite
import benchmarks.dsl.Tasks
import benchmarks.evaluation.gradle.GradleBenchmarkEvaluator
import benchmarks.evaluation.results.CompactResultListener
import java.io.File

internal val DEFAULT_TASKS = arrayOf(Tasks.DIST, Tasks.COMPILER_TEST_CLASSES, Tasks.IDEA_TEST_CLASSES)

internal fun mainImpl(benchmarks: Suite) {
    val eval = GradleBenchmarkEvaluator().apply {
        addListener(SimpleLoggingBenchmarkListener())
        val dir = File("build/benchmark-results").apply { mkdirs() }
        addListener(CompactResultListener(File.createTempFile("benchmark.result.bin", null, dir)))
    }
    eval.runBenchmarks(benchmarks)
}