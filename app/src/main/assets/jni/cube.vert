
attribute vec4 vPosition;
attribute vec4 aColor;
varying vec4 vColor;

uniform mat4 uMatrix;

void main() {
	gl_Position = uMatrix * vPosition;
	vColor = aColor;
}
