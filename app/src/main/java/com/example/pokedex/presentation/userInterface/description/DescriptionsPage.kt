package com.example.pokedex



import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.pokedex.domain.Pokemon
import com.example.pokedex.presentation.userInterface.HomePage.EvolutionBar
import com.example.pokedex.presentation.userInterface.HomePage.getTypeIconwithID
import com.example.pokedex.presentation.searchPageViewModel


    @Composable
    fun ShowcasePage(navController: NavHostController,viewModel: searchPageViewModel) {
        val context = LocalContext.current
        var selectedGender by remember { mutableStateOf(Gender.NONE) }
        val pokemon = viewModel.getPokemon()
        val maleColor = Color(49,59,169)
        val femaleColor = Color(143,68,124)
        var catchRateTextBox by remember { mutableStateOf(false) }
        var growthRateTextBox by remember { mutableStateOf(false) }
        var Favorized by remember { mutableStateOf(viewModel.PokemonsFave.value.contains(pokemon)) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState(), true)
        ) {
            // Top Baren folkens!
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                //Spacer(modifier = Modifier.width(14.dp))
                //Texten skal retrieve en string fra PokeAPI'en.

                viewModel.getPokemon()?.let {
                    Text(
                        text = it.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (pokemon != null) {
                            Image(
                                painter = painterResource(id = getTypeIconwithID(pokemon.type1)),
                                contentDescription = "type1",
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        if (pokemon != null) {
                            if (pokemon.type2 != "null") {
                                Image(
                                    painter = painterResource(id = getTypeIconwithID(pokemon.type2)),
                                    contentDescription = "type2",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
            Divider(
                color = Color.Black,
                thickness = 1.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        when (selectedGender) {
                            Gender.MALE -> maleColor
                            Gender.FEMALE -> femaleColor
                            else -> Color.Transparent // Or grey depends on logic.
                        }
                    )
            ) {
                if (pokemon != null) {
                    AsyncImage(
                        model = pokemon.pictureURL,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp),
                        contentScale = ContentScale.Crop
                    )


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                 //RYK GENDERICONS og Favorite ICON HER SÅ DET BLIVER IN PICTURE som FIGMA
                    ////////////////////////////////////////////////////
                    Spacer(modifier = Modifier.width(16.dp))

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                GenderIcon(
                    imageResId = R.drawable.male,
                    selectedGender = Gender.MALE,
                    onGenderSelected = { selectedGender = it }
                )

                GenderIcon(
                    imageResId = R.drawable.female,
                    selectedGender = Gender.FEMALE,
                    onGenderSelected = { selectedGender = it }
                )
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pokeball_bw),
                        contentDescription = "Favorite option",
                        //tint = if (viewModel.PokemonsFave.contains(pokemon)) Color.Red else Color.Black,
                        tint = if(Favorized) Color.Red else Color.Black,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable {
                                Favorized = !Favorized
                                pokemon?.let {
                                    if (Favorized) {


                                        PokemonObject._faveList.value =
                                            PokemonObject._pokeList.value
                                                .toMutableList()
                                                .apply {
                                                    add(it)
                                                } as ArrayList<Pokemon>
                                    } else {
                                        PokemonObject._faveList.value =
                                            PokemonObject._pokeList.value
                                                .toMutableList()
                                                .apply {
                                                    add(it)
                                                } as ArrayList<Pokemon>
                                        //if (viewModel.PokemonsFave.contains(pokemon))
                                        //  viewModel.PokemonsFave.remove(pokemon)
                                        //else
                                        //  pokemon?.let { viewModel.PokemonsFave.add(it) }

                                    }
                                }
                            }
                            .requiredSize(36.dp, 36.dp)
                            .align(Alignment.BottomEnd)
                    )
                }}

            Divider(
                color = Color.Black,
                thickness = 1.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ){
                    Text(text = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                    Spacer(modifier = Modifier.weight(4f))
                    EvolutionBar(navController)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Divider(
                color = Color.Black,
                thickness = 1.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rectangle),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.FillBounds
                )
                pokemon?.let {
                    Text(
                        text = pokemon.pokedexText,

                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterStart),
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left
                    )
                }
            }
            Divider(
                color = Color.Black,
                thickness = 1.5.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp))
            HeadlineAndInfoBox()
            CatchAndGrowthRateBoxes(viewModel = viewModel)
            Divider(
                    color = Color.Black,
            thickness = 1.5.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp))
        }
    }
    @Composable
    fun GenderIcon(
        imageResId: Int,
        selectedGender: Gender,
        onGenderSelected: (Gender) -> Unit
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { onGenderSelected(selectedGender) }
                .padding(2.dp)
                .border(
                    width = 2.dp,
                    color = if (selectedGender != Gender.NONE) Color.White else Color.Transparent,
                    //shape = CircleShape
                )
                .padding(2.dp),
        ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            //.padding(3.dp)
            //.size(36.dp)
            //.clickable { onGenderSelected(selectedGender) }
            //.border(
            //  width = 2.dp,
            //color = if (selectedGender != Gender.NONE) Color.White else Color.Transparent,),
            contentScale = ContentScale.Fit
            )
        }
    }


