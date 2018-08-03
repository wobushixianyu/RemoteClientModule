# Copyright Oray

LOCAL_PATH := $(call my-dir)

###############################################################################
# oraycommon
###############################################################################
include $(CLEAR_VARS)

# oraycommon库, 包含的common下面的库, lib_json, tinyxml 等
LOCAL_MODULE            := oraycommon
LOCAL_CPP_EXTENSION     := .cpp .cxx .cc 

#macro defines
LOCAL_CPPFLAGS          += -D__ANDROID__ -DTIXML_USE_STL -DUSE_ORAY_DNS
LOCAL_CPPFLAGS          += -DHTTPCALL_USER_AGENT="\"SLRC/3.0 (Android; SDK)\""

ROOT_PATH				:= ../../../../../../../../..
THIRD_PATH              := ../../../../../../../../../../../3rd

COMMON_PATH             := $(ROOT_PATH)/common
SRC_INCLUDE_PATH        := $(ROOT_PATH)/include
PLUGININTERFACE_PATH    := $(ROOT_PATH)/public/OrayPluginInterface
UDPLIB_PATH             := $(ROOT_PATH)/public/udplib2/src
SUNLOGIN_INCLUDE        := $(ROOT_PATH)/newsunlogin/include
CXXOBJECTJNI            := $(ROOT_PATH)/newsunlogin/include/android/cxxJNI
TINYXML_PATH            := $(THIRD_PATH)
JSON_SRC                := $(THIRD_PATH)/jsoncpp
POLARSSL                := $(THIRD_PATH)/polarssl-1.3.9
LIBJINGLE_TALK          := $(THIRD_PATH)/libjingle

