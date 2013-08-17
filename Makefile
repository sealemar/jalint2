#
# define compiler and compiler flag variables
#

CP     = "./:./com/sun/jna/*:./stdlib/*"
JFLAGS = -g -cp $(CP)
JC     = javac


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
#
# Delete the default suffixes
.SUFFIXES:
.SUFFIXES: .java .class


#
# Here is our target entry for creating .class files from .java files.
# Remember that there must be a < tab > before the command line ('rule')
#
# http://www.gnu.org/software/make/manual/make.html#Pattern-Intro
# http://www.gnu.org/software/make/manual/make.html#Pattern-Match
#
# don't use Old-Fashioned suffixes rules because
#
# http://www.gnu.org/software/make/manual/make.html#Suffix-Rules
# http://www.gnu.org/software/make/manual/make.html#Automatic-Variables

%.class : %.java
	$(JC) $(JFLAGS) $<


#
# CLASSES is a macro consisting of one \-terminated line for each java
# source file)
#

CLASSES = \
	HelloWorld.java                  \
	SameDir.java                     \
	computer_package/Computer.java   \
	UseArgument.java


#
# the default make target entry
#

run: classes
	java -cp $(CP) HelloWorld foo bar baz qux

debug: classes
	jdb -classpath $(CP) HelloWorld foo bar baz qux

default: run


#
# This target entry uses Suffix Replacement within a macro:
# $(name:string1=string2)
# 	In the words in the macro named 'name', replace 'string1' with 'string2'
# Below we are replacing the suffix .java of all words in the macro CLASSES
# with the .class suffix
#

classes: $(CLASSES:.java=.class)



# From - http://www.gnu.org/software/make/manual/make.html#Phony-Targets
#
# A phony target is one that is not really the name of a file; rather it is
# just a name for a recipe to be executed when you make an explicit request.
# There are two reasons to use a phony target: to avoid a conflict with a file
# of the same name, and to improve performance.

.PHONY: clean

#
# RM is a predefined macro in make (RM = rm -f)
# @ in front of a command makes it silent
#
clean:
	@$(RM) *.class

