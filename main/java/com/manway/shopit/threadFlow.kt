package com.manway.shopit

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

public class threadFlow:ViewModel(){

    //   val threadflow= viewModel<threadFlow>()
    val countFlow= flow<Int> {
        val start=0
        var current=start
        emit(start)
        while (true){
          delay(1000L)
          current++
          emit(current)
        }
    }




}