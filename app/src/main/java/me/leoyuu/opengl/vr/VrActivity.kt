package me.leoyuu.opengl.vr

import android.content.Context
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLSurfaceView.RENDERMODE_CONTINUOUSLY
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.gl_view.*
import me.leoyuu.opengl.App
import me.leoyuu.opengl.R

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * date 2018/12/6
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class VrActivity : AppCompatActivity(), GLSurfaceView.Renderer, SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mRotation: Sensor? = null
    private val mSkySphere = SkySphere(App.sp360Bmp)

    private val matrix = FloatArray(16)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gl_view)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mRotation = mSensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        gl.setEGLContextClientVersion(2)
        gl.setRenderer(this)
        gl.renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.registerListener(this, mRotation, SensorManager.SENSOR_DELAY_GAME)
        gl.onResume()
    }

    override fun onPause() {
        super.onPause()
        mSensorManager?.unregisterListener(this)
        gl.onPause()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        mSkySphere.create()
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_FRONT)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        mSkySphere.setSize(width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        mSkySphere.draw()
    }

    override fun onSensorChanged(event: SensorEvent) {
        SensorManager.getRotationMatrixFromVector(matrix, event.values)
        mSkySphere.setMatrix(matrix)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
}
