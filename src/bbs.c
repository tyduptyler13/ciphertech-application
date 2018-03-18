
#include <string.h>
#include <stdio.h>

unsigned char* Crypt(unsigned char* data, int dataLength, unsigned int value){
    //Need extra space to overflow the value so that 0x10000 * 0x10000 != 0
    value = ((unsigned long long) value * value) % 0xE2089EA5;

    for (int i = 0; i < dataLength; ++i, value = ((unsigned long long) value * value) % 0xE2089EA5){
        data[i] ^= (unsigned char) value;
    }

    return data;

}

int main(int argc, char** argv){

    const char* appleOrig = "apple";
    unsigned char apple[] = "apple";
    unsigned char aout[] = {0x4C,0x88,0x9E,0xDF,0xE8};

    Crypt(apple, 5, 0x12345678);

    if (strncmp((char*) apple, (char*) aout, 5) == 0){
        printf("Pass\n");
    } else {
        printf("Fail\n");
    }

    Crypt(apple, 5, 0x12345678);

    if (strncmp(appleOrig, (char*) apple, 5) == 0){
        printf("Pass\n");
    } else {
        printf("Fail\n");
    }

}
