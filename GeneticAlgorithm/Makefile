all:
	mkdir -p classes
	ant all

clean:
	find . -name "*.ser" -delete
	rm -rf classes test-classes instrumented-classes
	rm -f jars/shortcircuitga.jar

coverage:
	mkdir -p classes
	ant _compile
	ant coverage
	open coberta-report/index.html