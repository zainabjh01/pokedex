package com.example.pokedex.viweModel

import android.util.Log
import androidx.lifecycle.*
import com.example.pokedex.domain.Pokemon
import com.example.pokedex.PokemonObject
import kotlinx.coroutines.*
import java.net.URL
import org.json.JSONObject

class RepositoryImpl: ViewModel() {
//    load image
    //imageView.load("https://example.com/image.jpg")

    fun addPokemon(start: Int, end: Int, onlyDefaults: Boolean, cleanCopy: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
//            val fileName = "json/pokemonCache.json"
//            var file = File(fileName)
//            var cacheJson = JSONObject(file.readText())
            val limit = start + end
//            val jsonData =URL("https://pokeapi.co/api/v2/pokemon?limit=$limit&offset=$start").readText()
//            val pokemonJson = JSONObject(jsonData)
            var i: Int = start

            while (i <= end) {
////            if(cleanCopy and (!cacheJson.has("$i"))) {
//
                val jsonData2 = URL("https://pokeapi.co/api/v2/pokemon/$i").readText()
                val jsonDataSpecies = URL("https://pokeapi.co/api/v2/pokemon-species/$i").readText()
////            }
                var pokemonJson2 = JSONObject(jsonData2)
                var pokemonJsonSpecies = JSONObject(jsonDataSpecies)
                if (pokemonJson2.getBoolean("is_default") or !onlyDefaults) {
                    var pokeName: String =
                        pokemonJson2.getString("name")


                    var pokeId: Int =
                        pokemonJson2.getInt("id")


                    var pokeDefaultPictureFront: String =
                        pokemonJson2.getJSONObject(
                            "sprites"
                        ).getJSONObject("other").getJSONObject("official-artwork")
                            .getString("front_default")
                    var type1: String =
                        pokemonJson2.getJSONArray("types").getJSONObject(0).getJSONObject("type")
                            .getString("name")


                    var type2: String = "null"
                    if (pokemonJson2.getJSONArray("types").length() > 1) {
                        type2 = pokemonJson2.getJSONArray("types").getJSONObject(1)
                            .getJSONObject("type").getString("name")
                    }
                    val pokedexTextList = ArrayList<String>()
                    pokedexTextList.add(
                        pokemonJsonSpecies.getJSONArray("flavor_text_entries").getJSONObject(0)
                            .getString("flavor_text")
                    )

                    Log.d(
                        "info",
                        "" + pokeName + " " + pokeDefaultPictureFront + " " + pokeId + " " + type1 + " " + type2
                    )

                    PokemonObject.pokeList.add(
                        Pokemon(
                            pokeName.replaceFirstChar { it.uppercase() },
                            pokeDefaultPictureFront,
                            pokeId,
                            type1,
                            type2,
                            pokedexTextList
                            ,addEvolutionChain(pokeID = pokeId
                            )
                        )
                    )
//                cacheJson.put(""+pokeId,pokeName)
                }

                i++
            }

        }

    }

    fun addEvolutionChain(pokeID: Int): ArrayList<String> {
        val jsonDataSpecies = URL("https://pokeapi.co/api/v2/pokemon-species/$pokeID").readText()
        val evolutionJson1 = JSONObject(jsonDataSpecies)
        val chainUrl = evolutionJson1.getJSONObject("evolution_chain").getString("url")

        val jsonDataChain = URL(chainUrl).readText()
        val evolutionJson2 = JSONObject(jsonDataChain)

        val evoS = ArrayList<String>()

        // Add the initial Pokémon species to the list
        val initialSpeciesName =
            evolutionJson2.getJSONObject("chain").getJSONObject("species").getString("name")
        evoS.add(initialSpeciesName)

        val evolvesToArray = evolutionJson2.getJSONObject("chain").getJSONArray("evolves_to")


        // For the all second stage.
        for (i in 0 until evolvesToArray.length()) {
            val evolvesToObj = evolvesToArray.getJSONObject(i)
            val evolvesToSpeciesName = evolvesToObj.getJSONObject("species").getString("name")
            evoS.add(evolvesToSpeciesName)

            // For all third stage
            val furtherEvolutions = evolvesToObj.getJSONArray("evolves_to")
            for (j in 0 until furtherEvolutions.length()) {
                val furtherEvolutionSpeciesName = furtherEvolutions.getJSONObject(j).getJSONObject("species").getString("name")
                evoS.add(furtherEvolutionSpeciesName)
            }
        }

        return evoS
    }


}



