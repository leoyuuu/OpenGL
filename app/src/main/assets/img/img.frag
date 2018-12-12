precision mediump float;

const float PI = 3.14159265;
const float uD = 80.0; //旋转角度
const float uR = 0.5; //旋转半径

uniform sampler2D uTexture;
uniform int iType;
uniform vec2 uTextureSize;
varying vec2 vCoordinate;

vec2 mosaicSize = vec2(8, 8);

vec4 dip_filter(mat3 _filter, sampler2D _image, vec2 _xy, vec2 texSize)               
{
    mat3 _filter_pos_delta_x=mat3(vec3(-1.0, 0.0, 1.0), vec3(0.0, 0.0 ,1.0) ,vec3(1.0,0.0,1.0));
    mat3 _filter_pos_delta_y=mat3(vec3(-1.0,-1.0,-1.0),vec3(-1.0,0.0,0.0),vec3(-1.0,1.0,1.0));
    vec4 final_color = vec4(0.0, 0.0, 0.0, 0.0);
    for(int i = 0; i<3; i++)
    {
        for(int j = 0; j<3; j++)
        {
            vec2 _xy_new = vec2(_xy.x + _filter_pos_delta_x[i][j], _xy.y + _filter_pos_delta_y[i][j]);
            vec2 _uv_new = vec2(_xy_new.x/texSize.x, _xy_new.y/texSize.y);
            final_color += texture2D(_image,_uv_new) * _filter[i][j];
        }
    }
    return final_color;
}

vec4 vortex() {
    ivec2 ires = ivec2(512, 512);
    float Res = float(ires.s);

    vec2 st = vCoordinate;
    float Radius = Res * uR;

    vec2 xy = Res * st;

    vec2 dxy = xy - vec2(Res/2., Res/2.);
    float r = length(dxy);

    float beta = atan(dxy.y, dxy.x) + radians(uD) * 2.0 * (-(r/Radius)*(r/Radius) + 1.0);//(1.0 - r/Radius);

    vec2 xy1 = xy;
    if(r<=Radius)
    {
        xy1 = Res/2. + r*vec2(cos(beta), sin(beta));
    }

    st = xy1/Res;

    return vec4(texture2D(uTexture, st).rgb, 1.0);
}

void main(){
    vec4 vCameraColor=texture2D(uTexture, vCoordinate);
    if(iType == 1) {
        float fGrayColor = (0.3 * vCameraColor.r + 0.59 * vCameraColor.g + 0.11 * vCameraColor.b);
        gl_FragColor = vec4(vec3(fGrayColor), 1.0);
    } else if(iType == 2) {
        float fColor = vCameraColor.r;
        gl_FragColor = vec4(fColor, 0, 0, 1);
    } else if (iType == 3){
        float fColor = vCameraColor.g;
        gl_FragColor = vec4(0, fColor, 0, 0);
    } else if (iType == 4) {
        float fColor = vCameraColor.b;
        gl_FragColor = vec4(0, 0, fColor, 0);
    } else if(iType == 5){
        vec2 upLeftUV = vec2(vCoordinate.x - 1.0/uTextureSize.x, vCoordinate.y - 1.0/uTextureSize.y);
        vec4 upLeftColor = texture2D(uTexture, upLeftUV);
        vec4 delColor = vCameraColor - upLeftColor;
        float h = 0.3 * delColor.x + 0.59 * delColor.y + 0.11 * delColor.z;
        vec4 bkColor = vec4(0.5, 0.5, 0.5, 1.0);
        gl_FragColor = vec4(h, h, h, 0.0) + bkColor;
    } else if(iType == 6){
        vec2 intXY = vec2(vCoordinate.x * uTextureSize.x, vCoordinate.y * uTextureSize.y);
        vec2 XYMosaic = vec2(floor(intXY.x / mosaicSize.x) * mosaicSize.x, floor(intXY.y / mosaicSize.y) * mosaicSize.y);
        vec2 UVMosaic = vec2(XYMosaic.x / uTextureSize.x , XYMosaic.y / uTextureSize.y);
        vec4 baseMap = texture2D(uTexture, UVMosaic);
        gl_FragColor = baseMap;
    } else if(iType == 7) {
        vec2 intXY = vec2(vCoordinate.x * uTextureSize.x, vCoordinate.y * uTextureSize.y);
        mat3 _smooth_fil = mat3(1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0,1.0/9.0);
        vec4 tmp = dip_filter(_smooth_fil, uTexture, intXY, uTextureSize);
        gl_FragColor = tmp;
    } else if(iType == 8) {
        gl_FragColor = vortex();
    }
    else {
        gl_FragColor = vCameraColor;
    }
}