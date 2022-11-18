//
// 离开作用域自动 Delete 引用，来自 AOSP
// Created by hsz on 17-6-16.
//

#ifndef LINESELECTORDEMO_SCOPEDLOCALREF_H
#define LINESELECTORDEMO_SCOPEDLOCALREF_H

#include <jni.h>
#include <stddef.h>
#include "macros.h"

namespace ssl {
// A smart pointer that deletes a JNI local reference when it goes out of scope.
template<typename T>
class ScopedLocalRef {
public:
	ScopedLocalRef(JNIEnv& env, T localRef) : mEnv(&env), mLocalRef(localRef) {
	}

	~ScopedLocalRef() {
		reset();
	}

	void reset(T ptr = NULL) {
		if (ptr != mLocalRef) {
			if (mLocalRef != NULL) {
				mEnv->DeleteLocalRef(mLocalRef);
			}
			mLocalRef = ptr;
		}
	}

	T release() {
		T localRef = mLocalRef;
		mLocalRef = NULL;
		return localRef;
	}

	T get() const {
		return mLocalRef;
	}

private:
	JNIEnv *const mEnv;
	T mLocalRef;

	DISALLOW_COPY_AND_ASSIGN(ScopedLocalRef);
};

} // namespace ssl

#endif //LINESELECTORDEMO_SCOPEDLOCALREF_H
