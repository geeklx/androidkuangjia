/*
 * macros.h
 *
 *  一些常用的宏，摘抄自 Android ART 源码
 *  Created on: 2016年12月12日
 *      Author: hsz
 */

#ifndef EASYAPP_BASE_MACROS_H_
#define EASYAPP_BASE_MACROS_H_

// 数组元素个数
#define NELEM(array_name) (sizeof(array_name) / sizeof((array_name)[0]))

// DISALLOW_COPY_AND_ASSIGN disallows the copy and operator= functions.
// It goes in the private: declarations in a class.
#define DISALLOW_COPY_AND_ASSIGN(TypeName) \
  TypeName(const TypeName&);               \
  void operator=(const TypeName&)

// A macro to disallow all the implicit constructors, namely the
// default constructor, copy constructor and operator= functions.
//
// This should be used in the private: declarations for a class
// that wants to prevent anyone from instantiating it. This is
// especially useful for classes containing only static methods.
#define DISALLOW_IMPLICIT_CONSTRUCTORS(TypeName) \
  TypeName();                                    \
  DISALLOW_COPY_AND_ASSIGN(TypeName)

#if defined(__clang__) || defined(__GNUC__)
#define LIKELY(x)       __builtin_expect((x), true)
#define UNLIKELY(x)     __builtin_expect((x), false)

#else
#define LIKELY(x)       (x)
#define UNLIKELY(x)     (x)

#endif

#ifndef NDEBUG
#define ALWAYS_INLINE
#else
#define ALWAYS_INLINE  __attribute__ ((always_inline))
#endif


//注销通知，插件主动注销
#define NOTIFICATION_TUNNEL_PLUGIN_LOGOUT @"com.sangfor.awork.notification.logout"
//通知注销的原因
#define NOTIFICATION_TUNNEL_PLUGIN_LOGOUT_REASON @"awork.logout.reason"
//插件已停止的通知，被动【可能奔溃】
#define NOTIFICATION_TUNNEL_PLUGIN_NOT_EXIST @"com.sangfor.notifications.l3vpn.shudown"

#endif /* EASYAPP_BASE_MACROS_H_ */
