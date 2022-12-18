package com.jatinvashisht.binearysearchvisualizer.di

import com.jatinvashisht.binearysearchvisualizer.util.BinarySearchHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBinarySearchHelper() : BinarySearchHelper = BinarySearchHelper()
}