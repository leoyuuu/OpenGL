
uniform mat4 uMatrix;
attribute vec2 vPosition;

void main(){
    gl_Position = uMatrix * vec4(vPosition, 0, 1);
}

