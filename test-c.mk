TEST_BUILDDIR   := $(BUILDDIR)/$(TESTDIR)/c
TEST_EXE_RESULT := $(TEST_BUILDDIR)/test
TEST_C_SRC_DIR  := $(TESTDIR)/c

.PHONY: test-c

SRC_FILES = $(patsubst %, %/*.c, $(TEST_C_SRC_DIR))

.PHONY: test-c
test-c:
	@echo
	@echo Building C tests
	@echo -----------------
	@mkdir -p $(TEST_BUILDDIR)
	$(CC) $(CC_FLAGS) $(C_LIBS) -I$(TEST_C_SRC_DIR) -o $(TEST_EXE_RESULT) $(SRC_FILES)
