cmake_minimum_required(VERSION 3.10.2)

project("ndk_sample")


#音频渲染
set(OpenSL ${CMAKE_SOURCE_DIR}/opensl)

file(GLOB ALL_CPP ${OpenSL}/*.cpp)

#添加自己编写 cpp 源文件生成动态库
add_library(audiovideo SHARED ${ALL_CPP})

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log )

#最后才开始链接库
target_link_libraries(
        audiovideo

        ${log-lib}

)