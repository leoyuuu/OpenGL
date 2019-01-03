package me.leoyuu.glgame

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import me.leoyuu.glgame.game.GameActivity
import me.leoyuu.utils.dp2px

/**
 * date 2019/1/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class GameMainActivity : AppCompatActivity() {
    lateinit var root:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        setContentView(root)
        initGames()
    }

    private fun initGames() {
        GameActivity.GAMES.forEach {
            addGame(it)
        }
    }

    private fun addGame(name:String) {
        val btn = AppCompatButton(this)
        btn.text = name
        btn.setOnClickListener {
            GameActivity.start(this, name)
        }

        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.topMargin = dp2px(10)
        lp.leftMargin = dp2px(16)
        lp.rightMargin = dp2px(16)
        root.addView(btn, lp)
    }
}
