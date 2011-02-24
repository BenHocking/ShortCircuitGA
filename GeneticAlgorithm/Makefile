all:
	mkdir -p classes
	ant all

clean:
	find . -name "*.ser" -delete
	rm -rf edu/virginia/cs/test/data/test_output
	rm -rf edu/virginia/cs/test/data/trace_[0-9]*
	rm -rf classes test-classes instrumented-classes
	rm -f jars/shortcircuitga.jar

coverage:
	rm -rf edu/virginia/cs/test/data/test_output
	rm -rf edu/virginia/cs/test/data/trace_[0-9]*
	mkdir -p classes
	ant _compile
	ant coverage
	open coberta-report/index.html