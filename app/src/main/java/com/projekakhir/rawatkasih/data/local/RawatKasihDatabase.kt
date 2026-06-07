package com.projekakhir.rawatkasih.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        MedicineScheduleEntity::class,
        MedicineLogEntity::class,
        DailyConditionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RawatKasihDatabase : RoomDatabase() {
    abstract fun dao(): RawatKasihDao

    companion object {
        @Volatile
        private var Instance: RawatKasihDatabase? = null

        fun getDatabase(context: Context): RawatKasihDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RawatKasihDatabase::class.java,
                    "rawatkasih_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
