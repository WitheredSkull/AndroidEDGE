package com.daniel.edge.management.permission

interface IEdgePermissionManagement {
    fun onResult(_a:ArrayList<String>?)

    fun setCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack):EdgePermissionManagement

    fun requestPermission(vararg permission: String):EdgePermissionManagement

    fun requestPackageNeedPermission():EdgePermissionManagement

    fun build()
}