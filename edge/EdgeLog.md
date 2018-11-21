# 功能模块：日志打印_EdgeLog

**简要描述：**
> 1.美化日志输出并提高阅读质量
2.版本发布时可以选择发布来取消Log显示
3.支持Json格式化

**主要类名：**
```
EdgeLog，EdgeLogConfig
```

**初始化：**
```
EdgeLogConfig
            .build()
            .setType(EdgeLogType.DEBUG)//设置日志级别
            .setLogName("EDGE")//设置日志TAG
            .setHeadText("佛祖保佑，永无BUG//  ")//设置日志头
            .setEndFlag(" END")//设置日志尾部显示
            .setLength(150)//设置日志单行最大数量显示
            .setMarginLines(0)//设置日志上下间距行数
            .setAutoReleaseCloseLog(true)//智能Release关闭日志功能
```

**方法：**
EdgeLog

|方法|返回值|说明|
|:----    |-----   |-----   |
|show(clazz: Class<*>?, message: String?) |void|按照初始化的日志级别显示|
|showJson(clazz: Class<*>?, message: String?) |void|格式化JSON显示|
|v(clazz: Class<*>?, message: String?) |void|VERBOSE的显示|
|d(clazz: Class<*>?, message: String?) |void|DEBUG的显示|
|i(clazz: Class<*>?, message: String?) |void|INFO的显示|
|w(clazz: Class<*>?, message: String?) |void|WARN的显示|
|fun normal(type: EdgeLogType, clazz: Class<*>?, message: String?) |void|正常的显示|

 **使用示例**
```
EdgeLog.showJson(javaClass,"{\"data\":[{\"id\":6,\"link\":\"\",\"name\":\"面试\",\"order\":1,\"visible\":1},{\"id\":9,\"link\":\"\",\"name\":\"Studio3\",\"order\":1,\"visible\":1},{\"id\":5,\"link\":\"\",\"name\":\"动画\",\"order\":2,\"visible\":1},{\"id\":1,\"link\":\"\",\"name\":\"自定义View\",\"order\":3,\"visible\":1},{\"id\":2,\"link\":\"\",\"name\":\"性能优化 速度\",\"order\":4,\"visible\":1},{\"id\":3,\"link\":\"\",\"name\":\"gradle\",\"order\":5,\"visible\":1},{\"id\":4,\"link\":\"\",\"name\":\"Camera 相机\",\"order\":6,\"visible\":1},{\"id\":7,\"link\":\"\",\"name\":\"代码混淆 安全\",\"order\":7,\"visible\":1},{\"id\":8,\"link\":\"\",\"name\":\"逆向 加固\",\"order\":8,\"visible\":1}],\"errorCode\":0,\"errorMsg\":\"\"}")

EdgeLog.show(javaClass,"正常显示")
EdgeLog.d(javaClass,"正常显示")
```


 **备注**
- XXX欢迎使用ShowDoc！