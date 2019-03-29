package com.qiang.keyboard.presenter

interface KeyboardInterface {
    fun onInput(text:String)
    fun onDelete()
    fun onEnter()
    fun onSpace()
    fun onHideNumber(isHide:Boolean)
}