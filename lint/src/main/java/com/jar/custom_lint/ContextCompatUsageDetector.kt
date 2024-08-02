/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jar.custom_lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement

/**
 * A detector that checks for ContextCompat.getDrawable() usage in the project
 */
class ContextCompatUsageDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler =
        object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                println("SEEHERE caexpresion ${node.methodName}")
                val name = node.methodName ?: return

                when (name) {
                    "getDrawable" -> {
                        println("SEEHERE get Drawable ${isContextCompatCall(node)}}")
                        if (isContextCompatCall(node)) {
                            context.report(
                                ISSUE,
                                node,
                                context.getLocation(node),
                                "Use AppCompatResources.getDrawable instead of ContextCompat.getDrawable"
                            )
                        }
                    }
                }
            }
            private fun isContextCompatCall(node: UCallExpression): Boolean {
                val receiver = node.receiver?.asSourceString()
                return receiver?.contains("ContextCompat") == true
            }

        }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "ContextCompatUsage",
            briefDescription = "ContextCompatUsage",
            explanation = "Avoid using `ContextCompat.getDrawable` as it can cause crashes on Android 6.0 and below due to issues with vector drawable support enabled in our project. Consider using alternative methods to ensure compatibility and stability.",
            category = Category.CUSTOM_LINT_CHECKS,
            priority = 7,
            severity = Severity.ERROR,
            implementation = Implementation(
                ContextCompatUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )
    }
}
