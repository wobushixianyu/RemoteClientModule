#include "com_oray_sunlogin_servicesdk_jni_ClientServiceSDK.h"
#include "android/jniobj.h"
#include "CxxJavaObject.h"
#include "ClientServiceSDK.h"
#include "android/android_log.h"

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeStart
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeStart
  (JNIEnv* env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	return mgr->Start();
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeStop
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeStop
  (JNIEnv *env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	return mgr->Stop();
}


/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeLoginWithOpenID
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeLoginWithOpenID
  (JNIEnv *env, jobject jthiz, jstring jappId, jstring jappkey)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	const char* cappId = env->GetStringUTFChars(jappId, JNI_FALSE);
	const char* cappkey = env->GetStringUTFChars(jappkey, JNI_FALSE);
	std::string cxxappId = cappId?cappId:"";
	std::string cxxappkey = cappkey?cappkey:"";
	env->ReleaseStringUTFChars(jappId, cappId);
	env->ReleaseStringUTFChars(jappkey, cappkey);

	return mgr->LoginWithOpenID(cxxappId, cxxappkey);
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeLogin
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeLogin
  (JNIEnv *env, jobject jthiz, jstring jaddress, jstring jlic)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	const char* caddress = env->GetStringUTFChars(jaddress, JNI_FALSE);
	const char* clic = env->GetStringUTFChars(jlic, JNI_FALSE);
	std::string cxxaddress = caddress;
	std::string cxxlic = clic;
	env->ReleaseStringUTFChars(jaddress, caddress);
	env->ReleaseStringUTFChars(jlic, clic);

	return mgr->Login(cxxaddress, cxxlic);
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeLogout
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeLogout
  (JNIEnv *env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	return mgr->Logout();
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeGetAddress
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeGetAddress
  (JNIEnv *env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	std::string address = mgr->GetAddress();
	return env->NewStringUTF(address.c_str());
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeCreateSession
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeCreateSession
  (JNIEnv *env, jobject jthiz, jstring jplugintype)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	const char* cplugintype = env->GetStringUTFChars(jplugintype, JNI_FALSE);
	std::string cxxplugintype = cplugintype;
	env->ReleaseStringUTFChars(jplugintype, cplugintype);
	
	std::string session = mgr->CreateSession(cxxplugintype);
	return env->NewStringUTF(session.c_str());
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeDestroySession
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeDestroySession
  (JNIEnv *env, jobject jthiz, jstring jsession)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	const char* csession = env->GetStringUTFChars(jsession, JNI_FALSE);
	std::string cxxsession = csession;
	env->ReleaseStringUTFChars(jsession, csession);
	return mgr->DestroySession(cxxsession);
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeIsLogged
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeIsLogged
  (JNIEnv *env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

	return mgr->IsLogged();
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeIsRunning
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeIsRunning
  (JNIEnv *env, jobject jthiz)
{
	CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CClientServiceSDK* mgr = GetThis<CClientServiceSDK>(env, jthiz);
	assert(mgr);

    return mgr->IsRunning();
}

/*
 * Class:     com_oray_sunlogin_servicesdk_jni_ClientServiceSDK
 * Method:    nativeCreateCxxObject
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeCreateCxxObject
  (JNIEnv *env, jobject jthiz)
{
    WriteLog(LOG_INFO, "Java_com_oray_sunlogin_servicesdk_jni_ClientServiceSDK_nativeCreateCxxObject()");
    CCxxJavaObject* pCxxObject = (CCxxJavaObject*)new CClientServiceSDK();
	pCxxObject->AddRef();

	// java与cxx对象互相引用
	jobject global = env->NewGlobalRef(jthiz);
	pCxxObject->AttachJavaObject(global);
	AttachJNIObj<CCxxJavaObject>(env, jthiz, pCxxObject);
	return 0;
}

 
