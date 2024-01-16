package com.example.pokedex.data.local

import android.icu.text.IDNA
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.domain.Pokemon



    @Database(entities = [LocalPokemon::class], version = 2, exportSchema = false )
    abstract class PokemonDatabase : RoomDatabase() {

        abstract val dao : PokemonDAO
    }

