#include <jni.h>
#include <string>
#include "logging.h"
#include "native_lib.h"
#include "ScopedUtfChars.h"
#include "SFTestUdpClient.h"
#include "ScopedJniEnv.h"
#include "ScopedJniString.h"

#define TAG "native-lib"
using namespace ssl;

static JavaVM *sJvm = NULL;
static jint sJvmVersion = -1;

#define NATIVE_METHOD(name, signature) {#name, signature, (void *)UdpNative_##name}

// 数组元素个数
#define NELEM(array_name) (sizeof(array_name) / sizeof((array_name)[0]))
#define CLASS_NAME           "com/sangfor/sdkdemo/udp/UdpOperator"

static jclass sUdpOperatorClass = NULL;


static void UdpNative_nativeSendTo(JNIEnv* env,
                                   jclass clazz,
                                   jstring jip, jint jport, jstring jdata, jint jdataLen, jboolean looper)
{
    ScopedUtfChars ip(env, jip);
    ScopedUtfChars data(env, jdata);
    StartUdpClientTest(UDP_CLIENT_TYPE_SENDTO, ip.c_str(), (int)jport, data.c_str(), (int)jdataLen,
                       (bool) looper);
}

static const JNINativeMethod sMethods[] = {
        NATIVE_METHOD(nativeSendTo, "(Ljava/lang/String;ILjava/lang/String;IZ)V"),
};

struct CallbackMethodInfo {
    jmethodID id;
    const char *name;
    const char *signature;
};

//AuthResultListener的回调
static CallbackMethodInfo sUdpCallbacks[] = {
        {NULL, "recvfromCallback",  "(Ljava/lang/String;ILjava/lang/String;I)V"}
};
enum {
    NOTIFY_RECV_FROM,
    NOTIFY_AUTH_SUCCESS,
    NOTIFY_AUTH_FAILED,
    NOTIFY_NEXT_AUTH,
};


bool initUdpNative(JavaVM *vm, JNIEnv *env)
{
    if (!vm || !env) {
        LOGE(TAG, "Invalid Arguments: %p, %p", vm, env);
        return false;
    }
    sJvm = vm;
    sJvmVersion = env->GetVersion();
    jclass clazz = env->FindClass(CLASS_NAME);
    if (!clazz) {
        LOGE(TAG, "initMobileSecurityNative class %s not found", CLASS_NAME);
        return false;
    }
    jint ret = env->RegisterNatives(clazz, sMethods, NELEM(sMethods));
    if (ret != 0) {
        LOGE(TAG, "initMobileSecurityNative RegisterNatives for %s failed", CLASS_NAME);
        return false;
    }

    sUdpOperatorClass = reinterpret_cast<jclass>(env->NewGlobalRef(
            env->FindClass(CLASS_NAME)));
    if (sUdpOperatorClass == NULL) {
        LOGE(TAG, "initUdpNative find %s class failed.", CLASS_NAME);
        return false;
    }

    for (size_t i = 0; i < NELEM(sUdpCallbacks); ++i) {
        sUdpCallbacks[i].id = env->GetStaticMethodID(sUdpOperatorClass, sUdpCallbacks[i].name,
                                               sUdpCallbacks[i].signature);
        if (sUdpCallbacks[i].id == NULL) {
            LOGE(TAG, "initMobileSecurityNative method %s not found", sUdpCallbacks[i].name);
            return false;
        }
    }

    return true;

}

void onRecvData(const std::string &ip, int port, const std::string &data, int dataLen)
{
    ScopedJniEnv envGuard(*sJvm, sJvmVersion);
    if (!envGuard.valid()) {
        LOGE(TAG, "NativeLogoutListener, get env failed");
        return;
    }
    JNIEnv *env = envGuard.getEnv();

    ScopedJniString jip(env, ip.c_str(), ip.length());
    ScopedJniString jdata(env, data.c_str(), data.length());

    env->CallStaticVoidMethod(sUdpOperatorClass, sUdpCallbacks[NOTIFY_RECV_FROM].id, jip, port, jdata, dataLen);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        LOGE(TAG, "vm->GetEnv failed");
        return -1;
    }

    if (!initJniHelper(vm, env)) {
        LOGE(TAG, "initJniHelper failed");
        return -1;
    }

    if (!initUdpNative(vm, env))
    {
        LOGE(TAG, "vm->GetEnv failed");
    }
    return JNI_VERSION_1_6;
}