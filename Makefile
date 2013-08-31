# make           - does compile and jar
# make all       - compile and build everything
# make build     - synonym to make jar
# make clean     - clean
# make compile   - compile *.java -> *.class
# make debug     - executes main class in jdb. Run 'make build' first
# make jar       - builds project.jar
# make jni       - builds native library for JNI calls
# make help      - this help
# make run       - executes main class. Run 'make build' first
# make test-repl - runs 'lein repl' with project.jar injected and runs etc/lein-repl.clj in it. Run 'make all' first
# make ctags     - rebuild ctags in etc/java.ctags, etc/c.ctags
#
#
# mkdir -p build/classes
#
# javac -sourcepath src -classpath lib/jna-4.0.0.jar:lib/stdlib-package.jar -d build/classes src/com/rebsea/jalint/HelloWorld.java
# java -cp build/classes:lib/stdlib-package.jar:lib/jna-4.0.0.jar com.rebsea.jalint.HelloWorld
#
# jar cfm project.jar MANIFEST.MF -C bin .
#
# java -jar project.jar
#
# CLASSPATH=project.jar lein repl < lein-repl.clj
#
# MANIFEST.MF
#
# Main-Class: com.rebsea.jalint.HelloWorld
# Class-Path: lib/jna-4.0.0.jar lib/stdlib-package.jar
# Created-By: reb-cabin & sealemar in the name of goodness
#
# lein-repl.clj
#
# (import [com.rebsea.jalint HelloWorld])
# (import [com.rebsea.jalint HelloWorld$Flint])
# (HelloWorld$Flint/INSTANCE)
# (.flint_malloc HelloWorld$Flint/INSTANCE 32)
#
# following http://stackoverflow.com/questions/1953048/java-project-structure-explained-for-newbies
# default J2SE folder structure


RM := rm -fr

HEADER_HELP_LINES := 12

#DEBUG     := 1
BUILDDIR  := build
SRCDIR    := src
LIBDIR    := lib
ETCDIR    := etc


#
# A phony target is one that is not really the name of a file; rather it is
# just a name for a recipe to be executed when you make an explicit request.
# There are two reasons to use a phony target: to avoid a conflict with a file
# of the same name, and to improve performance.
#

#
# this should be the first recipe
# make, if started with no arguments, runs the first recipe from Makefile
#
.PHONY: all
all: compile jar

include jni.mk
include ctags.mk

JAVA_BUILDDIR  := $(BUILDDIR)/java
PROJECT_JAR_NAME := jalint.jar
MANIFEST  := $(ETCDIR)/MANIFEST.MF
LEIN_REPL_TEST_FILE := $(ETCDIR)/lein-repl.clj

JC_DEBUGOPT := -g:none
JAVA_FLAGS  = -cp $(classpath)
ifdef DEBUG
    JC_DEBUGOPT := -g
    JAVA_FLAGS  = -cp $(classpath) -ea -esa -Xfuture
endif

JFLAGS    = $(JC_DEBUGOPT) -classpath $(classpath) -sourcepath $(SRCDIR) -d $(JAVA_BUILDDIR)
JC        := javac
JAVA      = $(LD_LIBRARY_PATH_ENV_VAR_NAME)=$(LD_LIBRARY_PATH) java
JDB       = $(LD_LIBRARY_PATH_ENV_VAR_NAME)=$(LD_LIBRARY_PATH) jdb
LEIN_REPL = $(LD_LIBRARY_PATH_ENV_VAR_NAME)=$(LD_LIBRARY_PATH) CLASSPATH=$(PROJECT_JAR_NAME) lein repl
MAINCLASS := com.rebsea.jalint.HelloWorld

MAKE_BUILD_WARNING := echo "Something is wrong, try 'make compile' first"
MAKE_ALL_WARNING   := echo "Something is wrong, try 'make all' first"

# $(call make-classpath)
make-classpath =      \
    $(patsubst %:,%,  \
        $(shell find $(LIBDIR) -name "*.jar" -type f -exec echo -n '{}': \;))

classpath = $(call make-classpath)

CP = $(JAVA_BUILDDIR):$(classpath)

# See - http://www.gnu.org/software/make/manual/make.html#Makefile-Basics
#
# Different make programs have incompatible suffix lists and implicit rules,
# and this sometimes creates confusion or misbehavior. So it is a good idea
# to set the suffix list explicitly using only the suffixes you need in the
# particular Makefile.
#
# From - http://www.gnu.org/software/make/manual/make.html#Suffix-Rules
#
# If you wish to eliminate the default known suffixes instead of just
# adding to them, write a rule for .SUFFIXES with no prerequisites. By
# special dispensation, this eliminates all existing prerequisites of
# .SUFFIXES. You can then write another rule to add the suffixes you want.
#
# Delete the default suffixes
.SUFFIXES:
.SUFFIXES: .java .class

.PHONY: help
help:
	@$(call brief-help, $(CURDIR)/Makefile)

#
# generate all_javas file by making a list of *.java filenames found
# under $(SRCDIR)
#
all_javas := all.javas

# make all_javas INTERMEDIATE - auto remove when done
.PHONY: $(all_javas)
.INTERMEDIATE: $(all_javas)
$(all_javas):
	find $(SRCDIR) -type f -name "*.java" > $@

# $(call brief-help, makefile)
define brief-help
    head -n $(HEADER_HELP_LINES) Makefile | sed 's/# //'
endef

#
# use all_javas as an input file where all targets are listed
#
.PHONY: compile
compile: $(all_javas) jni
	@echo
	@echo Compiling Java classes
	@echo ----------------------
	@mkdir -p $(JAVA_BUILDDIR)
	$(JC) $(JFLAGS) @$<

.PHONY: build
build: jar

.PHONY: run
run:
	$(JAVA) -cp $(CP) $(MAINCLASS) foo bar baz qux || $(MAKE_BUILD_WARNING)

.PHONY: debug
debug:
	$(JDB) -classpath $(CP) $(MAINCLASS) foo bar baz qux || $(MAKE_BUILD_WARNING)

.PHONY: jar
jar: compile
	jar cfm $(PROJECT_JAR_NAME) $(MANIFEST) -C $(JAVA_BUILDDIR) . || $(MAKE_BUILD_WARNING)

.PHONY: test-repl
test-repl:
	$(LEIN_REPL) < $(LEIN_REPL_TEST_FILE) || $(MAKE_ALL_WARNING)

.PHONY: clean
clean:
	@$(RM) $(BUILDDIR) $(PROJECT_JAR_NAME)
