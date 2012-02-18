.PHONY: all clean coverage javadocs

all: coverage javadocs
	mvn install

clean:
	mvn clean

coverage:
	mvn clean cobertura:cobertura
	open target/site/cobertura/index.html

javadocs:
	mvn javadoc:javadoc
	open target/site/apidocs/index.html
