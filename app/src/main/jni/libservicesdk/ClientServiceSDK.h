/*
 * PluginServiceManager.h
 *
 *  Created on: 2014-3-6
 *      Author: lity
 */

#ifndef PLUGINSERVICEMANAGER_H_
#define PLUGINSERVICEMANAGER_H_

#include "AcceptorRaw.h"
#include "reference/IReference.h"
#include "android/cxxJNI/CxxJavaObject.h"
#include "IQueryDeviceRotation.h"

class CClientServiceSDK : public CCxxJavaObject, public IQueryDeviceRotation
{
public:
	CClientServiceSDK();
	virtual ~CClientServiceSDK();

	virtual int OnQueryDeviceRotation();

public:
	/*!
	 * @brief   
	 * @return  int
	 */
	int Start();

	/*!
	 * @brief   
	 * @return  int
	 */
	int Stop();

	/*!
	 * @brief   
	 * @param 	appId	 
	 * @param 	appKey	 
	 * @return  int
	 */
	int LoginWithOpenID(std::string appId, std::string appKey);

	/*!
	 * @brief   
	 * @param 	address	 
	 * @param 	lic	 
	 * @return  int
	 */
	int Login(std::string address, std::string lic);

	/*!
	 * @brief   登出服务器
	 * @return  int
	 */
	int Logout();

	/*!
	 * @brief   获取登录服务器上的p2p地址
	 * @return  std::string
	 */
	std::string GetAddress();

	/*!
	 * @brief   创建会话
	 * @param 	plugin	 
	 * @return  std::string
	 */
	std::string CreateSession(std::string plugin);

	/*!
	 * @brief   销毁会话
	 * @param 	session	 
	 * @return  int
	 */
	int DestroySession(std::string session);

public:
    /*!
     * @brief   判断服务是否正在运行
     * @return  bool true 正在运行; false 未开启或者已停止
     */
    bool IsRunning();

	/*!
	 * @brief   
	 * @return  bool
	 */
	bool IsLogged();

	/*!
	 * @brief   
	 * @param 	state	 
	 * @param 	hr	 
	 * @return  long
	 */
	virtual long OnEvent(int state, int hr);

private:
	/**
	 * 插件连接的状态
	 */
	int m_state;
	/**
	 * 插件连接状态代码
	 */
	long m_error;
	/**
	 * Acceptor
	 */
	CRefObj<CAcceptorRaw> mAcceptor;

	/**
	 * 生成session时需要使用
	 */
	CRefObj<ISessionItemRaw> m_rawSession;
};

#endif /* PluginServiceManager_H_ */
