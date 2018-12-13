#version 320 es
precision mediump float;

uniform vec4 uColor;

out vec4 FragColor;
void main() {
	FragColor = uColor;
}
