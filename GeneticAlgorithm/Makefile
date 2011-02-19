all:
	mkdir -p classes
	ant -f src/build.xml

clean:
	find . -name "*.ser" -delete
	rm -rf classes test-classes instrumented-classes
	rm -f jars/shortcircuitga.jar

coverage:
	mkdir -p classes
	ant -f src/build.xml _compile
	ant -f src/build.xml coverage
	open src/coberta-report/index.html