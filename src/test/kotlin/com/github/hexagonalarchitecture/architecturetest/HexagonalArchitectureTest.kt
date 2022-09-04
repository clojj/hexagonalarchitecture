package com.github.hexagonalarchitecture.architecturetest

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.jupiter.api.Test

class HexagonalArchitectureTest {

    private val importedClasses = ClassFileImporter()
        .withImportOption(DO_NOT_INCLUDE_TESTS)
        .importPackages("com.github.hexagonalarchitecture")

    @Test
    fun `domain should depend on domain only`() {
        classes()
            .that()
            .resideInAPackage("..domain..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..domain..",
                "java..",
                "org.jetbrains..",
                "kotlin..",
                "arrow.."
            )
            .check(importedClasses)
    }

    @Test
    fun `application services should not depend on infrastructure`() {
        classes()
            .that()
            .resideInAPackage("..application..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..domain..",
                "..application..",
                "java..",
                "org.jetbrains..",
                "kotlin..",
                "arrow.."
            )
            .check(importedClasses)
    }

    @Test
    fun `outbound should not depend on inbound nor application services`() {
        classes()
            .that()
            .resideInAPackage("..outbound..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..outbound..",
                "..domain..",
                "java..",
                "org.springframework..",
                "org.jetbrains..",
                "kotlin.."
            )
            .check(importedClasses)
    }
}
