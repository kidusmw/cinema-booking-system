.PHONY: clean ci run db-up db-down check-java

check-java:
	@java -version 2>&1 | grep -q '"21\.' || { \
		echo "❌ Error: Java 21 is required. Please install Java 21 (e.g., via SDKMAN, Adoptium, or your package manager)."; \
		exit 1; \
	}

clean:
	./mvnw clean

db-up:
	docker compose up -d

db-down:
	docker compose down

ci: check-java
	./mvnw clean verify spotless:check spotbugs:check

run: ci
	./run-app.sh
