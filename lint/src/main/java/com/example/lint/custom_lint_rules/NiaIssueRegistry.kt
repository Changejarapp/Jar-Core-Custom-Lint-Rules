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

package com.example.lint.custom_lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.example.lint_custom_rule.lint_check.custom_lint_rules.AnalyticsApiPostEventInFragmentDetector


class NiaIssueRegistry : IssueRegistry() {

    override val issues = listOf(
        ContextCompatUsageDetector.ISSUE,
        AnalyticsApiPostEventInFragmentDetector.ISSUE
    )

    override val api: Int = CURRENT_API

    override val minApi: Int = 12

    override val vendor: Vendor = Vendor(
        vendorName = "Change Jar",
        feedbackUrl = "https://github.com/Changejarapp/jar-android/issues",
        contact = "https://github.com/Changejarapp/jar-android/issues",
    )
}