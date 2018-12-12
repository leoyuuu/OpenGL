attribute vec4 vPosition;
uniform mat4 uMatrix;
varying vec4 aColor;

void main(){
    gl_Position = uMatrix * vPosition;
    if (vPosition.z!=0.0) {
        aColor = vec4(1, 0, 0, 1);
    } else {
        aColor = vec4(0, 1, 0, 1);
    }
}