androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.fragment:fragment-ktx:1.8.2")

        // Room (offline cache)
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        implementation("androidx.room:room-compiler:2.6.1")

        // Coroutines (used by repository + background work)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    }
}
