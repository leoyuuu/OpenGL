cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")

add_library(
        gljni
        SHARED
        draw/draw.h
        draw/draw.cpp
        draw/gl_helper.h
        draw/gl_helper.cpp
        draw/renders/Render.cpp
        draw/renders/RenderCamera.cpp
        draw/renders/RenderCube.cpp
        draw/renders/RenderSimple.cpp
        native_helper.h
        native_helper.cpp
        native_gl.h
        native_gl.cpp)

# add lib dependencies
target_link_libraries(
        gljni
        android
        log
        EGL
        GLESv2)

