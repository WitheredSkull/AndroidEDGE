package com.qiang.keyboard.presenter

interface KeyboardInterface {
    fun onChart(text: String)
    //当为Input时会发送数据
    fun onInput(text: String)
    fun onBack()
    fun onDelete()
    fun onEnter()
    fun onSpace()
    fun onHideNumber(isHide: Boolean)
}