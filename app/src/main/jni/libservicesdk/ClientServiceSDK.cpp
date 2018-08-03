#include "ClientServiceSDK.h"
#include "ErrType.h"
#include "jni.h"
#include "android/AutoDetach.h"
#include "DesktopServerPluginRaw.h"
#include "android/android_log.h"

class CAcceptorEvent : public IAcceptorRaw::IAcceptorEventListener, private CReference
{
public:
	enum {
		STATE_UNKNOWN = 0,
		STATE_LOGIN_OK,
		STATE_LOGIN_FAIL,
		STATE_CONNECT,
		STATE_DISCONNECT,
		STATE_PLUGIN_CONNECT,
		STATE_PLUGIN_DISCONNECT,

		STATE_LOGIN_ERROR_BEGIN = 20,
		ERROR_INVALID_ARG,
		ERROR_INVALID_LICENSE,
		ERROR_INVALID_SERVERADDR,
		ERROR_INVALID_PROTOCOL,
		ERROR_LICENSE_EXPIRE,
		ERROR_VERIFY_FAIL,
		ERROR_LOGIN_FAIL,
		STATE_LOGIN_ERROR_END = 39
	};
	CAcceptorEvent(CClientServiceSDK* mgr);
	~CAcceptorEvent();
	virtual bool OnConnect();
	virtual bool OnDisconnect();
	virtual bool OnLogOK();
	virtual bool OnLogFailed();
	/** 插件连接成功 */
	virtual bool OnPlugConnect(CRefObj<IPluginRaw> pPlug);
	/** 插件已经断开 */
	virtual bool OnPlugDisconnect(CRefObj<IPluginRaw> pPlug);
	virtual unsigned long __stdcall AddRef();
	virtual unsigned long __stdcall Release();
	/**
	* 外部类对象
	*/
	CRefObj<CClientServiceSDK> mOuter;

	/** 登录过程中出错信息 */
	virtual void OnErrorMessage(int errorcode, const char* errormsg);
};

CAcceptorEvent::CAcceptorEvent(CClientServiceSDK* mgr)
{
	WriteLog(LOG_INFO, "[sevice][sdk] CAcceptorEvent::CAcceptorEvent");
	mOuter = mgr;
}

CAcceptorEvent::~CAcceptorEvent()
{
	WriteLog(LOG_INFO, "[sevice][sdk] CAcceptorEvent::~CAcceptorEvent");
}

bool CAcceptorEvent::OnConnect()
{
	return 0 == mOuter->OnEvent(STATE_CONNECT, 0);
}

bool CAcceptorEvent::OnDisconnect()
{
	return 0 == mOuter->OnEvent(STATE_DISCONNECT, 0);
}

bool CAcceptorEvent::OnLogOK()
{
	return 0 == mOuter->OnEvent(STATE_LOGIN_OK, 0);
}

bool CAcceptorEvent::OnLogFailed()
{
	return 0 == mOuter->OnEvent(STATE_LOGIN_FAIL, 0);
}

bool CAcceptorEvent::OnPlugConnect(CRefObj<IPluginRaw> pPlug)
{
	return 0 == mOuter->OnEvent(STATE_PLUGIN_CONNECT, 0);
}

bool CAcceptorEvent::OnPlugDisconnect(CRefObj<IPluginRaw> pPlug)
{
	return 0 == mOuter->OnEvent(STATE_PLUGIN_DISCONNECT, 0);
}

void CAcceptorEvent::OnErrorMessage(int errorcode, const char* errormsg)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	mOuter->OnEvent(STATE_LOGIN_ERROR_BEGIN + errorcode, 0);
}

unsigned long  CAcceptorEvent::AddRef()
{
	return CReference::AddRef();
}

unsigned long  CAcceptorEvent::Release()
{
	return CReference::Release();
}

/************************************************************************/
/*                                                                      */
/************************************************************************/
CClientServiceSDK::CClientServiceSDK()
{
	WriteLog(LOG_INFO, "[sevice][sdk] CClientServiceSDK::CClientServiceSDK()");
}

CClientServiceSDK::~CClientServiceSDK()
{
	WriteLog(LOG_INFO, "[sevice][sdk] CClientServiceSDK::~CClientServiceSDK()");
}

