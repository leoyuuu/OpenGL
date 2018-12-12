attribute vec2 vPosition;

void main(){
    vec2 p = vPosition;
    gl_Position = vec4(p, 0, 1);
}