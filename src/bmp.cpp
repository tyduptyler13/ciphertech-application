#include <cstdint>
#include <vector>
#include <array>
#include <fstream>
#include <iostream>

using namespace std;

#pragma pack(1)

struct bmp {
    array<char, 2> name;
    uint32_t size;
    uint32_t _reserved; //We have no use for these 4 bytes
    uint32_t dataOffset;
    uint32_t DIBSize;
};

struct windib {
    //Already got the size in bmp
    uint32_t width;
    uint32_t height;
    uint16_t colorPlanes;
    uint16_t bpp; //Bits per pixel
    uint32_t compMethod;
    uint32_t size;
    uint32_t ppmx; //Horiz pixel per meter
    uint32_t ppmy; //Vert pixel per meter
    uint32_t paletteSize;
    uint32_t impColors; //Important Colors
};

struct os21x {
    //Already got the size in bmp
    uint16_t width;
    uint16_t height;
    uint16_t colorPlanes;
    uint16_t bpp;
};

#pragma pack() //Don't need packing anymore

int main(int argc, char** argv) {

    string file;

    if (argc < 2){
        cin >> file;
    } else {
        file = argv[1];
    }

    ifstream in(file, ios_base::in | std::ifstream::binary);

    if (!in) {
        cerr << "Failed to open the file" << endl;
        return 1;
    }

    vector<char> buf(sizeof(bmp));

    in.read(buf.data(), buf.size());

    if (!in.good()) { //This will happen if we run out of data.
        cerr << "Invalid BMP file. File too short" << endl;
        return 1;
    }

    if (buf[0] != 'B' || buf[1] != 'M') {
        cerr << "Not a recognized bitmap file." << endl;
        return 1;
    }

    bmp* header = reinterpret_cast<bmp*>(buf.data());

    if (header->DIBSize == 12) { // OS/2 1.x

        buf.resize(sizeof(os21x));

        in.read(buf.data(), buf.size());
        //bmp is now invalid, don't use it.

        os21x* dib = reinterpret_cast<os21x*>(buf.data());

        cout << "Width: " << dib->width << "\nHeight: " << dib->height << endl;

    } else if (header->DIBSize == 40) { // Windows

        buf.resize(sizeof(windib));

        in.read(buf.data(), buf.size());
        //bmp is now invalid, don't use it.

        windib* dib = reinterpret_cast<windib*>(buf.data());

        cout << "Width: " << dib->width << "\nHeight: " << dib->height << endl;

    } else { // There are a few other headers, adobe, etc.
        cerr << "Unsupported BMP dib header" << endl;
        return 1;
    }

}
