package com.example.notehub.data

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [Note::class],
    version = 3,
    exportSchema = false
)

abstract class NoteHubDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}