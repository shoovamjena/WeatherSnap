package com.example.weathersnap.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherScreen(){
    var organisation by remember { mutableStateOf("") }
    Scaffold(
        Modifier.fillMaxSize().background(Color(0xff2b3009))
    ) {_ ->
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0.5f to Color(0xffbfcd81),
                            0.9f to Color(0xffa3d0c0)
                        )
                    ), RoundedCornerShape(15))
            ){
                Row(
                    Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            text = "WeatherSnap",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Text(
                            "Live weather reports with camera evidence",
                            color = Color.Black,
                            fontSize = 10.sp
                        )
                    }
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(Color(0xff2d3400)),
                        shape = RoundedCornerShape(25)
                    ) {
                        Text(
                            "Reports",
                            color = Color(0xffbfcd81),
                            fontSize = 10.sp
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(15))
                    .padding(10.dp)
                ,
                verticalAlignment = Alignment.CenterVertically,
            ){
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = organisation,
                    onValueChange = { organisation = it },
                    label = {
                        Text(
                            "City",
                            color = Color(0xffbfcd81),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Enter city",
                            color = Color(0xffbfcd81).copy(0.5f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(15),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White.copy(0.5f)
                    ),
                    supportingText = {
                        Text(
                            "Enter more than 2 letters to start city suggestion",
                            fontSize = 9.sp,
                            color = Color(0xffbfcd81),
                            textAlign = TextAlign.Left
                        )
                    }
                )
                Spacer(Modifier.width(20.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(Color(0xffbfcd81)),
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        "Search",
                        color = Color.Black
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(Color(0xff35352d), RoundedCornerShape(15.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column() {
                        Text(
                            "Ben, Iran",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Partly cloudy",
                            color = Color.White.copy(0.5f)
                        )
                    }
                    Box(
                        Modifier.background(
                            Color(0xff424b07),
                            RoundedCornerShape(15)
                        ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "17°C",
                            fontSize = 24.sp,
                            color = Color(0xffbfcd81),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        Modifier.weight(1f).background(
                            Color(0xff2e7964).copy(0.15f), RoundedCornerShape(15)
                        )
                    ){
                        Column(
                            Modifier.padding(10.dp)
                        ) {
                            Text(
                                "Humidity",
                                color = Color.White.copy(0.5f),
                                fontSize = 12.sp
                            )
                            Text(
                                "43%",
                                color = Color(0xff2e7964),
                                fontSize = 16.sp
                            )
                        }
                    }
                    Box(
                        Modifier.weight(1f).background(
                            Color(0xff335c8f).copy(0.15f), RoundedCornerShape(15)
                        )
                    ){
                        Column(
                            Modifier.padding(10.dp)
                        ) {
                            Text(
                                "Wind",
                                color = Color.White.copy(0.5f),
                                fontSize = 12.sp
                            )
                            Text(
                                "3.83 m/s",
                                color = Color(0xff335c8f),
                                fontSize = 16.sp
                            )
                        }
                    }
                    Box(
                        Modifier.weight(1f).background(
                            Color(0xff775218).copy(0.15f), RoundedCornerShape(15)
                        )
                    ){
                        Column(
                            Modifier.padding(10.dp)
                        ) {
                            Text(
                                "Pressure",
                                color = Color.White.copy(0.5f),
                                fontSize = 12.sp
                            )
                            Text(
                                "791",
                                color = Color(0xff775218),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                Box(
                    Modifier.fillMaxWidth().background(Color(0xff424338), RoundedCornerShape(15))
                        .padding(20.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Report readiness",
                            color = Color.White.copy(0.5f),
                            fontSize = 10.sp
                        )
                        Text(
                            "Camera and Room DB enabled",
                            fontSize = 12.sp
                        )
                    }
                }
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xffbfcd81))
                ) {
                    Text(
                        "Create Report",
                        color = Color.Black
                    )
                }
            }
        }
    }
}
