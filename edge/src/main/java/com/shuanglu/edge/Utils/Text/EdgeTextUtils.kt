package com.daniel.edge.Utils.Text

import android.service.autofill.FieldClassification
import android.text.TextUtils
import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeTextUtils {
    inline fun formatJson(string: String): String {
        //替换可能导致出错的字符串
        string.replace("{\n", "{")
        string.replace("\n{", "{")
        string.replace("\n}", "}")
        string.replace("}\n", "}")
        string.replace("[\n", "[")
        string.replace("\n[", "[")
        string.replace("\n]", "]")
        string.replace("]\n", "]")
        string.replace(",\n", ",")
        string.replace("\n,", ",")
        var formatString = StringBuffer();
        var spaceNum: Int = 0
        for (char: Char in string) {
            when (char.toString()) {
                "{" -> {
                    ++spaceNum
                    formatString.append("{\n${addSpace(spaceNum)}")
                }
                "[" -> formatString.append("[\n")
                "]" -> formatString.append("\n]")
                "}" -> {
                    --spaceNum
                    formatString.append("\n}${addSpace(spaceNum)}")
                }
                "," -> formatString.append(",\n${addSpace(spaceNum)}")
                else -> formatString.append(char)
            }
        }
        return formatString.toString()
    }

    /**
     * 生成空格
     * @param num 生成空格数量
     */
    fun addSpace(num: Int): String {
        var spaceString = StringBuffer()
        for (item in 1..num) {
            spaceString.append(" ")
        }
        return spaceString.toString()
    }

    /**
     * @param s
     * @return 返回实际的带中文字符串长度
     */
    fun textNum(s: String): Long {
        var num = 0L
        s.forEach {
            if (isChinese(it + "")) {
                num += 2
            } else {
                ++num
            }
        }
        return num
    }

    /**
     * 插入中文字符串
     * @param oldString 原先的字符串
     * @param offset 需要插入的位置
     * @param insertString 插入的中文字符
     * @return 返回新的字符串
     */
    fun insertChinese(oldString: String, offset: Int, insertString: String): String {
        var newString = StringBuffer();
        var num = 0
        var isInsert = false
        oldString.forEach {
            if (isChinese(it + "")) {
                num += 2
            } else {
                ++num
            }
            newString.append(it)
            if (!isInsert && num - 1 == offset || num + 1 == offset || num == offset) {
                newString.append(insertString)
                isInsert = true
            }
        }
        return newString.toString();
    }

    /**
     * @param s 传入字符串
     * @return 判断是否是中文字符串
     */
    fun isChinese(s: String): Boolean {
        val p_str = Pattern.compile("[\\u4e00-\\u9fa5]+")
        val m = p_str.matcher(s)
        return m.find() && m.group(0) == s
    }

    /**
     * 拼接字符串
     * @param arg 传入不限量的字符串进行拼接
     */
    fun splicing(vararg arg: String): String {
        var sb = StringBuffer()
        arg.forEach {
            sb.append(it)
        }
        return sb.toString()
    }
}