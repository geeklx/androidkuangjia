/*
 * ScopedUtfChars.h
 *  自动销毁的 jstring
 *  Created on: 2016年12月12日
 *      Author: hsz
 */

#ifndef EASYAPP_BASE_SCOPEDUTFCHARS_H_
#define EASYAPP_BASE_SCOPEDUTFCHARS_H_

#include <jni.h>
#include <stddef.h>
#include <string.h>

#include "macros.h"

namespace ssl {

class ScopedUtfChars {
public:
	ScopedUtfChars(JNIEnv* env, jstring jstr) :
			env_(env), jstr_(jstr), cstr_(NULL), len_(-1) {
		if (!jstr) {
			return;
		}

		cstr_ = env_->GetStringUTFChars(jstr, NULL);
        len_ = cstr_ ? strlen(cstr_): -1;
	}

	~ScopedUtfChars() {
		if (cstr_ != NULL) {
			env_->ReleaseStringUTFChars(jstr_, cstr_);
		}
	}

	const char *c_str() const {
		return cstr_;
	}

	/**
	 * 判断转换结果是否为空，注意，如果传入一个空的 jstring，结果也为空
	 * @return true 如果不为空
	 */
	bool valid() const {
		return cstr_ != NULL;
	}

	int length() const {
		return len_;
	}

private:
	JNIEnv * const env_;
	jstring jstr_;
	const char *cstr_;
    int len_;

	DISALLOW_COPY_AND_ASSIGN(ScopedUtfChars);
};

} // namespace ssl

#endif /* EASYAPP_BASE_SCOPEDUTFCHARS_H_ */
