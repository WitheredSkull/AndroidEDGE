package com.daniel.edge

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.content.res.AppCompatResources
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import com.daniel.edge.Utils.Log.EdgeLog
import com.shuanglu.edge.Management.Application.EdgeApplicationManagement
import com.shuanglu.edge.Management.File.EdgeFileManagement
import com.shuanglu.edge.Management.File.Model.EdgeBaseFileProperty
import com.shuanglu.edge.View.Banner.TextBanner.Model.TextBannerAdapter
import com.shuanglu.edge.View.Banner.TextBanner.View.TextBannerView
import com.paradoxie.autoscrolltextview.VerticalTextview



class MainActivity : AppCompatActivity() {
    var tv:TextView? = null;
    lateinit var tbv:TextBannerView<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        EdgeSharePreferencesUtils.getSP("appConfig").edit().putString("open","是的").commit()
//        EdgeLog.show(javaClass,"===="+EdgeSharePreferencesUtils.getSPProperty<Long>("appConfig","xxx",Long))
//        EdgeSharePreferencesUtils.setSPProperty("appConfig","age",1)
//        EdgeLog.show(javaClass,"===="+EdgeSharePreferencesUtils.getSPProperty<Int>("appConfig","age",Int))
//        EdgeSharePreferencesUtils.clearAppSP()

        tv = findViewById(R.id.tv)
        EdgeFileManagement.deleteDirectoryAllData(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt")
        EdgeFileManagement.writeTextFileFileUseBufferWriter(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","测试",true)
        tv!!.text = EdgeFileManagement.readTextFromFileUseChar(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","utf-8")
        var ani = AnimationUtils.loadAnimation(this,R.anim.text)

//        tv!!.startAnimation(ani)
        var aniamtion0 = TranslateAnimation(0f, 0f, 0f, -300f)
        var animator = ObjectAnimator.ofFloat(tv!!,"translationY",0f,-100f)
        animator.duration = 1500
        animator.start()
        aniamtion0.duration = 1500
        aniamtion0.interpolator = DecelerateInterpolator()
        aniamtion0.fillAfter = true
//        aniamtion0.fillBefore = true
//        aniamtion0.isFillEnabled = true
//        tv!!.animation = aniamtion0
//        aniamtion0.start()


        var d = AppCompatResources.getDrawable(this,R.drawable.ic_add)
        var b = d as BitmapDrawable
        EdgeFileManagement.saveBitmapToLocal(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.png",b.bitmap,Bitmap.CompressFormat.PNG)
        var bitmap = EdgeFileManagement.readLocalToBitmap(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.png")
        var bitmap2 = bitmap
        bitmap2 = EdgeApplicationManagement.appIcon("com.shuanglu.ibc")
        var b3 = bitmap2
        var a = EdgeApplicationManagement.allApplication()
        var a2= a
        EdgeLog.show(javaClass,EdgeApplicationManagement.appName("com.shuanglu.ibc"))

        tbv = findViewById(R.id.tbv)
        var list2 = arrayListOf<String>()
        list2.add("错误")
        list2.add("正确")
        tbv.adapter = object : TextBannerAdapter<String>(list2) {
            override fun getItem(item: String?, position: Int): View {
                var view = View.inflate(this@MainActivity,R.layout.text_item,null)
                view.findViewById<TextView>(R.id.t).text = item
                return view
            }
        }
        tbv.startRoll()

        var list = arrayListOf<String>()
        for (i in 0..4){
            list.add("第${i}个")
        }

        var text = findViewById<VerticalTextview>(R.id.text)
        text.setTextList(list)//加入显示内容,集合类型
        text.setText(26f, 5, Color.RED)//设置属性,具体跟踪源码
        text.setTextStillTime(3000)//设置停留时长间隔
        text.setAnimTime(2500)//设置进入和退出的时间间隔
        //对单条文字的点击监听
        text.setOnItemClickListener(VerticalTextview.OnItemClickListener {
            Toast.makeText(this,list.get(it),Toast.LENGTH_SHORT).show()
        })
        text.startAutoScroll()
    }

    fun click(v:View){
        Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show()
    }
}
