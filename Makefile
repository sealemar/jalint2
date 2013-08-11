#
# define compiler and compiler flag variables
#

JFLAGS = -g
JC = javac


#
# Clear default targets for building .class files from .java files. We
# provide our own target entry for this here. Make has default targets
# for different suffixes (like .c.o). Currently, make does not have a
# definition for this target, but later versions of make may. Good
# practice to clear default definitions.
#

.SUFFIXES: .java .class


#
# Here is our target entry for creating .class files from .java files.
# This entry uses the suffix-rule syntax:
#	DSTS:
#		rule
#  'TS' is the suffix of the target file, 'DS' is the suffix of the dependency 
#  file, and 'rule'  is the rule for building a target	
# '$*' is a built-in macro that gets the basename of the current target 
# Remember that there must be a < tab > before the command line ('rule') 
#

.java.class:
	$(JC) $(JFLAGS) $*.java


#
# CLASSES is a macro consisting of one \-terminated line for each java
# source file)
#

CLASSES = \
	HelloWorld.java \
	Computer.java   \
	UseArgument.java

#
# the default make target entry
#

default: classes


#
# This target entry uses Suffix Replacement within a macro: 
# $(name:string1=string2)
# 	In the words in the macro named 'name', replace 'string1' with 'string2'
# Below we are replacing the suffix .java of all words in the macro CLASSES 
# with the .class suffix
#

classes: $(CLASSES:.java=.class)


#
# RM is a predefined macro in make (RM = rm -f)
#

clean:
	$(RM) *.class
