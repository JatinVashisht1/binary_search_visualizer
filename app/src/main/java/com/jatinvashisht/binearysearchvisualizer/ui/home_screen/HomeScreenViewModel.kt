package com.jatinvashisht.binearysearchvisualizer.ui.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jatinvashisht.binearysearchvisualizer.util.BinarySearchHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val binarySearchHelper: BinarySearchHelper) :
    ViewModel() {
    // sorted list of integers
    private val elementListState = mutableStateOf(listOf(4, 8, 10, 11, 15, 16))
    val elementList = elementListState
    private val target = mutableStateOf(8)
    private val elementPhaseList: MutableList<List<Int>> = mutableListOf()
    private val low = mutableStateOf(0)
    private val high = mutableStateOf(elementListState.value.size)
    private val mid = mutableStateOf(
        binarySearchHelper.getMid(
            low = low.value,
            high = high.value
        )
    )

    private var visualizerState = mutableStateOf<VisualizerState>(VisualizerState.Paused)
    val visualizerStatePublic = visualizerState as State<VisualizerState>

    private val indexGettingProcessed = mutableStateOf<Int>(0)
    init {
        elementPhaseList.add(elementListState.value)
        viewModelScope.launch {
            binarySearchHelper.binarySearch(elementList = elementListState.value, target = target.value){updatedList ->
//                if(updatedList.isNotEmpty())
                    elementPhaseList.add(updatedList)
                Log.d("ViewModel", "updated list is $updatedList")
            }
        }
    }

    fun onEvent(visualizerEvent: VisualizerEvents){
        when(visualizerEvent){
            VisualizerEvents.PlayPauseClicked -> {
                playPauseClicked()
            }
        }
    }

    private fun playPauseClicked(){
            Log.d("ViewModel", "play pause clicked ${visualizerState.value.toString()}")
        when(visualizerState.value){
            VisualizerState.Paused -> {
                play()
            }
            VisualizerState.Playing -> {
                pause()
            }
            VisualizerState.Reset -> {
                reset()
            }
        }
    }

    private fun reset() {
        elementListState.value = elementPhaseList[0]
        indexGettingProcessed.value = 0
        visualizerState.value = VisualizerState.Paused
    }

    private fun pause() {
        Log.d("ViewModel", "pause clicked ${elementListState.value}")
        visualizerState.value = VisualizerState.Paused
    }

    private fun play() {
        visualizerState.value = VisualizerState.Playing
        viewModelScope.launch {
            for(i in indexGettingProcessed.value until elementPhaseList.size){
                if(visualizerState.value == VisualizerState.Paused){
                    indexGettingProcessed.value = i
                    return@launch
                }
                elementListState.value = elementPhaseList[i]
                Log.d("viewmodel", "entered in play function ${elementListState.value}")
                low.value = 0
                high.value = elementListState.value.size - 1
                mid.value = binarySearchHelper.getMid(low = low.value, high = high.value)
                delay(1000)
            }
            visualizerState.value = VisualizerState.Reset
        }
    }


}

sealed interface VisualizerEvents{
    object PlayPauseClicked: VisualizerEvents
}