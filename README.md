<<<<<<< HEAD
# 利刃
**刃所及，愁千丝不可近**
**MVP快速开发框架，可拓展、可解耦成模块**



---
> # 自定义View
---

## 标题栏EdgeToolBarView


初始化:
        
```
TOOBARVIEWUTILS
    .GETINSTANCE()
    .SETBACKGROUNDCOLOR(EDGECOLORUTILS.GETCOLOR(THIS, R.COLOR.COLORACCENT)) //设置背景颜色
    .SETTEXTCOLOR(EDGECOLORUTILS.GETCOLOR(THIS, R.COLOR.COLORPRIMARY)) //设置文字颜色
    .SETIMGBACK(R.DRAWABLE.IC_BACK) //设置返回图片
    .SETLINECOLOR(EDGECOLORUTILS.GETCOLOR(THIS, R.COLOR.COLORPRIMARYDARK)) //设置底部线条颜色
    .SETTEXTCOLOR(EDGECOLORUTILS.GETCOLOR(THIS,R.COLOR.COLORACCENT)); //设置文字颜色
```

---
> # 工具类
---

## 日志打印工具:EdgeLog
初始化:

```
EdgeLogConfig
        .build()
        .setType(EdgeLogType.DEBUG) //设置日志级别
        .setLogName("EDGE") //设置日志显示名
        .setEndFlag("END") //设置日志结束语
        .setLength(150) //设置日志一行最多支持字数
        .setMarginLines(2); //设置日志上下间隔行数
```

使用示范:

```
EdgeLog.show(javaClass,"内容")
```

功能

```
show,v,i,d,w,e
```
=======
# AndroidEDGE
利刃所至，愁千丝不可及
>>>>>>> 522256fb79abf7866d31be21fcccbebab4ff035a
