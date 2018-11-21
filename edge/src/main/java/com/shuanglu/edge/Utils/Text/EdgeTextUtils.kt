package com.daniel.edge.Utils.Text

import android.service.autofill.FieldClassification
import android.text.TextUtils
import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

// Create Time 2018/11/1
// Create Author Daniel 
object EdgeTextUtils {
    fun formatJson(string: String): String {
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

    fun addSpace(num: Int): String {
        var spaceString = StringBuffer()
        for (item in 1..num) {
            spaceString.append(" ")
        }
        return spaceString.toString()
    }

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
            if (!isInsert && num - 1 == offset || num +1 == offset || num == offset){
                newString.append(insertString)
                isInsert = true
            }
        }
        return newString.toString();
    }

    fun isChinese(s: String): Boolean {
        val p_str = Pattern.compile("[\\u4e00-\\u9fa5]+")
        val m = p_str.matcher(s)
        return if (m.find() && m.group(0) == s) {
            true
        } else false
    }
}