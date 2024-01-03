package com.example.pokedex
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pokedex.domain.Pokemon
import com.example.pokedex.presentation.theme.PokedexTheme
import com.example.pokedex.presentation.navigation.navStart
import com.example.pokedex.viweModel.RepositoryImpl


object PokemonObject{
    var pokeList = ArrayList<Pokemon>()
    var faveList = ArrayList<Pokemon>()
}


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepositoryImpl().addPokemon(1,40,true,true)

        setContent {
            PokedexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    navStart()
                }
            }
        }

    }
}
