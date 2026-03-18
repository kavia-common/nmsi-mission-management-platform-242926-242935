package org.example.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for offline caching.
 *
 * Note: schema migrations are omitted because this is a demo/mock app. If schema changes, bump
 * version and provide a migration.
 */
@Database(
    entities = [
        CachedListItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cachedListItemDao(): CachedListItemDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        // PUBLIC_INTERFACE
        fun getInstance(context: Context): AppDatabase {
            /** Returns singleton Room DB instance for the application process. */
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nmsi_app.db",
                ).build().also { instance = it }
            }
        }
    }
}
