#ifndef _ANDROID_ACCEPTOR_H_
#define _ANDROID_ACCEPTOR_H_

#include "AcceptorRaw.h"

/**
 * 这里只有定义，基本没有实现 
 */ 
class CAndroidAcceptor : public CAcceptorRaw {
public:
	CAndroidAcceptor();
	virtual ~CAndroidAcceptor();

public:
	virtual void OnPlugConnect(CRefObj<IPluginRaw> pPlugin);
	virtual void OnPlugDisconnect(CRefObj<IPluginRaw> pPlugin);

};

#endif
