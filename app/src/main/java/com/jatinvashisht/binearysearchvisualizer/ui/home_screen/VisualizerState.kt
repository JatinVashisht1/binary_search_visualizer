package com.jatinvashisht.binearysearchvisualizer.ui.home_screen

sealed interface VisualizerState{
    object Paused: VisualizerState
    object Playing: VisualizerState
    object Reset: VisualizerState
}
