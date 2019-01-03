package me.leoyuu.glgame.game

import android.content.Context
import android.content.Intent
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.leoyuu.glgame.game.shooter.ShooterRender

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GameActivity : AppCompatActivity() {
    private lateinit var gl:GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gl = GLSurfaceView(this)
        setContentView(gl)

        gl.setEGLContextClientVersion(2)
        gl.setRenderer(when(intent.getStringExtra(GAME_NAME)) {
            GAME_SHOOT -> ShooterRender(this)
            else -> EmptyRender(this)
        })
        gl.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onPause() {
        super.onPause()
        gl.onPause()
    }

    override fun onResume() {
        super.onResume()
        gl.onResume()
    }

    companion object {
        private const val GAME_NAME = "game_name"

        private const val GAME_SHOOT = "shoot"
        private const val GAME_EMPTY = "nothing"

        val GAMES = arrayOf(
            GAME_EMPTY,
            GAME_SHOOT
        )

        fun start(context: Context, name:String) {
            context.startActivity(Intent(context, GameActivity::class.java).apply {
                putExtra(GAME_NAME, name)
            })
        }
    }
}
