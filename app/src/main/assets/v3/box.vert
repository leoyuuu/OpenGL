#version 320 es
precision mediump float;

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 vColor;
uniform mat4 matrix;

void main()
{
    gl_Position = matrix * vec4(aPos, 1.0f);
    vColor = aColor;
}