#files
LOCAL_SRC_FILES := \
    $(CXXOBJECTJNI)/com_oray_sunlogin_jni_JavaCxxObject.cpp \
    $(CXXOBJECTJNI)/CxxJavaObject.cpp \
    $(SUNLOGIN_INCLUDE)/android/GlobalDef.cpp \
    $(COMMON_PATH)/urlserializer/UrlSerializer.cpp \
    $(COMMON_PATH)/time/timecnv.cpp \
    $(COMMON_PATH)/String/String.cpp \
    $(COMMON_PATH)/blowfish/blowfish.cpp \
    $(COMMON_PATH)/base64/base64.cpp \
    $(COMMON_PATH)/md5/md5.cpp \
    $(COMMON_PATH)/md5/md5code.cpp \
    $(COMMON_PATH)/urlcode/urlcode.cpp \
    $(COMMON_PATH)/httpparser/HttpParser.cpp \
    $(COMMON_PATH)/logger/flogger.cpp \
    $(COMMON_PATH)/thread/BaseThread.cpp \
    $(COMMON_PATH)/memalloc/mallocins.cpp \
    $(COMMON_PATH)/phstream/BaseStream.cpp \
    $(COMMON_PATH)/phstream/SockStream.cpp \
    $(COMMON_PATH)/phstream/SockConnector.cpp \
    $(COMMON_PATH)/phstream/SockAcceptor.cpp \
    $(COMMON_PATH)/phstream/HttpProxyConnector.cpp \
    $(COMMON_PATH)/phstream/InitSock.cpp \
    $(COMMON_PATH)/phstream/MultiplexHandler.cpp \
    $(COMMON_PATH)/phstream/MultiplexLogicStream.cpp \
    $(COMMON_PATH)/phstream/Proxy.cpp \
    $(COMMON_PATH)/phstream/Socks4ProxyConnector.cpp \
    $(COMMON_PATH)/phstream/Socks5ProxyConnector.cpp \
    $(COMMON_PATH)/phstream/UDPLibStream.cpp \
    $(COMMON_PATH)/phstream/UDPLibWrapper.cpp \
	$(COMMON_PATH)/phstream/WebStream.cpp \
    $(COMMON_PATH)/phstream/SSLStream.cpp \
    $(COMMON_PATH)/DetectLocalIP.cpp \
    $(COMMON_PATH)/ObjectMonitor/ObjectMonitor.cpp \
    $(COMMON_PATH)/utf8code/UTF8Code.cpp \
    $(COMMON_PATH)/gzip/gzip_decoder.cpp \
    $(COMMON_PATH)/msg_include/oraymsg.cpp \
    $(COMMON_PATH)/msg_include/oraymsgclient.cpp \
    $(COMMON_PATH)/msg_include/SubScribeClient.cpp \
    $(COMMON_PATH)/http_call/http_call.cpp \
    $(COMMON_PATH)/http_call/HttpObject.cpp \
    $(COMMON_PATH)/urlparser/urlparser.cpp \
    $(COMMON_PATH)/format_message/format_message.cpp \
    $(COMMON_PATH)/libsocket/libsocket.cpp \
    $(COMMON_PATH)/time/timer.cpp \
    $(COMMON_PATH)/crc/crc_16.cpp \
    $(COMMON_PATH)/upnp/upnpnat.cpp \
    $(COMMON_PATH)/upnp/xmlParser.cpp \
    $(COMMON_PATH)/resolve/o_resolve.cpp \
    $(COMMON_PATH)/estr/EString.cpp \
    $(TINYXML_PATH)/tinyxml/tinystr.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxml.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxmlerror.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxmlparser.cpp \
    $(JSON_SRC)/src/lib_json/json_reader.cpp \
    $(JSON_SRC)/src/lib_json/json_value.cpp \
    $(JSON_SRC)/src/lib_json/json_writer.cpp \
    $(SRC_INCLUDE_PATH)/get_proxy_info.cpp \
    $(SRC_INCLUDE_PATH)/DecideTcpClientType.cpp \
    $(SRC_INCLUDE_PATH)/PreConnectProxySvrHandler.cpp \
    $(SRC_INCLUDE_PATH)/ActivePlugin.cpp \
    $(SRC_INCLUDE_PATH)/LicVerifierRaw.cpp \
    $(SRC_INCLUDE_PATH)/PluginStreamImplRaw.cpp \
    $(SRC_INCLUDE_PATH)/DecideMultiChannelClient.cpp \
    $(SRC_INCLUDE_PATH)/MultiChannelStream.cpp \
    $(SRC_INCLUDE_PATH)/PluginThreadManager.cpp \
	$(LIBJINGLE_TALK)/talk/base/sha1.cc \
	$(LIBJINGLE_TALK)/talk/base/base64.cc \
	
    
################################################
# LOCAL_C_INCLUDES dirs
################################################

LOCAL_C_INCLUDES := \
$(JNI_INTERFACE) \
$(LOCAL_PATH)/$(COMMON_PATH) \
$(LOCAL_PATH)/$(INCLUDE_INCLUDE) \
$(LOCAL_PATH)/$(TINYXML_PATH) \
$(LOCAL_PATH)/$(TINYXML_PATH)/tinyxml \
$(LOCAL_PATH)/$(JSON_SRC)/include \
$(LOCAL_PATH)/$(POLARSSL)/include \
$(LOCAL_PATH)/$(LIBJINGLE_TALK) \
$(LOCAL_PATH)/$(UDPLIB_PATH) \
$(LOCAL_PATH)/$(SRC_INCLUDE_PATH) \
$(LOCAL_PATH)/$(SUNLOGIN_INCLUDE) \
$(LOCAL_PATH)/$(PLUGININTERFACE_PATH) \
$(LOCAL_PATH)/$(CXXOBJECTJNI)


LOCAL_LDLIBS  += -lm -llog -lz -lstdc++

#link static libs
LOCAL_STATIC_LIBRARIES := libudp libpolarssl

#create lib
include $(BUILD_SHARED_LIBRARY)

