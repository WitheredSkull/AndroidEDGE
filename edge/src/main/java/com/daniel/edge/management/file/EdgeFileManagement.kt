package com.daniel.edge.management.file

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import com.daniel.edge.config.Edge
import com.daniel.edge.management.file.model.EdgeBaseFileProperty
import com.daniel.edge.utils.exception.EdgeException
import java.io.*
import java.util.*

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   获取
 */
object EdgeFileManagement {

    /**
     * 获取储存路径
     */
    @JvmStatic
    fun getExternalStorageDirectory():String{
        return Environment.getExternalStorageDirectory().absolutePath
    }

    //读取data/data/包名目录
    @JvmStatic
    fun getPackagePath(): String {
        return Edge.CONTEXT.cacheDir.parent
    }

    //读取SP目录
    @JvmStatic
    fun getSharedPrefsPath(): String {
        return getPackagePath() + "/shared_prefs/"
    }

    //读取目录下所有的文件夹以及文件
    @JvmStatic
    fun findDirectoryOnFile(
        directory: String,
        models: ArrayList<EdgeBaseFileProperty>
    ): ArrayList<EdgeBaseFileProperty> {
        var file = File(directory)
        if (file.exists()) {
            var fileBean = EdgeBaseFileProperty()
            fileBean.fileName = file.name
            fileBean.filePath = file.absolutePath

            if (file.isDirectory()) {
                //当是文件夹的时候
                fileBean.isDirectory = true
                models.add(fileBean)
                if (file.listFiles() != null && file.listFiles().size > 0)
                    file.listFiles().forEach {
                        findDirectoryOnFile(it.absolutePath, models)
                    }
            } else {
                //当是文件的时候
                fileBean.isDirectory = false
                models.add(fileBean)
            }
        }
        return models
    }

    //创建文件，会自动删除之前存在的文件
    @JvmStatic
    fun createFile(path:String):File{
        var file = File(path)
        if (file.exists()){
            file.delete()
        }else{
            file.parentFile.mkdirs()
        }
        file.createNewFile()
        return file
    }

    //删除目录下所有的东西
    @JvmStatic
    fun deleteDirectoryAllData(directory: String) {
        var list = arrayListOf<EdgeBaseFileProperty>()
        EdgeFileManagement.findDirectoryOnFile(directory, list);
        Collections.reverse(list)
        list.forEach {
            var file = File(it.filePath)
            if (file.exists()) {
                if (!file.delete()) {
                    if (!file.delete()) {
                        file.delete()
                    }
                }
            }
        }
    }

    //String转File.text
    //append表示是否使用追加模式
    @JvmStatic
    fun writeTextFileFileUseOutputStream(filePath: String, content: String, append: Boolean): Boolean {
        val file = File(filePath)
        if (file.exists() && !append) {
            throw EdgeException("已存在文件")
            return false
        } else {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            var fos = FileOutputStream(file, append)
            fos.write(content.toByteArray())
            fos.flush()
            fos.close()
            return true
        }
    }

    //String转File.text
    //append表示是否使用追加模式
    @JvmStatic
    fun writeTextFileFileUseBufferWriter(filePath: String, content: String, append: Boolean): Boolean {
        var file = File(filePath)
        if (file.exists() && !append) {
            throw EdgeException("已存在文件")
            return false
        } else {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            var fos = FileOutputStream(file, append)
            var osw = OutputStreamWriter(fos)
            var bw = BufferedWriter(osw)
            bw.write(content)
            bw.flush()
            bw.close()
            osw.close()
            fos.close()
            return true
        }
    }

    //String转File.text
    //append表示是否使用追加模式
    @JvmStatic
    fun writeTextFileFileUseFileWrite(filePath: String, content: String, append: Boolean): Boolean {
        var file = File(filePath)
        if (file.exists() && !append) {
            throw EdgeException("已存在文件")
            return false
        } else {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            var fw = FileWriter(filePath, append)
            fw.write(content)
            fw.flush()
            fw.close()
            return true
        }
    }

    //String转File.text
    //append表示是否使用追加模式
    @JvmStatic
    fun writeTextFileFileUseRandomAccessFile(filePath: String, content: String, append: Boolean): Boolean {
        var file = File(filePath)
        if (file.exists() && !append) {
            throw EdgeException("已存在文件")
            return false
        } else {
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            var raf = RandomAccessFile(filePath, "rw")
            var fileLength = raf.length()
            if (append) {
                raf.seek(fileLength)
                raf.writeUTF(content)
            }
            raf.close()
            return true
        }
    }

    //File转String
    @JvmStatic
    fun readTextFromFileUseBufferedReader(filePath: String): String? {
        var file = File(filePath)
        if (!file.exists()) {
            try {
                throw FileNotFoundException("文件未被找到")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                return null
            }
        }
        var fis = FileInputStream(file)
        var br = fis.bufferedReader()
        var content = StringBuffer()
        var s: String? = br.readLine()
        while (!TextUtils.isEmpty(s)) {
            content.append("$s\n")
            s = br.readLine()
        }
        br.close()
        fis.close()
        return content.toString()
    }

    //File转String,支持自定义缓冲大小，通常1024字节
    @JvmStatic
    fun readTextFromFileUseBytes(filePath: String, bufferSize: Int): String? {
        var file = File(filePath)
        if (!file.exists()) {
            try {
                throw FileNotFoundException("文件未被找到")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                return null
            }
        }
        var fis = FileInputStream(file)
        var bufferByte = ByteArray(bufferSize)
        var len = 0
        var baos = ByteArrayOutputStream()
        len = fis.read(bufferByte)
        while (len != -1) {
            baos.write(bufferByte, 0, len)
            len = fis.read(bufferByte)
        }
        var contentByte = baos.toByteArray()
        baos.close()
        fis.close()
        return String(contentByte)
    }

    //File转String,支持自定义编码
    @JvmStatic
    fun readTextFromFileUseChar(filePath: String, codingMode: String): String? {
        var file = File(filePath)
        if (!file.exists()) {
            try {
                throw FileNotFoundException("文件未被找到")
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                return null
            }
        }
        var fis = FileInputStream(file)
        var isr = InputStreamReader(fis, codingMode)
        var contentC = CharArray(fis.available())
        isr.read(contentC)
        return String(contentC)
    }

    //Bitmap保存成文件
    @JvmStatic
    fun saveBitmapToLocal(filePath: String,bitmap: Bitmap,format: Bitmap.CompressFormat){
        var file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        var fos = FileOutputStream(file)
        bitmap.compress(format,100,fos)
        fos.flush()
        fos.close()
    }

    //文件读取成Bitma
    @JvmStatic
    fun readLocalToBitmap(filePath: String):Bitmap?{
        var file = File(filePath)
        if (!file.exists()){
            return null
        }
        return BitmapFactory.decodeFile(filePath)
    }
}