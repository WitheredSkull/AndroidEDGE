package com.qiang.keyboard.model

class EventBusFunction {
    data class SwitchButton(val tag:String,val isEnable:Boolean)
    data class Refresh(val tag:String)
}