# Copyright Oray Project
# written by litl(lity_lee@163.com)
# date: 2013-12-06
# last modify: 2014-10-15

# udp lib, which will be built statically

LOCAL_PATH := $(call my-dir)

###############################################################################

# polarssl static lib
include $(CLEAR_VARS)

LOCAL_MODULE    := libpolarssl

LOCAL_CPPFLAGS  += -D__ANDROID__ -DPOSIX

LOCAL_CPP_EXTENSION := .cc .cpp

#path
ROOT_PATH	:= ../../../../../../../../..
THIRD_PATH  := ../../../../../../../../../../../3rd

POLARSSL 	:= $(THIRD_PATH)/polarssl-1.3.9

LOCAL_SRC_FILES := \
$(POLARSSL)/library/aes.c \
$(POLARSSL)/library/aesni.c \
$(POLARSSL)/library/arc4.c \
$(POLARSSL)/library/asn1parse.c \
$(POLARSSL)/library/asn1write.c \
$(POLARSSL)/library/base64.c \
$(POLARSSL)/library/bignum.c \
$(POLARSSL)/library/blowfish.c \
$(POLARSSL)/library/camellia.c \
$(POLARSSL)/library/ccm.c \
$(POLARSSL)/library/certs.c \
$(POLARSSL)/library/cipher.c \
$(POLARSSL)/library/cipher_wrap.c \
$(POLARSSL)/library/ctr_drbg.c \
$(POLARSSL)/library/debug.c \
$(POLARSSL)/library/des.c \
$(POLARSSL)/library/dhm.c \
$(POLARSSL)/library/ecdh.c \
$(POLARSSL)/library/ecdsa.c \
$(POLARSSL)/library/ecp.c \
$(POLARSSL)/library/ecp_curves.c \
$(POLARSSL)/library/entropy.c \
$(POLARSSL)/library/entropy_poll.c \
$(POLARSSL)/library/error.c \
$(POLARSSL)/library/gcm.c \
$(POLARSSL)/library/havege.c \
$(POLARSSL)/library/hmac_drbg.c \
$(POLARSSL)/library/md.c \
$(POLARSSL)/library/md_wrap.c \
$(POLARSSL)/library/md2.c \
$(POLARSSL)/library/md4.c \
$(POLARSSL)/library/md5.c \
$(POLARSSL)/library/memory_buffer_alloc.c \
$(POLARSSL)/library/net.c \
$(POLARSSL)/library/oid.c \
$(POLARSSL)/library/padlock.c \
$(POLARSSL)/library/pbkdf2.c \
$(POLARSSL)/library/pem.c \
$(POLARSSL)/library/pk.c \
$(POLARSSL)/library/pk_wrap.c \
$(POLARSSL)/library/pkcs11.c \
$(POLARSSL)/library/pkcs12.c \
$(POLARSSL)/library/pkcs5.c \
$(POLARSSL)/library/pkparse.c \
$(POLARSSL)/library/pkwrite.c \
$(POLARSSL)/library/platform.c \
$(POLARSSL)/library/ripemd160.c \
$(POLARSSL)/library/rsa.c \
$(POLARSSL)/library/sha1.c \
$(POLARSSL)/library/sha256.c \
$(POLARSSL)/library/sha512.c \
$(POLARSSL)/library/ssl_cache.c \
$(POLARSSL)/library/ssl_ciphersuites.c \
$(POLARSSL)/library/ssl_cli.c \
$(POLARSSL)/library/ssl_srv.c \
$(POLARSSL)/library/ssl_tls.c \
$(POLARSSL)/library/threading.c \
$(POLARSSL)/library/timing.c \
$(POLARSSL)/library/version.c \
$(POLARSSL)/library/version_features.c \
$(POLARSSL)/library/x509.c \
$(POLARSSL)/library/x509_create.c \
$(POLARSSL)/library/x509_crl.c \
$(POLARSSL)/library/x509_crt.c \
$(POLARSSL)/library/x509_csr.c \
$(POLARSSL)/library/x509write_crt.c \
$(POLARSSL)/library/x509write_csr.c \
$(POLARSSL)/library/xtea.c \

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/$(POLARSSL)/include \
	$(JNI_H_INCLUDE)

include $(BUILD_STATIC_LIBRARY)
