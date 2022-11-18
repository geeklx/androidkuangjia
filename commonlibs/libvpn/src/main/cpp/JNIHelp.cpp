//
// Created by hsz on 17-6-22.
//

#include "JNIHelp.h"

#include <stdio.h>
#include <stdarg.h>

#include "logging.h"
#include "ScopedLocalRef.h"

#define TAG "NativeHelper"

namespace ssl {

#define RETURN_FALSE_IF_NULL(expr) \
	if ((expr) == NULL) { \
		LOGE(TAG, "%s is NULL", #expr); \
		return false; \
	}

#define RETURN_FALSE_IF_NOT_ZERO(expr) \
	if ((expr) != 0) { \
		LOGE(TAG, "%s is NULL", #expr); \
		return false; \
	}

    static jstring sUtf8Charset = NULL;

// 定义
    jclass JniConstants::StringClass;
    jmethodID JniConstants::StringFromBytesConstructor;
    jclass JniConstants::StringArrayClass;

    bool initJniHelper(JavaVM *vm, JNIEnv *env) {
        int ret = -1;
        if (!env) {
            return false;
        }

        JniConstants::StringClass = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("java/lang/String")));
        RETURN_FALSE_IF_NULL(JniConstants::StringClass);
        JniConstants::StringFromBytesConstructor = env->GetMethodID(JniConstants::StringClass, "<init>", "([BLjava/lang/String;)V");
        RETURN_FALSE_IF_NULL(JniConstants::StringFromBytesConstructor);
        JniConstants::StringArrayClass = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("[Ljava/lang/String;")));
        RETURN_FALSE_IF_NULL(JniConstants::StringArrayClass);

        sUtf8Charset = reinterpret_cast<jstring>(env->NewGlobalRef(env->NewStringUTF("UTF-8")));
        RETURN_FALSE_IF_NULL(sUtf8Charset)

        ret = initHashMap(env);
        RETURN_FALSE_IF_NOT_ZERO(ret);

        ret = initSet(env);
        RETURN_FALSE_IF_NOT_ZERO(ret);

        ret = initString(env);
        RETURN_FALSE_IF_NOT_ZERO(ret);

        ret = initStringArray(env);
        RETURN_FALSE_IF_NOT_ZERO(ret);

        return true;
    }

