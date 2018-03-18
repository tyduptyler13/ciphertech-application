{
    "targets": [
        {
            "target_name": "bbs",
            "type": "executable",
            "include_dirs": ["src"],
            "sources": [ "src/bbs.c" ],
            "cflags": [
                "-std=c99"
            ]
        },
        {
            "target_name": "bmp",
            "type": "executable",
            "include_dirs": ["src"],
            "sources": ["src/bmp.cpp"],
            "cflags": [
                "-std=c++11"
            ]
        }
    ]

}