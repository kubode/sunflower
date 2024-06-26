/*
 * Copyright 2018 Google LLC
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

package com.google.samples.apps.sunflower.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.samples.apps.sunflower.KoinTestRule
import com.google.samples.apps.sunflower.MainCoroutineRule
import com.google.samples.apps.sunflower.data.AppDatabase
import com.google.samples.apps.sunflower.data.GardenPlantingRepository
import com.google.samples.apps.sunflower.data.PlantRepository
import com.google.samples.apps.sunflower.di.DatabaseModule
import com.google.samples.apps.sunflower.di.NetworkModule
import com.google.samples.apps.sunflower.runBlockingTest
import com.google.samples.apps.sunflower.utilities.testPlant
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module

class PlantDetailViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel
    private val koinTestRule = KoinTestRule(listOf(defaultModule, NetworkModule().module, DatabaseModule().module))
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule: RuleChain = RuleChain
            .outerRule(koinTestRule)
            .around(instantTaskExecutorRule)
            .around(coroutineRule)

    lateinit var plantRepository: PlantRepository

    lateinit var gardenPlantingRepository: GardenPlantingRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        plantRepository = koinTestRule.koin.get()
        gardenPlantingRepository = koinTestRule.koin.get()

        val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
            set("plantId", testPlant.plantId)
        }
        viewModel = PlantDetailViewModel(savedStateHandle, plantRepository, gardenPlantingRepository)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest {
        assertFalse(viewModel.isPlanted.first())
    }
}