package com.qiang.keyboard.view

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.daniel.edge.management.activity.EdgeActivityManagement
import com.daniel.edge.utils.system.EdgeSystemUtils
import com.daniel.edge.utils.text.EdgeTextUtils
import com.daniel.edge.utils.toast.EdgeToastUtils
import com.lzy.okgo.model.Response
import com.qiang.keyboard.R
import com.qiang.keyboard.model.data.AccountData
import com.qiang.keyboard.model.network.callBack.String64CallBack
import com.qiang.keyboard.model.network.request.DeviceRequest
import com.qiang.keyboard.utlis.VibrateUtlis
import com.qiang.keyboard.view.account.AccountActivity

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    override fun onListItemClick(l: ListView?, v: View, position: Int, id: Long) {
        when (position) {
            2 -> EdgeToastUtils.getInstance().show("打开主题")
            3 -> {
                AccountData.clear()
                EdgeActivityManagement.getInstance().exit()
                startActivity(Intent(this, AccountActivity::class.java))
            }
            else -> super.onListItemClick(l, v, position, id)
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || GeneralFragment::class.java.name == fragmentName
                || PersonageFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class GeneralFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)
            val prefVibration = findPreference("audio_config_vibration")
            if (VibrateUtlis.isSupport()) {
                prefVibration.summary = resources.getString(R.string.pref_vibration_support)
                prefVibration.isEnabled = true
            } else {
                prefVibration.summary = resources.getString(R.string.pref_vibration_nonsupport)
                prefVibration.isEnabled = false
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class PersonageFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_personage)
            setHasOptionsMenu(true)
            val prefPersonageNickName = findPreference("key_user_name")
            val nickName = AccountData.getAccountData<String>(
                AccountData.PROPERTY_NICK_NAME,
                getString(R.string.default_personage)
            )
            prefPersonageNickName.summary = nickName
            prefPersonageNickName.setDefaultValue(nickName)
            prefPersonageNickName.setOnPreferenceChangeListener { preference, newValue ->
                if (!EdgeTextUtils.isEmpty(newValue.toString())) {
                    DeviceRequest.setNickName(newValue as String,EdgeSystemUtils.getIMEI(), object : String64CallBack() {
                        override fun success(code: Int, status: Int, des: String, body: Response<String>) {
                            prefPersonageNickName.summary = newValue
                            prefPersonageNickName.setDefaultValue(newValue)
                        }

                        override fun error(code: Int, status: Int, des: String) {
                            super.error(code, status, des)
                            prefPersonageNickName.summary = nickName
                            prefPersonageNickName.setDefaultValue(nickName)
                        }
                    })
                    true
                } else {
                    prefPersonageNickName.setDefaultValue(nickName)
                    false
                }
            }
            val prefPersonageAlterPwd = findPreference("key_alter_pwd")
            prefPersonageAlterPwd.setOnPreferenceClickListener {
                val intent = Intent(activity,AccountActivity::class.java)
                intent.putExtra("type",1)
                startActivity(intent)
                true
            }
            prefPersonageAlterPwd.setOnPreferenceChangeListener { preference, newValue ->
                if (!EdgeTextUtils.isEmpty(newValue.toString())) {
                    DeviceRequest.setNickName(newValue as String,EdgeSystemUtils.getIMEI(), object : String64CallBack() {
                        override fun success(code: Int, status: Int, des: String, body: Response<String>) {
                            prefPersonageNickName.summary = newValue
                            prefPersonageNickName.setDefaultValue(newValue)
                        }

                        override fun error(code: Int, status: Int, des: String) {
                            super.error(code, status, des)
                            prefPersonageNickName.summary = nickName
                            prefPersonageNickName.setDefaultValue(nickName)
                        }
                    })
                    true
                } else {
                    prefPersonageNickName.setDefaultValue(nickName)
                    false
                }
            }
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
            return super.onOptionsItemSelected(item)
        }
    }

}
