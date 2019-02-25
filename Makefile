default: Stag

%: %.java
	javac $@.java
	java -ea $@
