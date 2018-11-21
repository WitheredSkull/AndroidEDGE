# 功能模块：Activities管理器_EdgeActivityManagement

**简要描述：**
> 管理App正在运行的Activity

**主要类名：**
- EdgeActivityManagement

**初始化：**
  ```
EdgeManager.initEdge(application)
	.initActivityManagement()
```

**方法：**

|方法|返回值|说明|
|:----    |-----   |-----   |
|add |void|添加Activity|
|remove |void|移除Activity|
|finishOnLastFew(num) |void|移除最后多少个Activities|
|finishOnPosition(position) |void|移除指定位置的Activity|
|finishOnSimpleName |void|移除指定名字的Activity|
|finishOnClass |void|移除指定Class的Activity|
|findTopActivity |void|获取Top Activity|

 **使用示例**

```
EdgeActivityManagement.getInstance().add(activity)
EdgeActivityManagement.getInstance().remove(activity)
EdgeActivityManagement.getInstance().finishOnClass(Activity::class.java)
EdgeActivityManagement.getInstance().finishOnLastFew(5)
EdgeActivityManagement.getInstance().finishOnPosition(0)
EdgeActivityManagement.getInstance().finishOnSimpleName("activity")
var topActivity = EdgeActivityManagement.getInstance().findTopActivity()
```


 **备注**
- 已经初始化之后尽量不要手动去调用add和remove方法