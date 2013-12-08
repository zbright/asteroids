all: generate

	javac -d . src/asteroids/*.java

clean:
	rm -rf classes build
	rm -rf bin
	rm *.class

.PHONY: all generate clean
