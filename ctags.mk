C_JNI_CTAGS       = $(ETCDIR)/c.jni.ctags
C_TEST_CTAGS      = $(ETCDIR)/c.test.ctags
JAVA_CTAGS        = $(ETCDIR)/java.ctags
CTAGS             = ctags --sort=yes --fields=+ilaS --extra=+q

all_ctags_jni    := all_ctags_jni
all_ctags_c_test := all_ctags_c_test
all_ctags_java   := all_ctags_java

.PHONY: clean-ctags ctags ctags-c-jni ctags-c-test ctags-c ctags-java \
	$(all_ctags_c_test) $(all_ctags_jni) $(all_ctags_java)
.INTERMEDIATE: $(all_ctags_c_test) $(all_ctags_jni) $(all_ctags_java)

ctags: ctags-c ctags-java

ctags-c: ctags-c-jni ctags-c-test

#
# $(C_JNI_CTAGS)
#

$(all_ctags_jni):
	@find $(JNI_SRCDIR) -type f -name "*.[ch]" > $@

ctags-c-jni: $(all_ctags_jni)
	@echo
	@echo Building $(C_JNI_CTAGS)
	@echo ----------------------
	@mkdir -p $(ETCDIR)
	$(CTAGS) --language-force=C --c-kinds=+px -L "$<" -f "$(C_JNI_CTAGS)"

#
# $(C_TEST_CTAGS)
#

$(all_ctags_c_test):
	@find $(TEST_C_SRC_DIR) -type f -name "*.[ch]" > $@

ctags-c-test:  $(all_ctags_c_test)
	@echo
	@echo Building $(C_TEST_CTAGS)
	@echo ----------------------
	@mkdir -p $(ETCDIR)
	$(CTAGS) --language-force=C --c-kinds=+px -L "$<" -f "$(C_TEST_CTAGS)"

#
# $(JAVA_CTAGS)
#

$(all_ctags_java):
	@find $(SRCDIR) -type f -name "*.java" > $@

ctags-java: $(all_ctags_java)
	@echo
	@echo Building $(JAVA_CTAGS)
	@echo ----------------------
	@mkdir -p $(ETCDIR)
	$(CTAGS) --language-force=Java -L "$<" -f "$(JAVA_CTAGS)"

clean-ctags:
	$(RM) "$(C_JNI_CTAGS)" "$(C_TEST_CTAGS)" "$(JAVA_CTAGS)"
