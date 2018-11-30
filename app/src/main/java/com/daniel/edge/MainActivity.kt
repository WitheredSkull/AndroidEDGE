package com.daniel.edge

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.os.EnvironmentCompat
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.daniel.edge.Utils.Log.EdgeLog
import com.shuanglu.edge.Management.Download.EdgeDownManagement
import com.shuanglu.edge.Management.File.EdgeFileManagement
import com.shuanglu.edge.Management.Fragment.EdgeFragmentManagement
import com.shuanglu.edge.Management.Permission.EdgePermissionManagement
import com.shuanglu.edge.Management.Permission.OnEdgePermissionCallBack
import com.shuanglu.edge.Utils.Toast.EdgeToastUtils
import com.shuanglu.edge.Utils.Toast.Model.EdgeToastConfig
import com.shuanglu.edge.View.Banner.TextBanner.Model.TextBannerAdapter
import com.shuanglu.edge.View.Banner.TextBanner.View.TextBannerView


class MainActivity : AppCompatActivity() {
    var tv: TextView? = null;
    lateinit var tbv: TextBannerView<String>
    lateinit var edgePermissionManagement: EdgePermissionManagement
    lateinit var fragment:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        EdgeSharePreferencesUtils.getSP("appConfig").edit().putString("open","是的").commit()
//        EdgeLog.show(javaClass,"===="+EdgeSharePreferencesUtils.getSPProperty<Long>("appConfig","xxx",Long))
//        EdgeSharePreferencesUtils.setSPProperty("appConfig","age",1)
//        EdgeLog.show(javaClass,"===="+EdgeSharePreferencesUtils.getSPProperty<Int>("appConfig","age",Int))
//        EdgeSharePreferencesUtils.clearAppSP()

        tv = findViewById(R.id.tv)
//        EdgeFileManagement.deleteDirectoryAllData(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt")
//        EdgeFileManagement.writeTextFileFileUseBufferWriter(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","测试",true)
//        tv!!.text = EdgeFileManagement.readTextFromFileUseChar(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","utf-8")
        var ani = AnimationUtils.loadAnimation(this, R.anim.text)

//        tv!!.startAnimation(ani)
        var aniamtion0 = TranslateAnimation(0f, 0f, 0f, -300f)
        var animator = ObjectAnimator.ofFloat(tv!!, "translationY", 0f, -100f)
        animator.duration = 1500
        animator.start()
        aniamtion0.duration = 1500
        aniamtion0.interpolator = DecelerateInterpolator()
        aniamtion0.fillAfter = true
//        aniamtion0.fillBefore = true
//        aniamtion0.isFillEnabled = true
//        tv!!.animation = aniamtion0
//        aniamtion0.start()


//        var d = AppCompatResources.getDrawable(this,R.drawable.ic_add)
//        var b = d as BitmapDrawable
//        EdgeFileManagement.saveBitmapToLocal(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.png",b.bitmap,Bitmap.CompressFormat.PNG)
//        var bitmap = EdgeFileManagement.readLocalToBitmap(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.png")
//        var bitmap2 = bitmap
//        bitmap2 = EdgeApplicationManagement.appIcon("com.shuanglu.ibc")
//        var b3 = bitmap2
//        var a = EdgeApplicationManagement.allApplication()
//        var a2= a
//        EdgeLog.show(javaClass,EdgeApplicationManagement.appName("com.shuanglu.ibc"))

        tbv = findViewById(R.id.tbv)
        var list2 = arrayListOf<String>()
        list2.add("错误")
        list2.add("正确")
        tbv.adapter = object : TextBannerAdapter<String>(list2) {
            override fun getItem(item: String?, position: Int): View {
                var view = View.inflate(this@MainActivity, R.layout.text_item, null)
                view.findViewById<TextView>(R.id.t).text = item
                return view
            }
        }
        tbv.startRoll()

        var list = arrayListOf<String>()
        for (i in 0..4) {
            list.add("第${i}个")
        }
        edgePermissionManagement = EdgePermissionManagement()

            .requestPackageNeedPermission()

            .setCallBack(object : OnEdgePermissionCallBack {

                override fun onRequestPermissionSuccess() {
                }

                override fun onRequestPermissionFailure(permissions: ArrayList<String>) {
                    permissions.forEach {
                        EdgeLog.show(javaClass, "失败的" + permissions)
                    }
                }

            })

            .build(this)
        EdgeDownManagement.getInstance().down(
            Environment.getExternalStorageDirectory().absolutePath + "/",
            "https://android-1257046655.cos.ap-hongkong.myqcloud.com/Wandoujia_363640_web_seo_baidu_homepage.apk"
        )
        EdgeToastUtils.getInstance().show("弹出")

        fragment = findViewById(R.id.fragment)
        var s  = "第一个"


    }


    override fun onResume() {
        super.onResume()

        var fm = EdgeFragmentManagement(supportFragmentManager)
        fm.introductionFragment(DemoFragment::class.java)
        fm.build(R.id.fragment)
        fm.show(0)
    }
    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        edgePermissionManagement.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    var flag = true
    fun click(v: View) {
        if (flag) {
            flag = false
            EdgeDownManagement.getInstance().pause()
        } else {
            flag = true
            EdgeDownManagement.getInstance().down(
                Environment.getExternalStorageDirectory().absolutePath + "/",
                "https://android-1257046655.cos.ap-hongkong.myqcloud.com/Wandoujia_363640_web_seo_baidu_homepage.apk"
            )
        }
    }
}
