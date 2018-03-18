Ciphertech Challenge
====================

Building
--------
To build the c/c++ components I have a simple makefile that will create a very
reliable build process. It depends on GYP existing on your path. Alternatively,
I have generated the basic setup for msvs. I haven't personally tested those build
files as I don't use that ide. As a last resort, the files are extremely basic
and can definitely be built by hand on the command line or with minimal setup
in any environment you may be using.

* The c requires c99
* The c++ requires c++11

Generate the build files for your systems native build tool:
`gyp --depth=. --generator-output=build`

This places the build files in a new directory called build.

The java portion I didn't use any specific build tool to create and use. I manually
built these by hand and included a default action in the Makefile.

Build the java: `javac src/steg.java`
Run the java: `CLASSPATH=src java steg steg.bmp`

gyp
----
Should you need or want gyp, it is a simple process to get it.
`git clone https://chromium.googlesource.com/external/gyp deps/gyp`
Then add deps/gyp to your path.

If you are on linux, you can also install them globally.

```
cd deps/gyp
./setup.py build
sudo ./setup.py install
```

Notes
-----

* Part 2 and 3 both have arguments/input, both can either accept it via the command line arguments or stdin.
* I chose the languages for each part due to the following:
  + Part 1 was so simple it made sense to use C
  + Part 2 required a bit more like reading files so I prefered C++
  + Part 3 required some things that aren't standard in C++ without a library/system files so I used java. (Creatig a directory)
* All of my development was to target portable c and c++ so that it could run on linux or windows. Java is obviously cross platform.

