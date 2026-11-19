package com.example.notehub.data

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // ID poznámky, automaticky generované
    val title: String, // Název poznámky
    val content: String, // Obsah poznámky

    @ColumnInfo(defaultValue = "0") val priority: Int = 0,

    // NOVÉ 1: časové razítko vytvoření (v ms), vyplní se samo při vytvoření objektu
    val createdAt: Long = System.currentTimeMillis(),

    // NOVÉ 2: kategorie poznámky
    val category: String = "General"
)