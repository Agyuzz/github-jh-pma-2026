package com.example.notehub.data

import android.content.Context
import androidx.room3.Room
import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object NoteHubDatabaseInstance {

    /*@Volatile znamená, že proměnná je viditelná pro všechna vlákna okamžitě. */
    /* V praxi: když se v jednom vlákně změní hodnota (např. INSTANCE databáze),
       ostatní vlákna uvidí tu novou hodnotu hned — ne až po zpoždění z cache paměti. */
    @Volatile
    private var INSTANCE: NoteHubDatabase? = null

    // migrace z verze 1 na verzi 2 (sloupec priority)
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override suspend fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN priority INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    //migrace z verze 2 na verzi 3 (sloupce createdAt a category)
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override suspend fun migrate(connection: SQLiteConnection) {
            // createdAt (Long -> INTEGER) nesmí být NULL, proto DEFAULT 0
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
            )

            // category (String -> TEXT) nesmí být NULL, proto DEFAULT 'General'
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN category TEXT NOT NULL DEFAULT 'General'"
            )
        }
    }

    /**
     * Vrátí instanci databáze. Pokud ještě neexistuje, vytvoří ji.
     */
    fun getDatabase(context: Context): NoteHubDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NoteHubDatabase::class.java,
                "notehub_database"
            )
                //.setDriver(AndroidSQLiteDriver()) // NOVINKA V ROOM 3: Nastavení ovladače pro Android
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // ZMĚNA: obě migrace
                .build()
            INSTANCE = instance
            instance
        }
    }

}