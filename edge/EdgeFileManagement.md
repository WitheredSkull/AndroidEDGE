# 功能模块：File管理器_EdgeFileManagement

**简要描述：**
> File管理

**主要类名：**
```
EdgeFileManagement
```

**初始化：**
- null

**方法：**

|方法|返回值|说明|
|:----    |-----   |-----   |
|getSharedPrefsPath |String|返回的是SP的文件夹路径|
|getPackagePath |String|返回data/data/包名的路径|
|findDirectoryOnFile |ArrayList<EdgeBaseFileProperty>|返回所有的文件夹以及文件路径|
|deleteDirectoryAllData |void|删除指定目录下所有的文件和目录|
|writeTextFileFileUseOutputStream |boolean|写入文本到文件，使用OutputStream和ByteArray实现|
|writeTextFileFileUseBufferWriter |boolean|写入文本到文件，使用FileOutputStream、OutputStreamWriter和BufferedWriter实现|
|writeTextFileFileUseFileWrite |boolean|写入文本到文件，使用FileWriter实现|
|writeTextFileFileUseRandomAccessFile |boolean|写入文本到文件，使用RandomAccessFile实现|
|readTextFromFileUseBufferedReader |String|读取文件到文本，使用FileInputStream、bufferedReader实现|
|readTextFromFileUseBytes |String|读取文件到文本，使用FileInputStream、ByteArrayOutputStream实现|
|readTextFromFileUseChar |String|读取文件到文本，使用FileInputStream、InputStreamReader、CharArray实现|
|saveBitmapToLocal |void|写入图片到文件，如果有同名图片会自动删除后保存|
|readLocalToBitmap |void|读取文件到图片|



 **使用示例**
```
tv = findViewById(R.id.tv)
//删除文件       EdgeFileManagement.deleteDirectoryAllData(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt")
//写入文件
EdgeFileManagement.writeTextFileFileUseBufferWriter(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","测试",true)
//读取文件
tv!!.text = EdgeFileManagement.readTextFromFileUseChar(Environment.getExternalStorageDirectory().absolutePath+"/Download/test1.txt","utf-8")
```


 **备注**
- 当使用某些写入和读取时可能会出现空格读取为\0000的问题，日志无法打印，但是可以用TextView显示出正确的值