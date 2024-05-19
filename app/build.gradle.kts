import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.ksp)
  alias(libs.plugins.kotlinx.serialization)
}

kotlin {
  androidTarget()

  sourceSets {
    commonMain {
      dependencies {
      }
    }
    androidMain {
      dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.navigation.compose)
        implementation(libs.androidx.paging.compose)
        implementation(libs.androidx.room.ktx)
        implementation(libs.androidx.work.runtime.ktx)
        implementation(libs.material)
        implementation(libs.okhttp3.logging.interceptor)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.androidx.profileinstaller)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.koin.android)
        implementation(libs.koin.androidx.compose)
        implementation(libs.koin.annotations)

        // Compose
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.constraintlayout.compose)
        implementation(libs.androidx.compose.runtime)
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.compose.foundation.layout)
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.ui.viewbinding)
        implementation(libs.androidx.compose.ui.tooling.preview)
        implementation(libs.androidx.compose.runtime.livedata)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        implementation(libs.accompanist.systemuicontroller)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor)
      }
    }
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    invokeWhenCreated("androidDebug") {
      dependencies {
        implementation(libs.androidx.compose.ui.tooling)
        implementation(libs.androidx.monitor)
      }
    }
    getByName("androidUnitTest") {
      dependencies {
        implementation(libs.junit)
      }
    }
    getByName("androidInstrumentedTest") {
      dependencies {
        implementation(libs.androidx.arch.core.testing)
        implementation(libs.androidx.espresso.contrib)
        implementation(libs.androidx.espresso.core)
        implementation(libs.androidx.espresso.intents)
        implementation(libs.androidx.test.ext.junit)
        implementation(libs.androidx.test.uiautomator)
        implementation(libs.androidx.work.testing)
        implementation(libs.androidx.compose.ui.test.junit4)
        implementation(libs.accessibility.test.framework)
        implementation(libs.kotlinx.coroutines.test)
      }
    }
  }

  targets.all {
    compilations.all {
      kotlinOptions {
        // Enable Coroutines and Flow APIs
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
      }
    }
  }
  androidTarget().compilations.all {
    kotlinOptions {
      // work-runtime-ktx 2.1.0 and above now requires Java 8
      jvmTarget = JavaVersion.VERSION_17.toString()
    }
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(platform(libs.androidx.compose.bom))
  // Add ksp dependencies for each targets
  kotlin.targets.all {
    val configurationName = if (name == "metadata") "kspCommonMainMetadata" else "ksp${name.replaceFirstChar(Char::uppercase)}"
    add(configurationName, libs.androidx.room.compiler)
    add(configurationName, libs.koin.ksp.compiler)
  }
  add("kspAndroidTest", libs.koin.ksp.compiler)
}

android {
  compileSdk = libs.versions.compileSdk.get().toInt()
  buildFeatures {
    dataBinding = true
  }
  defaultConfig {
    applicationId = "com.google.samples.apps.sunflower"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    testInstrumentationRunner = "com.google.samples.apps.sunflower.utilities.MainTestRunner"
    versionCode = 1
    versionName = "0.1.6"
    vectorDrawables.useSupportLibrary = true

    // Consult the README on instructions for setting up Unsplash API key
    buildConfigField("String", "UNSPLASH_ACCESS_KEY", "\"" + getUnsplashAccess() + "\"")
  }
  buildTypes {
    release {
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }
    create("benchmark") {
      initWith(getByName("release"))
      signingConfig = signingConfigs.getByName("debug")
      isDebuggable = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules-benchmark.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
    dataBinding = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }
  packagingOptions {
    // Multiple dependency bring these files in. Exclude them to enable
    // our test APK to build (has no effect on our AARs)
    resources.excludes += "/META-INF/AL2.0"
    resources.excludes += "/META-INF/LGPL2.1"
  }

  testOptions {
    managedDevices {
      devices {
        maybeCreate<com.android.build.api.dsl.ManagedVirtualDevice>("pixel2api27").apply {
          device = "Pixel 2"
          apiLevel = 27
          systemImageSource = "aosp"
        }
      }
    }
  }
  namespace = "com.google.samples.apps.sunflower"
}

androidComponents {
  onVariants(selector().withBuildType("release")) {
    // Only exclude *.version files in release mode as debug mode requires
    // these files for layout inspector to work.
    it.packaging.resources.excludes.add("META-INF/*.version")
  }
}

fun getUnsplashAccess(): String? {
  return project.findProperty("unsplash_access_key") as? String
}