enum class Gender {
    MALE, FEMALE, NONE // None because some rare exist. Maybe gray should be added.
}

@Composable
@Preview(showBackground = true)
fun PokemonShowcasePreview() {
}

@Composable
fun CatchAndGrowthRateBoxes(viewModel: searchPageViewModel) {
    var catchRateTextBox by remember { mutableStateOf(false) }
    var growthRateTextBox by remember { mutableStateOf(false) }
    val pokemon = viewModel.getPokemon()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ) {
        /*Image(
            painter = painterResource(id = R.drawable.catchrate),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .clickable { catchRateTextBox = !catchRateTextBox }

      )*/

        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(120.dp)
                .height(40.dp)
                //.offset(x = 4.dp)
                .clip(RoundedCornerShape(25.dp))
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20.dp)
                ),
                //.clickable { catchRateTextBox = !catchRateTextBox },
            contentAlignment = Alignment.Center
        ) {
            if (pokemon != null) {
                Text(
                    text = pokemon.capture_rate.toString(),
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .width(120.dp)
                .height(40.dp)
                // .offset(x = 4.dp)
                .clip(RoundedCornerShape(25.dp))
                .border(
                    width = 2.dp,
                    color = Color.LightGray,
                    shape = RoundedCornerShape(20.dp)
                ),
                //.clickable { growthRateTextBox = !growthRateTextBox },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = pokemon?.growth_rate.toString(),
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    /*Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (catchRateTextBox) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .width(120.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
                    .padding(3.dp)
            ) {
                Text(
                    text = "This is the catch rate of the Pokemon.",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (growthRateTextBox) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .width(120.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray)
                    .padding(3.dp)
            ) {
                Text(
                    text = "This is the growth rate of the Pokemon.",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }*/
}


val gradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFD4C21B), Color(0xFF76C5DE)))
@Composable
fun GradientBox() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Box(
            modifier = Modifier
                .height(180.dp)
                .width(350.dp)
                .background(Color.Transparent)
                .border(width = 1.dp, brush = gradient, shape = RoundedCornerShape(35.dp))
        )
    }
}

@Composable
fun HeadlineAndInfoBox() {
var infoBox by remember { mutableStateOf(false) }
    Box {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Catch and Growth Rates",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Image(
                painter = painterResource(id = R.drawable.questionmark),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .offset(x = -8.dp)
                    .clickable { infoBox = !infoBox }
            )
        }
    }
        if(infoBox){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .height(130.dp)
                        .width(350.dp)
                        .background(Color.LightGray)
                ) {
                    Text(
                        text = "Catch rate on the left, Growth rate on the right. " +
                                "The catch rate shows the difficulity of catching the Pokemon." +
                                "The growth rate means the rate of which the Pokemon gains XP and levels up. ",
                        modifier = Modifier
                            .padding(16.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
}

@Composable
fun Divider() {
    Divider(
        color = Color.Black,
        thickness = 1.5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}