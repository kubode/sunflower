/*
 * Copyright 2020 Google LLC
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

package com.google.samples.apps.sunflower

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.samples.apps.sunflower.di.DatabaseModule
import com.google.samples.apps.sunflower.di.NetworkModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

class GardenActivityTest {

    private val koinTestRule = KoinTestRule(listOf(defaultModule, DatabaseModule().module, NetworkModule().module))
    private val composeTestRule = createAndroidComposeRule<GardenActivity>()

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(koinTestRule)
        .around(composeTestRule)

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test fun clickAddPlant_OpensPlantList() {
        // Given that no Plants are added to the user's garden

        // When the "Add Plant" button is clicked
        with(composeTestRule.onNodeWithText("Add plant")) {
            assertExists()
            assertIsDisplayed()
            performClick()
        }

        composeTestRule.waitForIdle()

        // Then the pager should change to the Plant List page
        with(composeTestRule.onNodeWithTag("plant_list")) {
            assertExists()
        }
    }
}