###############################################################################
# oraycommon_chhdiy
###############################################################################
ifeq ($(TARGET_DIY),chhdiy)
include $(CLEAR_VARS)

# oraycommon库, 包含的common下面的库, lib_json, tinyxml 等
LOCAL_MODULE            := oraycommon_chhdiy
LOCAL_CPP_EXTENSION     := .cpp .cxx .cc 

#macro defines
LOCAL_CPPFLAGS          += -D__ANDROID__ -DTIXML_USE_STL -DUSE_ORAY_DNS
LOCAL_CPPFLAGS          += -DCHANGHONG_DIY
LOCAL_CPPFLAGS          += -DHTTPCALL_USER_AGENT="\"SLCC/1.0 (Diy chonghong)\""

#path
ROOT_PATH				:= ../../../../..
THIRD_PATH              := ../../../../../../../3rd

COMMON_PATH             := $(ROOT_PATH)/common
SRC_INCLUDE_PATH        := $(ROOT_PATH)/include
PLUGININTERFACE_PATH    := $(ROOT_PATH)/public/OrayPluginInterface
UDPLIB_PATH             := $(ROOT_PATH)/public/udplib2/src
SUNLOGIN_INCLUDE        := $(ROOT_PATH)/newsunlogin/include
CXXOBJECTJNI            := $(ROOT_PATH)/newsunlogin/include/android/cxxJNI
LIBUDP2_PATH        	:= $(ROOT_PATH)/newsunlogin/include/android/libUDP2
TINYXML_PATH            := $(THIRD_PATH)
JSON_SRC                := $(THIRD_PATH)/jsoncpp
POLARSSL                := $(THIRD_PATH)/polarssl-1.3.9
LIBJINGLE_TALK          := $(THIRD_PATH)/libjingle

