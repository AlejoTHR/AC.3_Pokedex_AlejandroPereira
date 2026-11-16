package com.example.ac3_pokedex_alejandropereira

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ac3_pokedex_alejandropereira.ui.theme.AC3_Pokedex_AlejandroPereiraTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
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


val Context.dataStore by preferencesDataStore(
    name = "preferencias_Pokedex"
)
private val SHOW_WAS_CAPTURED = booleanPreferencesKey("show_Was_Captured")
suspend fun saveWasCaptured(
    context: Context,
    show_captured : Boolean
){
    context.dataStore.edit {
        preferences -> preferences[SHOW_WAS_CAPTURED] = show_captured
    }
}
/*
fun ShowWasCaptured(context: Context): Flow<Boolean>{
    return context.dataStore.data.map { preferences -> preferences[SHOW_WAS_CAPTURED] ? = false
    }
}
*/
var pokemonShowDescription by mutableStateOf(Pokemon(1,"Bulbasour",10,10,arrayListOf(12,12,12,12,12,12),arrayListOf("Plant","Poison"),arrayListOf(1,3,5,7,9,11,13,15), arrayListOf("ssss","aaaa","dddd","fffff","tttttt","kkkkkk"),"p001.png",false))

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
                            ListScreen(modifier = Modifier.padding(innerPadding),
                                {
                                    navController.navigate("details")
                                })
                        }
                        composable("details") {
                            PokemonScreen(modifier = Modifier.padding(innerPadding),
                                {
                                    navController.navigate("home")
                            }, pokemonShowDescription ,LocalContext.current)




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

// GETS FROM LIST
fun getPokemonId(pokemon : Pokemon) : String{
    if(pokemon.id <10){
        return "00${pokemon.id}"
    }
    else if(pokemon.id <100 && pokemon.id >= 10){
        return "0${pokemon.id}"
    }
    return "${pokemon.id}"
}
fun getPokemonName(pokemon : Pokemon) : String{
    return pokemon.name
}
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
// ALL SCREEN COMPONENTS COMBINATION
@Composable
fun ListScreen(modifier: Modifier, onPokemonClick : () -> Unit) {
    val context = LocalContext.current
    val input = context.assets.open("PokedexData.json")
    val texto = input.bufferedReader().use { it.readText() }
    val pokemonGLobalList: List<Pokemon> = Json.decodeFromString(texto)
    input.close()

    Column() {
        CreateHeader()
        LazyColumn(contentPadding = PaddingValues(0.dp, 5.dp, 10.dp)) {
            items(pokemonGLobalList) { PokemonFromList ->
                // BUTON FOR EACH Pokemon IN PokemonList
                CreateButton(PokemonFromList,
                    {
                        pokemonShowDescription = PokemonFromList
                        onPokemonClick()
                })
            }
        }
    }
}


// CREATE BUTTON INTERIOR
@Composable
fun CreateButton(pokemon : Pokemon, onPokemonButtonClick : ()->Unit){
    Box(Modifier.fillMaxWidth()) {
        Button(onPokemonButtonClick, modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier) {
                Spacer(Modifier.width(5.dp))
                Box(modifier = Modifier.weight(0.1f), contentAlignment = Alignment.TopStart){
                    Text(
                        text = getPokemonId(pokemon),
                        fontSize = 16.sp
                    )
                }
                Box(modifier =  Modifier.padding(0.dp,0.dp,20.dp), contentAlignment = Alignment.TopStart){
                    Text(
                        text = getPokemonName(pokemon),
                        fontSize = 16.sp
                    )
                }
                Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.TopCenter){
                    Image(
                        painter = painterResource(R.drawable.pokeballempty),
                        contentDescription = "NotCaptures",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

        }
    }

}


@Composable
fun getPokemonImg(context: Context, pokemonImg: Pokemon) : Int{
    val imgPath = "app/res/drawable/p${pokemonImg.image_id}"
    val resId =
        context.resources.getIdentifier(
            imgPath,
            "drawable",
            context.packageName
        )
    return resId
}

// POKEMON SCREEN
@Composable
fun PokemonScreen(modifier: Modifier, onBackClick : ()->Unit, pokemonToShow : Pokemon, context: Context){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)){
                Button(onClick = onBackClick, modifier = Modifier){
                    Text("Go Back")
                }
            }
        }
        item{
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(0.dp, 10.dp, 0.dp), contentAlignment = Alignment.Center){
                Image(painter = painterResource(R.drawable.p001), contentDescription = "PokemonImage", modifier = Modifier.fillMaxSize())
            }
        }
        item{
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Box(modifier = Modifier.padding(20.dp,0.dp,50.dp), contentAlignment = Alignment.Center){
                    Text("ID: ${getPokemonId(pokemonToShow)}", fontSize = 24.sp)
                }
                Box(modifier = Modifier.padding(0.dp,0.dp,0.dp), contentAlignment = Alignment.Center){
                    Text(getPokemonName(pokemonToShow), fontSize = 24.sp)
                }
            }
        }
        items(pokemonToShow.type){ pokemonType ->
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){
                Text(text = pokemonType)
            }
        }
        item{
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){
                Text("Weight: ${pokemonToShow.weight.toString()}", fontSize = 16.sp)
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter){
                Text("Height: ${pokemonToShow.height.toString()}", fontSize = 16.sp)
            }
        }
        item{
            Box(modifier = Modifier.fillMaxWidth().padding(10.dp,0.dp,10.dp).background(Color.Gray), contentAlignment = Alignment.TopCenter){
                Text(pokemonToShow.flavor_text[2], fontSize = 18.sp)
            }
        }


    }

}

@Preview(showBackground = true)
@Composable
fun TEST() {
    AC3_Pokedex_AlejandroPereiraTheme {
        ListScreen(Modifier,{})
    }
}
@Preview(showBackground = true)
@Composable
fun TEST_2() {
    AC3_Pokedex_AlejandroPereiraTheme {
        PokemonScreen(modifier = Modifier, {}, pokemonShowDescription, LocalContext.current)
    }
}

