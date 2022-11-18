//
// Created by jianxin on 2019/7/4.
//

#ifndef SANGFORSDK_SCOPEDJSTRING_H
#define SANGFORSDK_SCOPEDJSTRING_H

#include <jni.h>

#include "JNIHelp.h"

namespace ssl {
    class ScopedJniString {
    public:
        ScopedJniString(JNIEnv *env, const char *data, size_t size):
            jstr_(NULL) {

            if (data == NULL) {
                jstr_ = NULL;
                return ;
            }
            if (size == 0) {
                jstr_ = env->NewStringUTF("");
                return ;
            }

            jbyteArray bytes = env->NewByteArray(size);
            if (bytes == NULL) {
                //LOGE(TAG, "new ByteArray of %zu size failed", size);
                return;
            }

            env->SetByteArrayRegion(bytes, 0, size, (jbyte *) data);
            if (env->ExceptionCheck()) {
                //LOGE(TAG, "SetByteArrayRegion failed");
                return;
            }
            jstr_ = static_cast<jstring>(env->NewObject(JniStringConstants::stringClass,
                                                       JniStringConstants::stringConstructor,
                                                       bytes,
                                                       env->NewStringUTF("utf-8")));
        }

        bool valid() {
            return jstr_ != nullptr;
        }
        jstring getJniString() {
            return jstr_;
        }
    private:
        jstring jstr_;
    };
}
#endif //SANGFORSDK_SCOPEDJSTRING_H
