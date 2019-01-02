#version 320 es
precision mediump float;

uniform sampler2D uTexture;
in vec2 vCoordinate;
out vec4 FragColor;
void main(){
    FragColor=texture(uTexture, vCoordinate);
}