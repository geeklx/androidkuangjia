//
// 离开作用域自动 detach VM
// Created by hsz on 17-6-8.
//

#ifndef LINESELECTORDEMO_SCOPEDJNIENV_H
#define LINESELECTORDEMO_SCOPEDJNIENV_H

#include "macros.h"
#include "logging.h"

#include <jni.h>
#include <stddef.h>

namespace ssl {

class ScopedJniEnv {
public:
	ScopedJniEnv(JavaVM& vm, jint version)
			: vm_(&vm), env_(NULL), attached_(false) {

		jint ret = vm_->GetEnv((void **) &env_, version);
		if (ret == JNI_OK) {
			return;
		} else if (ret != JNI_EDETACHED) {
			LOGE("ScopedJniEnv", "GetEnv failed: %d", ret);
			return;
		}

		ret = vm_->AttachCurrentThread(&env_, NULL);
		if (ret == JNI_OK) {
			attached_ = true;
		} else {
			LOGE("ScopedJniEnv", "AttachCurrentThread failed: %d", ret);
		}
	}

	~ScopedJniEnv() {
		if (attached_) {
			vm_->DetachCurrentThread();
		}
	}

	JNIEnv *getEnv() const {
		return env_;
	}

	bool valid() const {
		return env_ != NULL;
	}

private:
	JavaVM * const vm_;
	JNIEnv *env_;
	bool attached_;

	DISALLOW_COPY_AND_ASSIGN(ScopedJniEnv);
};

} // namespace ssl

#endif //LINESELECTORDEMO_SCOPEDJNIENV_H
