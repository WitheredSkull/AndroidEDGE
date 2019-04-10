package com.daniel.edgeDemo.utils

import java.io.Serializable

/**
 *  @see SingletonDemo 恶汉模式 (不管有没有使用过该单例都会被实例化，类似于写在Java的static的初始化方法块中)
 */
object SingletonDemo : Serializable {
    private fun readResolve(): Any {
        return SingletonDemo
    }
}

/**
 * @see SingletonDemo2 懒汉模式（根据需要实例化对象，并且有同步锁，是线程安全的）
 */
class SingletonDemo2 : Serializable {
    companion object {
        private var mInstance: SingletonDemo2? = null
            get() {
                return field ?: SingletonDemo2()
            }

        @Synchronized
        fun getInstance(): SingletonDemo2 {
            return requireNotNull(mInstance)
        }
    }

    //防止反序列化重新生成对象
    private fun readResolve(): Any {
        return SingletonDemo2.getInstance()
    }
}

/**
 * @see SingletonDemo3 Double check lock 同步锁的懒汉模式 DCL（不申请同步锁，
 * 如果单例已经被实例化了直接返回单例实例，但是可能存在线程安全的问题）
 */
class SingletonDemo3 {
    companion object {
        /**
         * @see JvmStatic 如果不加注解调用instance Java需要使用SingletonDemo3.Companion.getInstance()调用
         * @see lazy 使用lazy属性代理，并指定LazyThreadSafetyMode.SYNCHRONIZED 模式保证线程安全
         */
        @JvmStatic
        val instance: SingletonDemo3 by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SingletonDemo3()
        }
    }

    //防止反序列化重新生成对象
    private fun readResolve() = SingletonDemo3.instance

}

/**
 * @see SingletonDemo4 静态内部类单例（常用的一种）
 */
class SingletonDemo4 {
    companion object {
        @JvmStatic
        fun getInstance() = Instance.INSTANCE
    }

    object Instance {
        val INSTANCE = SingletonDemo4()
    }

    //防止反序列化重新生成对象
    private fun readResolve() = Instance.INSTANCE
}

/**
 * @see SingletonDemo5 枚举单例，在序列化枚举类型时会储存枚举类的引用和枚举常量名称，
 * 反序列化对象可以根据引用查找类型实例从而实现防止反序列化对象
 */
enum class SingletonDemo5 {
    INSTANCE;

    fun doSomeThing() {
        print("do Some Thing")
    }
}