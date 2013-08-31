JNI_INCLUDE     := $(JAVA_HOME)/include
JNI_INCLUDE_MD  := $(JNI_INCLUDE)/darwin
JNI_SRCDIR      := jni
JNI_BUILDDIR    := $(BUILDDIR)/jni
CC              := cc
CC_FLAGS        := -O3
C_INCLUDE       := -I$(JNI_INCLUDE) -I$(JNI_INCLUDE_MD)
C_LIBS          := -lflint -lgmp
#
# TODO: system dependent { LD_LIBRARY_PATH | DYLD_LIBRARY_PATH }
#
LD_LIBRARY_PATH_ENV_VAR_NAME := DYLD_LIBRARY_PATH
LD_LIBRARY_PATH := $(JNI_BUILDDIR):$(LD_LIBRARY_PATH)

ifdef DEBUG
    CC_FLAGS := -g -O0
endif

CC_FLAGS     += $(CC_DEBUG) -Weverything -shared -std=c99 -x c

# TODO: *.dylib -> system dependent { *.dyld | *.so }
# TODO: search for the JNI libraries automaticly

.PHONY: jni
jni:
	@echo
	@echo Building JNI libs
	@echo -----------------
	@mkdir -p $(JNI_BUILDDIR)
	$(CC) $(CC_FLAGS) $(C_INCLUDE) $(C_LIBS) -o $(JNI_BUILDDIR)/libjalint.dylib $(JNI_SRCDIR)/libjalint/libjalint.c
