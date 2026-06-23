.PHONY: ci run

ci:
	mvn clean verify spotless:check spotbugs:check pmd:check

run: ci
	mvn javafx:run
