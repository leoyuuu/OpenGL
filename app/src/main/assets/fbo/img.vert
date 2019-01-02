#version 320 es
in vec4 aPosition;
in vec2 aCoordinate;

out vec2 vCoordinate;

void main(){
    gl_Position=aPosition;
    vCoordinate=aCoordinate;
}