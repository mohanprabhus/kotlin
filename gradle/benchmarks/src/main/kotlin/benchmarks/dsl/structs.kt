/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package benchmarks.dsl

import java.io.File

class Suite(
    val scenarios: Array<Scenario>,
    val defaultTasks: Array<Tasks>
) {
    fun copy(scenarios: Array<Scenario> = this.scenarios, defaultTasks: Array<Tasks> = this.defaultTasks) =
        Suite(scenarios, defaultTasks)
}

class Scenario(
    val expectedSlowBuildReason: String? = null,
    val name: String,
    val steps: Array<Step>,
    val repeat: UByte
)

sealed class Step {
    abstract val isMeasured: Boolean
    abstract val isExpectedToFail: Boolean
    abstract val tasks: Array<Tasks>?

    class SimpleStep(
        override val isMeasured: Boolean,
        override val isExpectedToFail: Boolean,
        override val tasks: Array<Tasks>?,
        val fileChanges: Array<FileChange>
    ) : Step()

    class RevertLastStep(
        override val isMeasured: Boolean,
        override val isExpectedToFail: Boolean,
        override val tasks: Array<Tasks>?
    ) : Step()
}

data class FileChange(val targetFile: TargetFile, val typeOfChange: TypeOfChange)

enum class TargetFile(path: String) {
    CORE_UTIL_STRINGS("core/util.runtime/src/org/jetbrains/kotlin/utils/strings.kt"),
    CORE_UTIL_CORE_LIB("core/util.runtime/src/org/jetbrains/kotlin/utils/coreLib.kt");

    val ioFile = File(path)
}

enum class TypeOfChange {
    ADD_PRIVATE_FUNCTION,
    ADD_PUBLIC_FUNCTION,
    ADD_PRIVATE_CLASS,
    ADD_PUBLIC_CLASS,
    CHANGE_INLINE_FUNCTION,
    INTRODUCE_COMPILE_ERROR,
    FIX_COMPILE_ERROR;

    val fileExtension: String
        get() = ".${name.constantCaseToCamelCase()}.benchmark"
}

@Suppress("unused")
enum class Tasks(private val customTask: String? = null) {
    CLEAN,
    CORE_UTIL_CLASSES(":core:util.runtime:classes"),
    DIST,
    COMPILER_TEST_CLASSES(":compiler:testClasses"),
    IDEA_TEST_CLASSES(":idea:testClasses"),
    IDEA_PLUGIN,
    INSTALL,
    CLASSES,
    TEST_CLASSES;

    val path: String
        get() = customTask ?: name.constantCaseToCamelCase()
}