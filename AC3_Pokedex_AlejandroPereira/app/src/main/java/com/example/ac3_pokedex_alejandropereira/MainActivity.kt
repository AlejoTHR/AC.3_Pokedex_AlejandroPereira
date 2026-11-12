package com.example.ac3_pokedex_alejandropereira

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ac3_pokedex_alejandropereira.ui.theme.AC3_Pokedex_AlejandroPereiraTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Serializable
// NP = NO PRINT
data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val stats: List<Int>,
    val type: List<String>,
    val language_id : List<Int>,
    val flavor_text: List<String>

)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent{


            AC3_Pokedex_AlejandroPereiraTheme {
                // NAVIGATION BETWEEN ACTIVITIES
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController, "home",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("home") {
                            ListScreen(modifier = Modifier.padding(innerPadding))

                        }
                    }




                }
            }
        }
    }

}

@Composable
fun ReadFromJson(){
    val context = LocalContext.current
    val input = context.assets.open("PokedexData.json")
    val textRead = input.bufferedReader().use { it.readText() }
    val pokemon: Pokemon = Json.decodeFromString(textRead)
}
@Composable
fun WriteFromJson(pokemon: Pokemon){
    val context = LocalContext.current
    val textJson = Json.encodeToString(pokemon)
    context.openFileOutput("PokedexData.json", Context.MODE_PRIVATE).bufferedWriter().use {
        writer -> writer.write(textJson)
    }

}



// POKEMON LIST
fun GetPokemonId(pokemon : Pokemon) : String{
    if(pokemon.id <10){
        return "00${pokemon.id}"
    }
    else if(pokemon.id <100 && pokemon.id >= 10){
        return "0${pokemon.id}"
    }
    return "${pokemon.id}"
}
fun GetPokemonName(pokemon : Pokemon) : String{
    return pokemon.name
}
@Composable
fun GetPokemonImg(pokemon : Pokemon, pokemonid : Int){


}

@Composable
fun PrintPokemonButton(onClick: () -> Unit, pokemon: Pokemon){
    Box(Modifier.width(200.dp)){
        Button(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Row(){
                Text(
                    text = GetPokemonName(pokemon),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.weight(0.1f))
                Text(
                    text = GetPokemonId(pokemon),
                    fontSize = 16.sp

                )
            }
        }
    }
}

@Composable
fun CreateScreenView(pokemons : List<Pokemon>, onClick : () -> Unit){
    Column() {
        Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.Gray), contentAlignment = Alignment.Center){
            Text(text = "Kanto Pokédex",
                textAlign = TextAlign.Center
            )

        }
        Row(){
            Column(Modifier.weight(0.9f)) {


            }
            LazyColumn(contentPadding = PaddingValues(0.dp, 5.dp,10.dp) ) {
                items(pokemons) { pokemon->
                    PrintPokemonButton(onClick, pokemon)
                }
            }
        }
    }

}
@Composable
fun ListScreen(modifier: Modifier){
    val context = LocalContext.current
    val input = context.assets.open("PokedexData.json")
    val texto = input.bufferedReader().use { it.readText() }
    val pokemons : List<Pokemon> = Json.decodeFromString(texto)
    input.close()

    CreateScreenView(pokemons, {})
}





@Preview(showBackground = true)
@Composable
fun TEST() {
    AC3_Pokedex_AlejandroPereiraTheme {
        ListScreen(Modifier)

    }
}

