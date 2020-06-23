/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.codeInsight.hints;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/codeInsight/hints/types")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinReferenceTypeHintsProviderTestGenerated extends AbstractKotlinReferenceTypeHintsProviderTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInTypes() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("idea/testData/codeInsight/hints/types"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @TestMetadata("ConstInitializerType.kt")
    public void testConstInitializerType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/ConstInitializerType.kt");
    }

    @TestMetadata("ConstructorWithExplicitTypeParametersType.kt")
    public void testConstructorWithExplicitTypeParametersType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/ConstructorWithExplicitTypeParametersType.kt");
    }

    @TestMetadata("ConstructorWithoutTypeParametersType.kt")
    public void testConstructorWithoutTypeParametersType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/ConstructorWithoutTypeParametersType.kt");
    }

    @TestMetadata("DestructingType.kt")
    public void testDestructingType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/DestructingType.kt");
    }

    @TestMetadata("LocalVariable.kt")
    public void testLocalVariable() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/LocalVariable.kt");
    }

    @TestMetadata("PropertyType.kt")
    public void testPropertyType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/PropertyType.kt");
    }

    @TestMetadata("QualifiedReferences.kt")
    public void testQualifiedReferences() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/QualifiedReferences.kt");
    }

    @TestMetadata("UnaryConstInitializerType.kt")
    public void testUnaryConstInitializerType() throws Exception {
        runTest("idea/testData/codeInsight/hints/types/UnaryConstInitializerType.kt");
    }
}
