package me.leoyuu.opengl.list

import android.content.Context
import android.graphics.Color
import android.opengl.GLES30
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import me.leoyuu.opengl.R
import me.leoyuu.rv.BaseRvItemView

/**
 * date 2018/12/5
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class ItemView(context: Context) : BaseRvItemView<RenderItem>(context) {
    private val btn = AppCompatButton(context)
    init {
        addView(btn)
        setPadding(48, 3, 48, 0)
        btn.setTextColor(Color.WHITE)
        btn.setBackgroundResource(R.drawable.btn)
        btn.gravity = Gravity.CENTER
        btn.setOnClickListener {
            performClick()
        }
    }

    override fun onBindData(data: RenderItem) {
        btn.text = data.desc
    }
}
