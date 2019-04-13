package com.daniel.edge.utils.cache

/**
 * @author Daniel
 * 通过类型和KEY确定值得缓存，可以减小普通key,value形式缓存引起的重复冲突问题
 */
class EdgeCacheHashMap {
    var hashMapFromString = hashMapOf<String, Any>()
    /**
     * @param type 缓存的类型
     * @param key 缓存类型对应的KEY
     * @param default 默认值
     * @return 获取缓存的值
     */
    fun <T : Any> getValue(type: String, key: String, default: T?): T? {
        val value = hashMapFromString.get("$type+$key")
        if (value != null) {
            return value as T
        } else {
            return default
        }
    }

    /**
     * @param type 缓存的类型
     * @param key 缓存类型对应的KEY
     * @param value 存入的值（不能为Null）
     */
    fun put(type: String, key: String, value: Any) {
        hashMapFromString.put("$type+$key", value)
    }

    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    private object Instance {
        val INSTANCE = EdgeCacheHashMap()
    }
}