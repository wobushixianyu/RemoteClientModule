# The ARMv7 is significanly faster due to the use of the hardware FPU
APP_ABI := armeabi
#x86 mips arm64-v8a
APP_CPPFLAGS += -fexceptions
APP_CPPFLAGS += -frtti
APP_CPPFLAGS += -fpermissive
APP_CPPFLAGS += -std=gnu++0x
APP_CPPFLAGS += -Wreturn-type

# just for debug(ouput log, data from server)
#APP_CPPFLAGS += -D__DEBUG__

# android platform
APP_CPPFLAGS += -D__ANDROID__ -DPOSIX

# cpp, wide int (wchar_t)
APP_CPPFLAGS += -D_WINT_T

# monitor object(use rtti better), p2p hole, udp, polarssl, jingle lib
APP_CPPFLAGS += -D_DISABLE_OBJECT_MONITORING -DP2P_HOLE_PUNCH -D_USING_UDP -DUSE_POLARSSL -DUSE_LIB_JINGLE

# set toolchain version 
NDK_TOOLCHAIN_VERSION=4.9

APP_STL := gnustl_shared
APP_PLATFORM := android-9