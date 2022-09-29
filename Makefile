.PHONY: test build help run-rankings-with-file run-rankings-with-stdin run-random

all: test build help run-rankings-with-file run-rankings-with-stdin run-random

build:
	@echo "\nBuilding the cli"
	@echo "-------------------------------------"
	@sbt pack
	@echo "=====================================\n"

test:
	@echo "\nRunning Tests"
	@echo "-------------------------------------"
	@sbt test

help:
	@echo "\nBase help"
	@echo "-------------------------------------"
	@./target/pack/bin/scoreboard -h
	@echo "=====================================\n"

	@echo "Rankings help"
	@echo "-------------------------------------"
	@./target/pack/bin/scoreboard rankings -h
	@echo "=====================================\n"

	@echo "Random help"
	@echo "-------------------------------------"
	@./target/pack/bin/scoreboard random -h
	@echo "====================================="

run-rankings-with-file:
	@echo "\nRun Rankings with file"
	@echo "-------------------------------------"
	@./target/pack/bin/scoreboard rankings ./data.txt
	@echo "====================================="

run-rankings-with-stdin:
	@echo "\nRun Rankings with StdIn "
	@echo "-------------------------------------"
	@cat ./data.txt | ./target/pack/bin/scoreboard rankings -
	@echo "====================================="

run-random:
	@echo "\nRun Random help"
	@echo "-------------------------------------"
	@./target/pack/bin/scoreboard random
	@echo "====================================="

install:
	@echo "\nInstalling...."
	@echo "-------------------------------------"
	@cd target/pack; make install
	@echo "====================================="

