package com.hitesh.contricalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hitesh.contricalculator.ui.components.InputField
import com.hitesh.contricalculator.ui.theme.ContriCalculatorJetPackComposeTheme
import com.hitesh.contricalculator.ui.widget.RoundIconButton
import com.hitesh.contricalculator.util.calculatePerPersonCost
import com.hitesh.contricalculator.util.calculateTip

const val TAG = "CONTRI_CALCULATOR"

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

                    MainContent()
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
fun PriceCard(totalPerPerson: Double = 0.0) {
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

@Preview
@Composable
fun MainContent() {
    val totalCost = remember {
        mutableStateOf("")
    }

    val splitByValue = remember {
        mutableStateOf(1)
    }

    val splitRange = IntRange(1, 10)

    val perPersonCost = remember {
        mutableStateOf(0.0)
    }

    BillForm(splitRange, splitByValue, totalCost, perPersonCost)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    splitRange: IntRange,
    splitByValue: MutableState<Int>,
    totalCost: MutableState<String>,
    perPersonCost: MutableState<Double>,
    modifier: Modifier = Modifier
) {


    val totalCostValidState = remember(totalCost.value) {
        totalCost.value.trim().isNotEmpty()
    }

    val sliderPosition = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPosition.value * 100).toInt()

    val tipActual = remember {
        mutableStateOf(0.0)
    }


    val keyBoardController = LocalSoftwareKeyboardController.current
    PriceCard(perPersonCost.value)
    Spacer(modifier = Modifier.height(8.dp))

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)

    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
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

            if (totalCostValidState) {

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(R.string.split))
                    Spacer(modifier = Modifier.width(100.dp))
                    Row(
                        modifier = Modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundIconButton(vector = Icons.Default.Remove, onClick = {
                            if (splitByValue.value > 1) {
                                splitByValue.value = splitByValue.value - 1
                                perPersonCost.value = calculatePerPersonCost(
                                    splitByValue.value,
                                    totalCost.value,
                                    tipPercentage
                                )

                            }
                        })

                        Text(
                            text = "${splitByValue.value}",
                            modifier = Modifier.padding(horizontal = 10.dp),
                            style = MaterialTheme.typography.h6
                        )

                        RoundIconButton(vector = Icons.Default.Add, onClick = {
                            if (splitByValue.value <= splitRange.last) {
                                splitByValue.value = splitByValue.value + 1
                                perPersonCost.value = calculatePerPersonCost(
                                    splitByValue.value,
                                    totalCost.value,
                                    tipPercentage
                                )
                            }
                        })

                    }
                }

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.tip))
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        text = "$ ${tipActual.value}",
                        modifier = Modifier.padding(horizontal = 10.dp),
                        style = MaterialTheme.typography.h6
                    )
                }

                Text(
                    text = "${tipPercentage}%", modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )

                Slider(value = sliderPosition.value,
                    steps = 5,
                    onValueChange = {
                        sliderPosition.value = it

                        tipActual.value = calculateTip(totalCost.value, tipPercentage)

                        perPersonCost.value =
                            calculatePerPersonCost(
                                splitByValue.value,
                                totalCost.value,
                                tipPercentage
                            )
                        Log.d(TAG, "BillForm: $tipActual")
                    })
            } else {

                Box() {

                }
            }
        }
    }
}
