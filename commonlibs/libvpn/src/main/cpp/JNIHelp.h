//
// JNI 辅助函数
// Created by hsz on 17-6-22.
//

#ifndef LINESELECTORDEMO_JNIHELP_H
#define LINESELECTORDEMO_JNIHELP_H

#include <jni.h>
#include <string>
#include <stddef.h>

namespace ssl {

#define THROW_AND_LOG(env, className, format, ...) \
    do { \
        LOGE(TAG, format, ##__VA_ARGS__); \
        jniThrowException(env, (className), (format), ##__VA_ARGS__); \
    } while (false)

    /**
     * 定义 java/util/HashMap 常用类指针
     */
    struct JniHashMapConstants {
        static jclass mapClass;
        static jmethodID keySetMethodiId;
        static jmethodID getMethodId;
    };
    /**
     * 定义 java/util/Set 常用类指针
     */
    struct JniSetConstants {
        static jclass setClass;
        static jmethodID toArrayMethodId;
    };
/**
 * 仿照 Android 源码，用于缓存 Java 中的一些类和方法
 */
    struct JniConstants {
        static jclass StringClass;
        static jmethodID StringFromBytesConstructor; // String(byte[] bytes, String charset) 构造函数
        static jclass StringArrayClass;
    };


    struct JniStringConstants {
        static jclass stringClass;
        static jmethodID stringConstructor;
    };

    struct JniStringArrayConstants {
        static jclass stringArrayClass;
    };

    /**
     * 定义 java/io/StringWriter 类常用指针
     */
    struct StringBufferWriterConstants {
        static jclass stringBufferWriterClass;
        static jmethodID stringBufferWriterConstructor;
        static jmethodID toStringMethodId;
    };

    struct StringWriterConstants {
        static jclass stringWriterClass;
        static jmethodID stringWriterConstructor;
    };

/**
 * 初始化 JniConstants 以及其他变量
 * @param env
 * @return
 */
    bool initJniHelper(JavaVM *vm, JNIEnv *env);

/**
 * 抛出指定异常
 * @param env env
 * @param className 异常类名
 * @param format 格式字符串
 * @return 是否成功抛出
 */
    bool jniThrowException(JNIEnv *env, const char *className, const char *format, ...);

/**
 * 获取指定异常的调用栈
 * @param env
 * @param exception 异常
 * @param result 结果
 * @return 是否成功获取
 */
    bool jniGetStackTrace(JNIEnv *env, jthrowable exception, std::string& result);

/**
 * 转换指定 char* 的数据为 Java String
 * 因为直接调用 env->NewStringUTF() 在遇到无法识别的字节时会抛出异常
 * @param env
 * @param data 数据
 * @param size 数据大小
 * @return NULL 表示失败
 */
    jstring jniNewStringUTF(JNIEnv *env, const char *data, size_t size);

/**
 * 转换 std::string 为 Java String
 * @param env
 * @param str 待转换字符串
 * @return NULL 表示失败
 */
    inline jstring jniNewStringUTF(JNIEnv *env, const std::string& str) {
        return jniNewStringUTF(env, str.data(), str.size());
    }

    int initHashMap(JNIEnv *env);
    int initSet(JNIEnv *env);
    int initString(JNIEnv *env);
    int initStringArray(JNIEnv *env);

} // namespace ssl
#endif //LINESELECTORDEMO_JNIHELP_H