int CClientServiceSDK::OnQueryDeviceRotation()
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	CAutoDetach env;
	assert(NULL != env);

	jobject obj = GetJavaObjectLocalRef(env);
	if (NULL != obj) {
		// 回调类
		jclass jclzz = env->GetObjectClass(obj);
		assert(NULL != jclzz);

		jmethodID callback = env->GetStaticMethodID(jclzz, "jniCallGetDisplayRotation", "()I");
		if (callback) {
			jint rotation = env->CallStaticIntMethod(jclzz, callback);
			WriteLog(LOG_INFO, "[sevice][sdk] GetDisplayRotation:%d", rotation);
			return rotation;
		}
	}

	return 0;
}

long CClientServiceSDK::OnEvent(int state, int hr)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	m_state = state;
	m_error = hr;
	WriteLog(LOG_INFO, "[sevice][sdk] CClientServiceSDK::OnEvent(), state= %d, error=%d.", state, hr);

	CAutoDetach env;
	assert(NULL != env);

	jobject javaPluginLocalRef =  GetJavaObjectLocalRef(env);
	if (NULL != javaPluginLocalRef) {
		jclass jclzz = env->GetObjectClass(javaPluginLocalRef);
		assert(NULL != jclzz);

		//回调方法
		jmethodID methodId = env->GetMethodID(jclzz,"jniCallOnConnectorOnEvent","(II)V");
		assert(NULL != methodId);

		//调用java端状态变化事件响应并传出host的状态和错误码
		env->CallVoidMethod(javaPluginLocalRef, methodId, (jint)state, (jint)hr);

		// 必须释放LocalRef
		env->DeleteLocalRef(javaPluginLocalRef);
	}else {
		// Java对象无效, 不作任务处理
	}
	return S_OK;
}

int CClientServiceSDK::Start()
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	bool is_running = IsRunning();
    if (is_running) {
        return 1;
    } else {
        // 创建Acceptor, 并监听
		mAcceptor =  new CAcceptorRaw();
		assert(mAcceptor);

        CAcceptorEvent* listener = new CAcceptorEvent(this);
        mAcceptor->set_listener(listener);

		// 开启Acceptor, 必须开启
		mAcceptor->Start();
        return 0;
    }
}

int CClientServiceSDK::Stop()
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	if (NULL != mAcceptor) {
		mAcceptor->set_listener(NULL);
        mAcceptor->Stop();
        mAcceptor = NULL;
        return 0;
	} else {
        return 1;
    }
}

int CClientServiceSDK::LoginWithOpenID(std::string appId, std::string appkey)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	int ret;
	if (mAcceptor) {
		mAcceptor->LoginWithOpenID(appId.c_str(), appkey.c_str());
		ret = 0;
	} else {
		ret = 1;
	}
	return ret;
}

int CClientServiceSDK::Login(std::string address, std::string lic)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	int ret;
	if (mAcceptor) {
		mAcceptor->Login(address.c_str(), lic.c_str());
		ret = 0;
	} else {
		ret = -1;
	}
	return ret;
}

std::string CClientServiceSDK::GetAddress()
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	std::string address;
	if (mAcceptor)
		address = mAcceptor->get_address();
	WriteLog(LOG_INFO, "[service][sdk] local host address: %s", address.c_str());
	return address;
}

std::string CClientServiceSDK::CreateSession(std::string plugin)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	std::string session;

	if (NULL != mAcceptor) {
		if ("desktop" == plugin) {
    		CDesktopServerPluginRaw* rawPlugin = new CDesktopServerPluginRaw();
			rawPlugin->SetEventListener(this);
    		mAcceptor->AcceptEx(rawPlugin, "", false, &m_rawSession.p);
		} else if ("file" == plugin) {
			//do nothing, not implement
		} else if("data" == plugin) {
			//do nothing, not implement
		}
	}
	if (NULL != m_rawSession) {
		const char* szsession = m_rawSession->get_Value();
		session = (szsession != NULL ? szsession : "");
	}
	WriteLog(LOG_INFO, "[service][sdk] plugin: %s, session: %s", plugin.c_str(), session.c_str());
	return session;
}


int CClientServiceSDK::DestroySession(std::string session)
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	if (mAcceptor) {
        return mAcceptor->Cancel(session);
    }
    return 1;
}

bool CClientServiceSDK::IsLogged()
{
	if (mAcceptor)
		return mAcceptor->is_logged();
	return false;
}

bool CClientServiceSDK::IsRunning()
{
	if (mAcceptor) {
        return mAcceptor->IsRunning();
    }
    return false;
}

int CClientServiceSDK::Logout()
{
	//CAutoDebugLog_Android debuglog(__FUNCTION__, __FILE__, __LINE__);
	if (mAcceptor) {
		mAcceptor->Logout();
        return 0;
    } else {
        return 1;
    }
}

