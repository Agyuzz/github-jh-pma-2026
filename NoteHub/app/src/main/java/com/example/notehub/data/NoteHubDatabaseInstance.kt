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

    // migrace z verze 1 na verzi 2
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        // "suspend", protože metoda migrate je v Room 3 suspend funkce
        override suspend fun migrate(connection: SQLiteConnection) {
            // Přidá do existující tabulky nový sloupec "priority".
            // NOT NULL DEFAULT 0 = stávající řádky dostanou hodnotu 0 (žádná data se neztratí)
            connection.execSQL(
                "ALTER TABLE note_table ADD COLUMN priority INTEGER NOT NULL DEFAULT 0"
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
                .addMigrations(MIGRATION_1_2) // zaregistruje migraci 1 -> 2
                .build()
            INSTANCE = instance
            instance
        }
    }

}