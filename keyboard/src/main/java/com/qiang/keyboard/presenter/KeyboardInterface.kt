package com.qiang.keyboard.presenter

interface KeyboardInterface {
    fun onSendText(text: String)
    fun onInput(text: String)
    fun onBack()
    fun onDelete()
    fun onEnter()
    fun onSpace()
    fun onHideNumber(isHide: Boolean)
}