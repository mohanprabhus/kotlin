/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package benchmarks.evaluation

import benchmarks.dsl.FileChange
import benchmarks.dsl.Step
import benchmarks.utils.stackTraceString
import java.io.File
import java.util.ArrayDeque

class ChangesApplier {
    private val appliedChanges = ArrayDeque<ApplicableChanges>()

    fun applyStepChanges(step: Step): Boolean {
        return when (step) {
            is Step.SimpleStep -> {
                val applicable = ApplicableChanges(changes = step.fileChanges)
                if (applicable.apply()) {
                    appliedChanges.addFirst(applicable)
                    true
                } else {
                    applicable.revert()
                    false
                }
            }
            is Step.RevertLastStep -> {
                appliedChanges.pollFirst().revert()
            }
        }
    }

    fun revertAppliedChanges() {
        while (appliedChanges.isNotEmpty()) {
            try {
                appliedChanges.pollFirst().revert()
            } catch (e: Exception) {
                System.err.println("Failed to revert changes: ${e.stackTraceString()}")
            }
        }
    }

    val hasAppliedChanges: Boolean
        get() = appliedChanges.isNotEmpty()
}

private class ApplicableChanges(private val changes: Array<FileChange>) {
    private val prevVersions = arrayListOf<Pair<File, ByteArray?>>()

    fun apply(): Boolean {
        for (change in changes) {
            val file = change.targetFile.ioFile
            val prevContent = if (file.exists()) file.readBytes() else null

            try {
                val newContent = File(file.path + change.typeOfChange.fileExtension).readBytes()
                file.writeBytes(newContent)
            } catch (e: Exception) {
                System.err.println("Could not apply change ${change}: ${e.stackTraceString()}")
                return false
            }

            prevVersions.add(file to prevContent)
        }
        return true
    }

    fun revert(): Boolean {
        val failedToRevert = prevVersions.mapNotNull { (file, prevContent) ->
            try {
                if (prevContent == null) file.delete() else file.writeBytes(prevContent)
                null
            } catch (e: Exception) {
                System.err.println("Could not revert change to ${file}: ${e.stackTraceString()}")
                file to prevContent
            }
        }
        prevVersions.clear()
        prevVersions.addAll(failedToRevert)
        return failedToRevert.isEmpty()
    }
}