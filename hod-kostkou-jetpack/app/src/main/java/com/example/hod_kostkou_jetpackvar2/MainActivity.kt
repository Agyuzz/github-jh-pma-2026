package com.example.hod_kostkou_jetpackvar2

import android.R.attr.color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                DiceApp()
            }
        }
    }
}

@Composable

fun DiceApp(){
    val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")
    var diceValue by remember { mutableStateOf(value =1) }
    var isRolling by remember { mutableStateOf(value = false) }
    var totalRolls by remember { mutableStateOf(value = 0) }
    var counts by remember { mutableStateOf(List(6){0}) }
    val scope = rememberCoroutineScope()

    val backgroundColor = Color (0xFFF5F3FF)
    val primaryColor = Color(0xFF352060)

    Column(
        modifier = Modifier
            . fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hoď si",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Text(
            text = diceSymbols[diceValue -1],
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        //přidání textu Počet hodů...
        Text(
            text = "Počet hodů: $totalRolls",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            modifier = Modifier.padding(top = 24.dp)
        )
        //compose se překreslí jen při změně State
        counts.forEachIndexed { index, count ->
            Text(
                text = "${diceSymbols[index]} ${index + 1}: ${count}×",
                fontSize = 30.sp,
                color = primaryColor
            )
        }

        Button(
            enabled = !isRolling,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = Color.White,

                ),
            onClick = {
                isRolling = true

                scope.launch {
                    repeat (times = 10){
                        diceValue = (1..6).random()
                        delay (timeMillis = 250)
                    }

                    diceValue = (1..6).random()

                    totalRolls++
                    counts = counts.toMutableList().also { it[diceValue -1]++ }

                    isRolling = false
                }
            }
        ) {
            Text(
                text = "Hodit",
                fontSize = 26.sp
            )
        }

    }
}
