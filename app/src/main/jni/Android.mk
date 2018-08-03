LOCAL_PATH := $(call my-dir)

subdirs := $(addprefix $(LOCAL_PATH)/,$(addsuffix /Android.mk, \
		libcommon \
		libservicesdk \
	))
	
include $(subdirs)
#include $(call all-subdir-makefiles)
