
all: build
	$(MAKE) -C build

build: build.gyp
	gyp --depth=. --generator-output=build

bbs: build
	$(MAKE) -C build bbs

bmp: build
	$(MAKE) -C build bmp

steg: src/steg.java
	javac src/steg.java

clean:
	rm -rf build
	rm src/steg.class
