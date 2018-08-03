# Copyright Oray

LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

#lib name
LOCAL_MODULE 		:= orayservice_sdk
LOCAL_CPP_EXTENSION := .cc .cpp

#macro
LOCAL_CPPFLAGS 	+= -D__ANDROID__ -DTIXML_USE_STL -DUSE_ANDROID_LOCALSOCKET
LOCAL_CFLAGS	+= -DUSE_UNIXSOCKET

ROOT_PATH			:= ../../../../../../../../..
THIRD_PATH   		:= ../../../../../../../../../../../3rd

COMMON_PATH  		:= $(ROOT_PATH)/common
SRC_INCLUDE 		:= $(ROOT_PATH)/include
UDPLIB2_SRC  		:= $(ROOT_PATH)/public/udplib2/src
INCLUDE_PATH 		:= $(ROOT_PATH)/sunlogin/include
DESKTOP_PATH 		:= $(ROOT_PATH)/sunlogin/client/plugin/OrayRemoteDesktop
INTERFACEMP  		:= $(ROOT_PATH)/public/OrayPluginInterfaceMP
INTERFACE    		:= $(ROOT_PATH)/public/OrayPluginInterface
ANDROID_COMMON		:= $(ROOT_PATH)/sunlogin/include/android/libCommon
CXXOBJECTJNI		:= $(ROOT_PATH)/sunlogin/include/android/cxxJNI
IPC_PATH            := $(ROOT_PATH)/sunlogin/client/public/ipc
SUNLOGIN_PATH		:= $(ROOT_PATH)/sunlogin

POLARSSL     		:= $(THIRD_PATH)/polarssl-1.3.9
LIBJINGLE_SRC		:= $(THIRD_PATH)/libjingle

LOCAL_C_INCLUDES := \
$(LOCAL_PATH)/$(ANDROID_COMMON)/jni \
$(LOCAL_PATH)/$(COMMON_PATH) \
$(LOCAL_PATH)/$(SRC_INCLUDE) \
$(LOCAL_PATH)/$(THIRD_PATH) \
$(LOCAL_PATH)/$(SSL_INCLUDE) \
$(LOCAL_PATH)/$(INCLUDE_PATH) \
$(LOCAL_PATH)/$(COMMON_PATH) \
$(LOCAL_PATH)/$(SRC_INCLUDE) \
$(LOCAL_PATH)/$(INCLUDE_PATH) \
$(LOCAL_PATH)/$(INCLUDE_PATH)/remotedesktop \
$(LOCAL_PATH)/../../OrayPluginInterface/jni \
$(LOCAL_PATH)/$(UDPLIB2_SRC) \
$(LOCAL_PATH)/$(INTERFACEMP) \
$(LOCAL_PATH)/$(INTERFACE) \
$(LOCAL_PATH)/$(LIBJINGLE_SRC) \
$(LOCAL_PATH)/$(POLARSSL)/include \
$(LOCAL_PATH)/$(DESKTOP_PATH) \
$(LOCAL_PATH)/$(SUNLOGIN_PATH)/client/mac/remoteclient/src \
$(LOCAL_PATH)/$(SUNLOGIN_PATH)/client/linux/include \
$(LOCAL_PATH)/$(SUNLOGIN_PATH)/client/public/ipc \
$(LOCAL_PATH)/$(SUNLOGIN_PATH)/client/android/libservice \
$(LOCAL_PATH)/$(CXXOBJECTJNI) \
$(LOCAL_PATH)/$(IPC_PATH) \
$(JNI_H_INCLUDE)

LOCAL_SRC_FILES := \
$(DESKTOP_PATH)/DesktopServerPluginRaw.cpp \
$(SUNLOGIN_PATH)/client/mac/remoteclient/src/DesktopMsgParser.cpp \
$(SUNLOGIN_PATH)/client/mac/remoteclient/src/LocalControlStream.cpp \
$(SUNLOGIN_PATH)/client/mac/remoteclient/src/PluginEntity.cpp \
$(IPC_PATH)/ipc_base.cpp \
$(SUNLOGIN_PATH)/client/linux/include/PHSocket.cpp \
$(SUNLOGIN_PATH)/client/android/libservice/InputAgentClient_android.cpp \
$(SUNLOGIN_PATH)/client/android/libservice/ScreenAgentClient_android.cpp \
$(IPC_PATH)/android_localsockets.c \
$(IPC_PATH)/ipc_android.cpp \
$(INCLUDE_PATH)/MessageLoopThreadRaw.cpp \
$(SRC_INCLUDE)/get_proxy_info.cpp \
$(SRC_INCLUDE)/ActivePlugin.cpp \
$(SRC_INCLUDE)/LicVerifierRaw.cpp \
$(SRC_INCLUDE)/PluginStreamImplRaw.cpp \
$(SRC_INCLUDE)/DecideMultiChannelClient.cpp \
$(SRC_INCLUDE)/DecideTcpClientType.cpp \
$(SRC_INCLUDE)/MultiChannelStream.cpp \
$(SRC_INCLUDE)/PluginThreadManager.cpp \
$(SRC_INCLUDE)/PreConnectProxySvrHandler.cpp \
$(INTERFACEMP)/OrayPluginInterfaceMP.cpp \
$(INTERFACE)/AcceptorImpl.cpp \
$(INTERFACE)/AcceptorRaw.cpp \
$(INTERFACE)/ConnectorRaw.cpp \
$(INTERFACE)/ProxySettingRaw.cpp \
$(INTERFACE)/SessionItemRaw.cpp \
$(INTERFACE)/ClientAcceptor.cpp \
$(INTERFACE)/ClientStream.cpp \
$(INTERFACE)/DecideClient.cpp \
$(INTERFACE)/GetRemoteAddress.cpp \
$(INTERFACE)/HostStream.cpp \
$(INTERFACE)/PretreatHandler.cpp \
$(INTERFACE)/StreamInfoImplHandler.cpp \
$(INTERFACE)/TCPAcceptor.cpp \
$(INTERFACE)/TCPConnector.cpp \
$(INTERFACE)/UDPAcceptor.cpp \
$(INTERFACE)/UDPConnector.cpp \
$(INTERFACE)/ReconnectHandler.cpp \
$(UDPLIB2_SRC)/Bigbit.cpp \
$(UDPLIB2_SRC)/crypto.cpp \
$(UDPLIB2_SRC)/udpstack.cpp \
./AndroidAcceptor.cpp \
./ClientServiceSDK.cpp \
./com_oray_sunlogin_servicesdk_jni_ClientServiceSDK.cpp \


LOCAL_LDLIBS += -lm -llog -lz -lstdc++ \
	-L$(LOCAL_PATH)/../../libs/$(TARGET_ARCH_ABI) -loraycommon

include $(BUILD_SHARED_LIBRARY) 
