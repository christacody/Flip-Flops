JCC = javac
JFLAGS = -g
RUN = java

default: Board.class

Board.class: Board.java
	$(JCC) $(JFLAGS) Board.java

clean:
	$(RM) *.class

run:
	$(RUN) Board