// 定义
    jclass JniHashMapConstants::mapClass;
    jmethodID JniHashMapConstants::keySetMethodiId;
    jmethodID JniHashMapConstants::getMethodId;

    int initHashMap(JNIEnv *env)
    {
        // 初始化map相关函数
        jclass mapClass = env->FindClass("java/util/HashMap");
        if (mapClass == NULL) {
            return -1;
        }
        JniHashMapConstants::mapClass = reinterpret_cast<jclass>(env->NewGlobalRef(mapClass));

        // 初始化keySet的函数指针
        JniHashMapConstants::keySetMethodiId = env->GetMethodID(JniHashMapConstants::mapClass, "keySet",
                                                                "()Ljava/util/Set;");
        JniHashMapConstants::getMethodId = env->GetMethodID(JniHashMapConstants::mapClass, "get",
                                                            "(Ljava/lang/Object;)Ljava/lang/Object;");
        return 0;
    }

    // 定义
    jclass JniSetConstants::setClass;
    jmethodID JniSetConstants::toArrayMethodId;

    int initSet(JNIEnv *env)
    {
        // 初始化map相关函数
        jclass setClass = env->FindClass("java/util/Set");
        if (setClass == NULL) {
            return -1;
        }
        JniSetConstants::setClass = reinterpret_cast<jclass>(env->NewGlobalRef(setClass));

        // 初始化keySet的函数指针
        JniSetConstants::toArrayMethodId = env->GetMethodID(JniSetConstants::setClass, "toArray", "()[Ljava/lang/Object;");
        if(JniSetConstants::toArrayMethodId == NULL){
            return -1;
        }
        return 0;
    }

    // 定义
    jclass JniStringConstants::stringClass;
    jmethodID JniStringConstants::stringConstructor;

    int initString(JNIEnv *env)
    {
        // 初始化String类相关函数
        jclass stringClass = env->FindClass("java/lang/String");
        if (stringClass == NULL) {
            return -1;
        }
        JniStringConstants::stringClass = reinterpret_cast<jclass>(env->NewGlobalRef(stringClass));

        // 初始化String构造函数
        JniStringConstants::stringConstructor =  env->GetMethodID(JniStringConstants::stringClass, "<init>", "([BLjava/lang/String;)V");
        if(JniStringConstants::stringConstructor== NULL){
            return -1;
        }
        return 0;
    }

    // 定义
    jclass JniStringArrayConstants::stringArrayClass;
    int initStringArray(JNIEnv *env)
    {
        // 初始化String类相关函数
        jclass stringArrayClass = env->FindClass("[Ljava/lang/String;");
        if (stringArrayClass == NULL) {
            return -1;
        }
        JniStringArrayConstants::stringArrayClass = reinterpret_cast<jclass>(env->NewGlobalRef(
                stringArrayClass));
        if(JniStringArrayConstants::stringArrayClass== NULL){
            return -1;
        }
        return 0;
    }


    bool jniThrowException(JNIEnv *env, const char *className, const char *format, ...) {
        char msg[256] = {0};
        va_list args;
        va_start(args, format);
        int ret = vsnprintf(msg, sizeof(msg), format, args);
        va_end(args);
        if (ret < 0) {
            LOGE(TAG, "vsnprintf failed");
            return false;
        }

        jclass clazz = env->FindClass(className);
        if (clazz == NULL) {
            LOGE(TAG, "Unable to find exception class %s", className);
            return false;
        }


        if (env->ThrowNew(clazz, msg) != JNI_OK) {
            LOGE(TAG, "Failed throwing '%s' '%s'", className, msg);
            return false;
        }

        return true;
    }

    bool jniGetStackTrace(JNIEnv *env, jthrowable exception, std::string& result) {
        if (!env || !exception) {
            return false;
        }

        ScopedLocalRef<jclass> stringWriterClass(*env, env->FindClass("java/io/StringWriter"));
        if (stringWriterClass.get() == NULL) {
            return false;
        }

        jmethodID stringWriterCtor = env->GetMethodID(stringWriterClass.get(), "<init>", "()V");
        jmethodID stringWriterToStringMethod =
                env->GetMethodID(stringWriterClass.get(), "toString", "()Ljava/lang/String;");

        ScopedLocalRef<jclass> printWriterClass(*env, env->FindClass("java/io/PrintWriter"));
        if (printWriterClass.get() == NULL) {
            return false;
        }

        jmethodID printWriterCtor =
                env->GetMethodID(printWriterClass.get(), "<init>", "(Ljava/io/Writer;)V");

        ScopedLocalRef<jobject> stringWriter(*env,
                                             env->NewObject(stringWriterClass.get(), stringWriterCtor));
        if (stringWriter.get() == NULL) {
            return false;
        }

        ScopedLocalRef<jobject> printWriter(*env,
                                            env->NewObject(printWriterClass.get(), printWriterCtor,
                                                           stringWriter.get()));
        if (printWriter.get() == NULL) {
            return false;
        }

        ScopedLocalRef<jclass> exceptionClass(*env, env->GetObjectClass(exception)); // can't fail
        jmethodID printStackTraceMethod =
                env->GetMethodID(exceptionClass.get(), "printStackTrace", "(Ljava/io/PrintWriter;)V");
        env->CallVoidMethod(exception, printStackTraceMethod, printWriter.get());

        if (env->ExceptionCheck()) {
            return false;
        }

        ScopedLocalRef<jstring> messageStr(*env,
                                           (jstring) env->CallObjectMethod(stringWriter.get(),
                                                                           stringWriterToStringMethod));
        if (messageStr.get() == NULL) {
            return false;
        }

        const char *utfChars = env->GetStringUTFChars(messageStr.get(), NULL);
        if (utfChars == NULL) {
            return false;
        }

        result = utfChars;

        env->ReleaseStringUTFChars(messageStr.get(), utfChars);
        return true;
    }

    jstring jniNewStringUTF(JNIEnv *env, const char *data, size_t size) {
        if (!env || !data) {
            LOGE(TAG, "env or data is NULL");
            return NULL;
        }

        if (size == 0) {
            return env->NewStringUTF("");
        }

        jbyteArray bytes = env->NewByteArray(size);
        if (bytes == NULL) {
            LOGE(TAG, "new ByteArray of %zu size failed", size);
            return NULL;
        }

        env->SetByteArrayRegion(bytes, 0, size, (jbyte *)data);
        if (env->ExceptionCheck()) {
            LOGE(TAG, "SetByteArrayRegion failed");
            return NULL;
        }

        return static_cast<jstring>(env->NewObject(JniConstants::StringClass,
                                                   JniConstants::StringFromBytesConstructor,
                                                   bytes, sUtf8Charset));
    }

} // namespace ssl
