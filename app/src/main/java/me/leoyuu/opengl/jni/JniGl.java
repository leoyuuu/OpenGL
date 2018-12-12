package me.leoyuu.opengl.jni;

import android.app.Application;
import android.content.res.AssetManager;

/**
 * date 2018/12/8
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
public class JniGl {
    public static final int BTN_LEFT    = 0;
    public static final int BTN_UP      = 1;
    public static final int BTN_RIGHT   = 2;
    public static final int BTN_BOTTOM  = 3;
    public static final int BTN_A       = 4;
    public static final int BTN_B       = 5;
    public static final int BTN_C       = 6;
    public static final int BTN_D       = 7;

    public static final int INIT_TYPE_DEFAULT = 0;
    public static final int INIT_TYPE_SIMPLE  = 1;
    public static final int INIT_TYPE_CUBE    = 2;
    public static final int INIT_TYPE_CAMERA  = 3;
    public static final int INIT_TYPE_V3      = 4;

    static {
        System.loadLibrary("gljni");
    }

    public static native void nativeInit();
    public static native void nativeResize(int width, int height);
    public static native void nativeRender();

    public static native void nativeInitType(int type);
    public static native void nativeDone();
    public static native void nativeClickBtn(int id);
    public static native int  nativeCameraRenderGetTexture();

    private static native void nativeInitAssetManager(AssetManager assetManager);

    public static void init(Application app) {
        nativeInitAssetManager(app.getAssets());
    }
}
