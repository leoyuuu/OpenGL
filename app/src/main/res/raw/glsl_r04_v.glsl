attribute vec4 vPosition;
attribute vec4 aColor;
uniform mat4 uMatrix;

varying vec4 vColor;

void main(){
    gl_Position = uMatrix * vPosition;
    vColor = aColor;
}