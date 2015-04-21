JCC = javac
JFLAGS = -g
RUN = java
.SUFFIXES: .java .class
.java.class:
	$(JCC) $(JFLAGS) $*.java

CLASSES = \
	Board.java \
	TFlipFlop.java \
	JKFlipFlop.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	$(RUN) Board
