package com.example.ac3_pokedex_alejandropereira

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ac3_pokedex_alejandropereira.ui.theme.AC3_Pokedex_AlejandroPereiraTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


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
    val flavor_text: List<String>,
    val image_id : String,
    val captured : Boolean
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
// IN SCREEN

// HEADER
@Composable
fun CreateHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(Color.Gray), contentAlignment = Alignment.Center){
        Text(text = "Kanto Pokédex",
            textAlign = TextAlign.Center
        )
    }
}
// GETS FROM LIST
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




// ALL SCREEN COMPONENTS COMBINATION
@Composable
fun ListScreen(modifier: Modifier){
    val context = LocalContext.current
    val input = context.assets.open("PokedexData.json")
    val texto = input.bufferedReader().use { it.readText() }
    val PokemonsList : List<Pokemon> = Json.decodeFromString(texto)
    input.close()

    Column() {
        CreateHeader()
        Row(){

            Column(Modifier.weight(0.9f)) {

                Spacer(modifier = Modifier.weight(0.4f))

                Box(modifier = Modifier.fillMaxWidth().weight(0.3f) ){
                    // SET POKEMON IMAGE
                }


                Box(modifier = Modifier.padding(10.dp,0.dp,10.dp)){
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {/* SEND TO DETAILS ACTIVITY */}
                    ) {
                        Text(text = "Details")
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

            }

            LazyColumn(contentPadding = PaddingValues(0.dp, 5.dp,10.dp) ) {
                items(PokemonsList) { Pokemon ->
                    // BUTON FOR EACH Pokemon IN PokemonList
                    Box(Modifier.width(200.dp)){
                        Button(onClick = { }, modifier = Modifier.fillMaxSize()) {
                            CreateButtonInterior(Pokemon)
                        }
                    }
                }
            }
        }
    }
}
// CREATE BUTTON INTERIOR
@Composable
fun CreateButtonInterior(pokemon : Pokemon){
    Row(modifier = Modifier) {
        Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.TopCenter){
            Image(
                painter = painterResource(R.drawable.pokeballempty),
                contentDescription = "NotCaptures",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(Modifier.width(5.dp))
        Box(modifier = Modifier.weight(0.1f), contentAlignment = Alignment.TopStart){
            Text(
                text = GetPokemonId(pokemon),
                fontSize = 16.sp

            )
        }
        Box(modifier =  Modifier, contentAlignment = Alignment.TopStart){
            Text(
                text = GetPokemonName(pokemon),
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TEST() {
    AC3_Pokedex_AlejandroPereiraTheme {
        ListScreen(Modifier)

    }
}

