package com.example.storyapp.data

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdle {

    private const val RESOURCE ="GLOBAL"

    @JvmField
    val countingResource = CountingIdlingResource(RESOURCE)

    fun increment(){
        countingResource.increment()
    }

    fun decrement(){
        if (!countingResource.isIdleNow){
            countingResource.decrement()
        }
    }

}

inline fun <T> wrapEspressoIdle(function:()-> T):T {
    EspressoIdle.increment() //set busy
    return try {
        function()
    }
    finally {
        EspressoIdle.decrement() // set idle
    }
}