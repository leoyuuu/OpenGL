attribute vec2 aPosition;
attribute vec2 aCoordinate;
uniform mat4 uMatrix;

varying vec2 vCoordinate;

void main(){
    gl_Position = uMatrix * vec4(aPosition, 0, 1);
    vCoordinate = aCoordinate;
}