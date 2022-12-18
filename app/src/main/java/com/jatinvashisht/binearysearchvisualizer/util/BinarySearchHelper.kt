package com.jatinvashisht.binearysearchvisualizer.util

import android.util.Log
import javax.inject.Inject

class BinarySearchHelper @Inject constructor() {
    fun getMid(low: Int, high: Int): Int = (high + low)/2

    suspend fun binarySearch(elementList: List<Int>, target: Int,onPass: (updatedList: List<Int>)->Unit){
        Log.d("binarysearch", "element list is $elementList, target is $target")
        val elementListSize = elementList.size
        var low = 0
        var high = elementListSize - 1
        while(low <= high){
            val mid = getMid(low = low, high = high)
            val midElement = elementList[mid]
            val lowElement = elementList[low]
            val highElement = elementList[high]

            if(midElement == target){
                Log.d("binarysearch", "mid element is $midElement, target is $target")
                onPass(listOf(midElement))
                return
            }
            else if(midElement > target){
//                Log.d("binarysearch", "mid element is $midElement, target is $target")
                high = mid-1
                onPass(elementList.subList(low, high+1))
            }else{
//                Log.d("binarysearch", "mid element is $midElement, target is $target")
                low = mid+1
                onPass(elementList.subList(low, high+1))
            }
        }
        onPass(emptyList())
    }
}