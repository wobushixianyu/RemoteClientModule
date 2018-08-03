# Copyright Oray Project
# written by litl(lity_lee@163.com)
# date: 2013-12-06
# last modify: 2014-10-15

# udp lib, which will be built statically

LOCAL_PATH := $(call my-dir)

###############################################################################

# udp static lib
include $(CLEAR_VARS)

LOCAL_MODULE    := libudp

LOCAL_CPPFLAGS  += -D__ANDROID__ -DPOSIX
LOCAL_CPPFLAGS  += -D_DISABLE_OBJECT_MONITORING -DP2P_HOLE_PUNCH -D_USING_UDP -DUSE_POLARSSL -DUSE_LIB_JINGLE

LOCAL_LDLIBS    += -lm -llog -lz -lstdc++

LOCAL_CPP_EXTENSION := .cc .cpp

#path
ROOT_PATH			:= ../../../../../../../../..
THIRD_PATH      	:= ../../../../../../../../../../../3rd

PATH_UDPLIB2_SRC 	:= $(ROOT_PATH)/public/udplib2/src
COMMON_PATH      	:= $(ROOT_PATH)/common
LIBJINGLE_TALK   	:= $(THIRD_PATH)/libjingle
POLARSSL         	:= $(THIRD_PATH)/polarssl-1.3.9

LOCAL_SRC_FILES := \
$(PATH_UDPLIB2_SRC)/udpstack.cpp \
$(PATH_UDPLIB2_SRC)/Bigbit.cpp \
$(PATH_UDPLIB2_SRC)/crypto.cpp \
$(LIBJINGLE_TALK)/talk/base/stream.cc \
$(LIBJINGLE_TALK)/talk/base/asyncfile.cc \
$(LIBJINGLE_TALK)/talk/base/asyncsocket.cc \
$(LIBJINGLE_TALK)/talk/base/bytebuffer.cc \
$(LIBJINGLE_TALK)/talk/base/common.cc \
$(LIBJINGLE_TALK)/talk/base/event.cc \
$(LIBJINGLE_TALK)/talk/base/helpers.cc \
$(LIBJINGLE_TALK)/talk/base/ipaddress.cc \
$(LIBJINGLE_TALK)/talk/base/logging.cc \
$(LIBJINGLE_TALK)/talk/base/messagehandler.cc \
$(LIBJINGLE_TALK)/talk/base/messagequeue.cc \
$(LIBJINGLE_TALK)/talk/base/nethelpers.cc \
$(LIBJINGLE_TALK)/talk/base/physicalsocketserver.cc \
$(LIBJINGLE_TALK)/talk/p2p/base/pseudotcp.cc \
$(LIBJINGLE_TALK)/talk/base/signalthread.cc \
$(LIBJINGLE_TALK)/talk/base/socketaddress.cc \
$(LIBJINGLE_TALK)/talk/base/stringencode.cc \
$(LIBJINGLE_TALK)/talk/base/thread.cc \
$(LIBJINGLE_TALK)/talk/base/timeutils.cc \
$(LIBJINGLE_TALK)/talk/base/timing.cc \
$(LIBJINGLE_TALK)/talk/base/linux.cc \

# 附加包含目录

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/$(COMMON_PATH) \
	$(LOCAL_PATH)/$(PATH_UDPLIB2_SRC) \
	$(LOCAL_PATH)/$(POLARSSL)/include \
	$(LOCAL_PATH)/$(LIBJINGLE_TALK) \
	$(JNI_H_INCLUDE)

include $(BUILD_STATIC_LIBRARY)
