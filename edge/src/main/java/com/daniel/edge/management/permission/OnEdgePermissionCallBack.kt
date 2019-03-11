package com.daniel.edge.management.permission

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 9:53
 * @Description:
 *
 */
interface OnEdgePermissionCallBack {
    fun onRequestPermissionSuccess()
    fun onRequestPermissionFailure(permissions:ArrayList<String>)
}