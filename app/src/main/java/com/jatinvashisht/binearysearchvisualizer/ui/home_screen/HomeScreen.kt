package com.jatinvashisht.binearysearchvisualizer.ui.home_screen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    currentOnStart: () -> Unit,
    currentOnStop: () -> Unit,
) {
    var buttonText by remember{ mutableStateOf("Play") }
    val intList by remember{ mutableStateOf(viewModel.elementList.value)}
    LaunchedEffect(key1 = viewModel.visualizerStatePublic.value){
        buttonText = when(viewModel.visualizerStatePublic.value){
            VisualizerState.Paused -> {
                "Play"
            }
            VisualizerState.Playing -> {
                "Pause"
            }
            VisualizerState.Reset -> {
                "Start"
            }
        }
    }

    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver{_, event ->
            if(event == Lifecycle.Event.ON_START){
                currentOnStart()
            }else if(event == Lifecycle.Event.ON_STOP){
                currentOnStop()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PlayPauseButton(
                    buttonText = buttonText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    viewModel.onEvent(VisualizerEvents.PlayPauseClicked)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                TextBoxes(intList = viewModel.elementList.value)
            }
        }
    }
}

@Composable
fun PlayPauseButton(modifier: Modifier = Modifier, buttonText: String ,onButtonClicked: () -> Unit,) {
    Button(onClick = onButtonClicked, modifier = modifier) {
        Text(text = buttonText)
    }
}

@Composable
fun TextBoxes(intList: List<Int>) {
    Log.d("HomeScreen", "entered in text boxes function $intList")
    intList.forEach {
        var  colorState by remember{ mutableStateOf(Color.Gray) }
        val color by animateColorAsState(targetValue = colorState, animationSpec = tween(300))
        Box(modifier = Modifier
            .size(75.dp)
            .padding(4.dp)
            .drawBehind { drawRect(color = color) },
            contentAlignment = Alignment.Center
        ){
            Text(text = it.toString(), modifier = Modifier.align(Alignment.Center))
        }
    }
}