C_CTAGS         = $(ETCDIR)/c.ctags
JAVA_CTAGS      = $(ETCDIR)/java.ctags
CTAGS           = ctags --sort=yes --fields=+ilaS --extra=+q

all_ctags_c    := all_ctags_c
all_ctags_java := all_ctags_java

.PHONY: ctags ctags-c ctags-java $(all_ctags_c) $(all_ctags_java)
.INTERMEDIATE: $(all_ctags_c) $(all_ctags_java)

ctags: ctags-c ctags-java

$(all_ctags_c):
	@find $(JNI_SRCDIR) -type f -name "*.[ch]" > $@

ctags-c: $(all_ctags_c)
	@echo
	@echo Building $(C_CTAGS)
	@echo ----------------------
	@mkdir -p $(ETCDIR)
	$(CTAGS) --language-force=C --c-kinds=+px -L "$<" -f $(C_CTAGS)

$(all_ctags_java):
	@find $(SRCDIR) -type f -name "*.java" > $@

ctags-java: $(all_ctags_java)
	@echo
	@echo Building $(JAVA_CTAGS)
	@echo ----------------------
	@mkdir -p $(ETCDIR)
	$(CTAGS) --language-force=Java -L "$<" -f $(JAVA_CTAGS)
