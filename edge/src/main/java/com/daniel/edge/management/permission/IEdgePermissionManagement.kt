package com.daniel.edge.management.permission

interface IEdgePermissionManagement {
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>?,
        grantResults: IntArray?
    )

    fun setOnCallBack(onEdgePermissionCallBack: OnEdgePermissionCallBack):EdgePermissionManagement

    fun requestPermission(vararg permission: String):EdgePermissionManagement

    fun requestPackageNeedPermission():EdgePermissionManagement

    fun build()
}