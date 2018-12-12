attribute vec3 vPosition;
uniform mat4 uMatrix;
attribute vec3 aColor;
varying vec4 vColor;

void main(){
    gl_Position = uMatrix * vec4(vPosition,1);
	vColor = vec4(aColor, 1.0);
}