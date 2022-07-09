package com.hitesh.contricalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitesh.contricalculator.ui.components.InputField
import com.hitesh.contricalculator.ui.theme.ContriCalculatorJetPackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            MyApp {
                Column(
                    Modifier
                        .padding(16.dp)
                ) {
                    PriceCard()
                    Spacer(modifier = Modifier.height(8.dp))
                    CalculatorInputView()
                }
            }
        }

    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    ContriCalculatorJetPackComposeTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Preview()
@Composable
fun DefaultPreview() {
    ContriCalculatorJetPackComposeTheme {
        MyApp {
            //PriceCard()
        }
    }
}

@Preview()
@Composable
fun PriceCard(totalPerPerson: Double = 134.35) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        color = Color(0xFFA084D3)
    ) {
        Column(
            modifier = Modifier.padding(
                top = 32.dp,
                bottom = 32.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = stringResource(R.string.total_per_person_heading),
                style = MaterialTheme.typography.h5,
                color = Color.Black
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CalculatorInputView() {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)

    ) {
        val totalCost = remember {
            mutableStateOf("")
        }

        val totalCostValidState = remember(totalCost.value) {
            totalCost.value.trim().isNotEmpty()
        }

        val keyBoardController = LocalSoftwareKeyboardController.current

        Column() {
            InputField(
                modifier = Modifier.fillMaxWidth(),
                valueState = totalCost,
                labelId = stringResource(R.string.enter_bill),
                enable = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!totalCostValidState)
                        return@KeyboardActions


                    keyBoardController?.hide()
                }
            )
        }
    }
}