#files
LOCAL_SRC_FILES := \
    $(CXXOBJECTJNI)/com_oray_sunlogin_jni_JavaCxxObject.cpp \
    $(CXXOBJECTJNI)/CxxJavaObject.cpp \
    $(SUNLOGIN_INCLUDE)/android/GlobalDef.cpp \
    $(COMMON_PATH)/urlserializer/UrlSerializer.cpp \
    $(COMMON_PATH)/time/timecnv.cpp \
    $(COMMON_PATH)/String/String.cpp \
    $(COMMON_PATH)/blowfish/blowfish.cpp \
    $(COMMON_PATH)/base64/base64.cpp \
    $(COMMON_PATH)/md5/md5.cpp \
    $(COMMON_PATH)/md5/md5code.cpp \
    $(COMMON_PATH)/urlcode/urlcode.cpp \
    $(COMMON_PATH)/httpparser/HttpParser.cpp \
    $(COMMON_PATH)/logger/flogger.cpp \
    $(COMMON_PATH)/thread/BaseThread.cpp \
    $(COMMON_PATH)/phstream/BaseStream.cpp \
    $(COMMON_PATH)/memalloc/mallocins.cpp \
    $(COMMON_PATH)/phstream/SockStream.cpp \
    $(COMMON_PATH)/phstream/SockConnector.cpp \
    $(COMMON_PATH)/phstream/SockAcceptor.cpp \
    $(COMMON_PATH)/phstream/HttpProxyConnector.cpp \
    $(COMMON_PATH)/phstream/InitSock.cpp \
    $(COMMON_PATH)/phstream/MultiplexHandler.cpp \
    $(COMMON_PATH)/phstream/MultiplexLogicStream.cpp \
    $(COMMON_PATH)/phstream/Proxy.cpp \
    $(COMMON_PATH)/phstream/Socks4ProxyConnector.cpp \
    $(COMMON_PATH)/phstream/Socks5ProxyConnector.cpp \
    $(COMMON_PATH)/phstream/UDPLibStream.cpp \
    $(COMMON_PATH)/phstream/UDPLibWrapper.cpp \
    $(COMMON_PATH)/DetectLocalIP.cpp \
    $(COMMON_PATH)/ObjectMonitor/ObjectMonitor.cpp \
    $(COMMON_PATH)/phstream/SSLStream.cpp \
    $(COMMON_PATH)/utf8code/UTF8Code.cpp \
    $(COMMON_PATH)/gzip/gzip_decoder.cpp \
    $(COMMON_PATH)/msg_include/oraymsg.cpp \
    $(COMMON_PATH)/msg_include/oraymsgclient.cpp \
    $(COMMON_PATH)/msg_include/SubScribeClient.cpp \
    $(COMMON_PATH)/http_call/http_call.cpp \
    $(COMMON_PATH)/http_call/HttpObject.cpp \
    $(COMMON_PATH)/urlparser/urlparser.cpp \
    $(COMMON_PATH)/format_message/format_message.cpp \
    $(COMMON_PATH)/libsocket/libsocket.cpp \
    $(COMMON_PATH)/time/timer.cpp \
    $(COMMON_PATH)/crc/crc_16.cpp \
    $(COMMON_PATH)/upnp/upnpnat.cpp \
    $(COMMON_PATH)/upnp/xmlParser.cpp \
    $(COMMON_PATH)/resolve/o_resolve.cpp \
    $(COMMON_PATH)/estr/EString.cpp \
    $(TINYXML_PATH)/tinyxml/tinystr.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxml.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxmlerror.cpp \
    $(TINYXML_PATH)/tinyxml/tinyxmlparser.cpp \
    $(JSON_SRC)/src/lib_json/json_reader.cpp \
    $(JSON_SRC)/src/lib_json/json_value.cpp \
    $(JSON_SRC)/src/lib_json/json_writer.cpp \
    $(SRC_INCLUDE_PATH)/get_proxy_info.cpp \
    $(SRC_INCLUDE_PATH)/DecideTcpClientType.cpp \
    $(SRC_INCLUDE_PATH)/PreConnectProxySvrHandler.cpp \
    $(SRC_INCLUDE_PATH)/ActivePlugin.cpp \
    $(SRC_INCLUDE_PATH)/LicVerifierRaw.cpp \
    $(SRC_INCLUDE_PATH)/PluginStreamImplRaw.cpp \
    $(SRC_INCLUDE_PATH)/DecideMultiChannelClient.cpp \
    $(SRC_INCLUDE_PATH)/MultiChannelStream.cpp \
    $(SRC_INCLUDE_PATH)/PluginThreadManager.cpp
    
################################################
# LOCAL_C_INCLUDES dirs
################################################

LOCAL_C_INCLUDES := \
$(JNI_INTERFACE) \
$(LOCAL_PATH)/$(COMMON_PATH) \
$(LOCAL_PATH)/$(INCLUDE_INCLUDE) \
$(LOCAL_PATH)/$(TINYXML_PATH) \
$(LOCAL_PATH)/$(TINYXML_PATH)/tinyxml \
$(LOCAL_PATH)/$(JSON_SRC)/include \
$(LOCAL_PATH)/$(POLARSSL)/include \
$(LOCAL_PATH)/$(LIBJINGLE_TALK) \
$(LOCAL_PATH)/$(UDPLIB_PATH) \
$(LOCAL_PATH)/$(SRC_INCLUDE_PATH) \
$(LOCAL_PATH)/$(SUNLOGIN_INCLUDE) \
$(LOCAL_PATH)/$(PLUGININTERFACE_PATH) \
$(LOCAL_PATH)/$(CXXOBJECTJNI)


LOCAL_LDLIBS  += -lm -llog -lz -lstdc++

#link static libs
LOCAL_STATIC_LIBRARIES := libudp libpolarssl

#create lib
include $(BUILD_SHARED_LIBRARY)
endif #ifeq

###############################################################################
# extenal static lib
###############################################################################
include $(LOCAL_PATH)/polarssl.mk
include $(LOCAL_PATH)/udplib.mk

