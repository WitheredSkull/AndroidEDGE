package com.daniel.edge.management.permission

interface IEdgePermissionManagement {
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>?,
        grantResults: IntArray?
    )
}