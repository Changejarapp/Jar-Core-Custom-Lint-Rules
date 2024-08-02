/*
 * Copyright 2024 The Android Open Source Project
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
import org.jetbrains.uast.*


class AnalyticsApiPostEventInFragmentDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UCallExpression::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler =
        object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                val name = node.methodName ?: return

                when (name) {
                    "postEvent" -> {
                        if (isPostEventCallOnAnalyticsApi(node) && isInFragment(node)) {
                            context.report(
                                ISSUE ,
                                node,
                                context.getLocation(node),
                                "AnalyticsApi.postEvent should be called in ViewModel, not in Fragment"
                            )
                        }
                    }
                }
            }

            private fun isPostEventCallOnAnalyticsApi(node: UCallExpression): Boolean {
                val receiver = node.receiver as? UReferenceExpression ?: return false
                val resolvedType = receiver.getExpressionType() ?: return false
                return resolvedType.canonicalText.contains("AnalyticsApi")
            }

            private fun isInFragment(node: UCallExpression): Boolean {
                val containingClass = node.getContainingUClass() ?: return false
                return containingClass.supers.any {
                    println("BaseFragment = ${it.qualifiedName}")
                    it.qualifiedName == "com.jar.app.base.ui.fragment.BaseFragment" || it.qualifiedName == "com.jar.app.base.ui.fragment.BaseDialogFragment"
                }
            }
            override fun visitClassLiteralExpression(node: UClassLiteralExpression) {

            }

        }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
            id = "AnalyticsApiPostEventInFragmentDetector",
            briefDescription = "AnalyticsApiPostEventInFragmentDetector",
            explanation = "AnalyticsApi.postEvent should be called in ViewModel, not in Fragment",
            category = Category.CUSTOM_LINT_CHECKS,
            priority = 7,
            severity = Severity.ERROR,
            implementation = Implementation(
                AnalyticsApiPostEventInFragmentDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
            androidSpecific = true
        )
    }
}
