package com.qiang.keyboard.model.data

import com.daniel.edge.utils.log.EdgeLog
import com.daniel.edge.utils.sharePreference.EdgeSharePreferencesUtils
import com.daniel.edge.utils.text.EdgeTextUtils
import com.qiang.keyboard.model.entity.Device
import com.qiang.keyboard.model.entity.MemberResult

object AccountData {
    val FILE_NAME = "account"
    //以下都是用户信息
    val PROPERTY_ACCOUNT = "account"
    val PROPERTY_PWD = "pwd"
    val PROPERTY_SESSION_ID = "sessionId"
    val PROPERTY_MOBILE = "mobile"
    val PROPERTY_USER_NAME = "username"
    val PROPERTY_NICK_NAME = "nickname"
    val PROPERTY_MEMBER_ID = "memberId"
    val PROPERTY_ADDRESS = "address"
    val PROPERTY_AVATAR = "avatar"
    val PROPERTY_BIRTHDAY = "birthday"
    val PROPERTY_DESCRIPTION = "description"
    val PROPERTY_EMAIL = "email"
    val PROPERTY_IS_SHOP = "isshop"
    val PROPERTY_SEX = "sex"
    val PROPERTY_STATUS = "status"

    //以下都是设备信息
    val PROPERTY_DEVICE_DEVICE_ID = "deviceId"
    val PROPERTY_DEVICE_MEMBER_ID = "memberId"
    val PROPERTY_DEVICE_NICK_NAME = "nickname"
    val PROPERTY_DEVICE_IS_ACTIVE = "isActive"
    val PROPERTY_DEVICE_SITE_ID = "siteId"
    val PROPERTY_DEVICE_TYPE_ID = "typeId"
    val PROPERTY_DEVICE_STATUS = "status"

    fun clear() {
        EdgeSharePreferencesUtils.clearSP(FILE_NAME)
    }

    /**
     * 是否登录
     */
    fun isLogin(): Boolean = !(EdgeTextUtils.isEmpty(getAccountInfo()[0])
            || EdgeTextUtils.isEmpty(getAccountInfo()[1])
            || EdgeTextUtils.isEmpty(
        getAccountData(PROPERTY_SESSION_ID, "")
    )
            )

    /**
     * 获取账户密码
     */
    fun getAccountInfo(): Array<String> {
        val account = EdgeSharePreferencesUtils.getSPProperty<String>(FILE_NAME, PROPERTY_ACCOUNT, "")
        val pwd = EdgeSharePreferencesUtils.getSPProperty<String>(FILE_NAME, PROPERTY_PWD, "")
        return arrayOf(account, pwd)
    }

    fun getSession(): String = getAccountData(PROPERTY_SESSION_ID, "")

    /**
     * 获取单个用用户信息字段
     */
    fun <T> getAccountData(property: String, default: Any): T {
        return EdgeSharePreferencesUtils.getSPProperty(FILE_NAME, property, default)
    }

    /**
     * 设置账户密码
     */
    fun setAccountInfo(account: String? = null, pwd: String? = null) {
        account?.let {
            EdgeSharePreferencesUtils.setSPProperty(FILE_NAME, PROPERTY_ACCOUNT, account)
        }
        pwd?.let {
            EdgeSharePreferencesUtils.setSPProperty(FILE_NAME, PROPERTY_PWD, pwd)
        }
    }

    /**
     * 设置用户信息
     */
    fun setMemberInfo(model: MemberResult) {
        val sp = EdgeSharePreferencesUtils.getSP(FILE_NAME)
        sp.edit().putString(PROPERTY_ADDRESS, model.Address)
            .putString(PROPERTY_AVATAR, model.Avatar)
            .putString(PROPERTY_BIRTHDAY, model.Birthday)
            .putString(PROPERTY_DESCRIPTION, model.Description)
            .putString(PROPERTY_EMAIL, model.Email)
            .putInt(PROPERTY_IS_SHOP, model.IsShop)
            .putInt(PROPERTY_MEMBER_ID, model.MemberId)
            .putString(PROPERTY_MOBILE, model.Mobile)
            .putString(PROPERTY_NICK_NAME, model.Nickname)
            .putString(PROPERTY_SESSION_ID, model.SessionId)
            .putInt(PROPERTY_SEX, model.Sex)
            .putInt(PROPERTY_STATUS, model.Status)
            .putString(PROPERTY_USER_NAME, model.Username)
            .apply()
    }

    /**
     * 设置设备信息
     */
    fun setDeviceInfo(model: Device) {
        EdgeSharePreferencesUtils.setSPProperties(
            FILE_NAME,
            arrayOf(
                PROPERTY_DEVICE_DEVICE_ID,
                PROPERTY_DEVICE_MEMBER_ID,
                PROPERTY_DEVICE_NICK_NAME,
                PROPERTY_DEVICE_IS_ACTIVE,
                PROPERTY_DEVICE_SITE_ID,
                PROPERTY_DEVICE_TYPE_ID,
                PROPERTY_DEVICE_STATUS
            ),
            arrayOf(
                model.DeviceId,
                model.MemberId,
                model.Nickname,
                model.isActive,
                model.SiteId,
                model.TypeId,
                model.Status
            )
        )
    }
}