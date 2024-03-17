plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.kotlin.ttknpdev"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kotlin.ttknpdev"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // By default, your resources are located in src/main/res
    // you can change these paths to any other location with the res.srcDirs property in the sourceSets block
    sourceSets {
        getByName("main") {
            res.run {
                srcDirs("src/main/res/layouts/main")
                srcDirs("src/main/res/layouts/fragments")
                srcDirs("src/main/res/layouts/navigations")
                srcDirs("src/main/res/layouts/items")
                srcDirs("src/main/res/images")
            }
        }
    }
}

dependencies {
    val fragment_version = "1.6.2"
    // for clear fragments
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    // Below is the dependency for Volley which we will be using to get the data from API. For adding this dependency
    implementation("com.android.volley:volley:1.2.1")
    // for using Gson class,  use to convert json to Object class (Easy!!)
    implementation("com.google.code.gson:gson:2.9.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}