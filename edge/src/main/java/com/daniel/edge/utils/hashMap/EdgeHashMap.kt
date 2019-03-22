package com.daniel.edge.utils.hashMap

class EdgeHashMap {
    var hashMapFromString = hashMapOf<String, Any>()

    companion object {
        fun getInstance() = Instance.INSTANCE
    }

    private object Instance {
        val INSTANCE = EdgeHashMap()
    }

    fun put(type: String, key: String, value: Any) {
        hashMapFromString.put("$type+$key", value)
    }

    fun <T : Any> getValue(type: String, key: String, default: T?): T? {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            hashMapFromString.getOrDefault("$type+$key", default) as T
//        } else {
        var value = hashMapFromString.get("$type+$key")
        if (value != null) {
            return value as T
        } else {
            return default
        }
//        }
    }
}