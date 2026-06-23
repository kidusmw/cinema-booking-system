.PHONY: clean ci run db-up db-down

clean:
	mvn clean

db-up:
	docker compose up -d

db-down:
	docker compose down

ci:
	mvn clean verify spotless:check spotbugs:check

run: db-up ci
	mvn javafx:run
