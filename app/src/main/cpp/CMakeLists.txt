cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")

add_library(
        gljni
        SHARED
        draw/draw.cpp
        draw/ImgInfo.cpp
        draw/gl_helper.cpp
        draw/renders/Render.cpp
        draw/renders/RenderCamera.cpp
        draw/renders/RenderCube.cpp
        draw/renders/RenderSimple.cpp
        draw/renders/v3/RenderV3.cpp
        native_helper.cpp
        native_gl.cpp
        stb/stb_image.h)

set_target_properties(gljni
  PROPERTIES
    CXX_STANDARD 11
    CXX_STANDARD_REQUIRED YES
    CXX_EXTENSIONS NO
    INTERFACE_INCLUDE_DIRECTORIES $<TARGET_PROPERTY:gljni,INCLUDE_DIRECTORIES>
)

# add lib dependencies
target_link_libraries(
        gljni
        android
        log
        EGL
        GLESv2
        GLESv3
)

