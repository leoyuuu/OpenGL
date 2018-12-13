#version 320 es
precision mediump float;

out vec4 FragColor;
in vec4 vColor;

void main()
{
    FragColor = vColor;
}