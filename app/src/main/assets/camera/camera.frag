#extension GL_OES_EGL_image_external : require
precision mediump float;

uniform samplerExternalOES sTexture;
varying vec2 vCoordinate;

vec4 grey(vec4 vCameraColor) {
    float fGrayColor = (0.3 * vCameraColor.r + 0.59 * vCameraColor.g + 0.11 * vCameraColor.b);
    return vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
}

void main(){
    vec4 vCameraColor = texture2D(sTexture, vCoordinate);
    gl_FragColor = grey(vCameraColor);
}