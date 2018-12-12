attribute vec2 vPosition;
uniform mat4 uMatrix;

void main(){
    gl_Position = uMatrix * vec4(vPosition, 0, 1);
}