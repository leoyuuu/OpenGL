#version 320 es
layout(location = 0) in vec3 aPosition;
layout(location = 1) in vec3 aColor;

out vec3 ourColor; // 向片段着色器输出一个颜色

void main() {
	gl_Position = vec4(aPosition, 1.0);
	ourColor = aColor;
}
