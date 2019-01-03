package me.leoyuu.opengl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import me.leoyuu.glgame.GameMainActivity
import me.leoyuu.opengl.camera.CameraActivity
import me.leoyuu.opengl.ctrl.jump.JumpActivity
import me.leoyuu.opengl.esv3.V3Activity
import me.leoyuu.opengl.fbo.FrameBufferActivity
import me.leoyuu.opengl.img.ImgActivity
import me.leoyuu.opengl.jni.JniActivity
import me.leoyuu.opengl.list.ItemView
import me.leoyuu.opengl.list.RenderItem
import me.leoyuu.opengl.nrenders.BasePreRender
import me.leoyuu.opengl.nrenders.GlActivity
import me.leoyuu.opengl.obj.ObjViewActivity
import me.leoyuu.opengl.tmp.TmpActivity
import me.leoyuu.opengl.vr.VrActivity
import me.leoyuu.rv.BaseOnItemClickListener
import me.leoyuu.rv.BaseRvAdapter

class MainActivity : AppCompatActivity() , BaseOnItemClickListener<RenderItem>{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkList()
    }

    private fun checkList() {
        val adapter = BaseRvAdapter(ItemView::class.java)
        lv.adapter = adapter
        lv.layoutManager = LinearLayoutManager(this)
        adapter.onItemClickListener = this

        adapter.refresh(mutableListOf<RenderItem>().apply {
            add(RenderItem(TmpActivity::class.java, "Tmp"))
            addAll(BasePreRender.getRenderList())
            add(RenderItem(VrActivity::class.java, "VR 模拟"))
            add(RenderItem(JumpActivity::class.java, "跳跃"))
            add(RenderItem(CameraActivity::class.java, "相机"))
            add(RenderItem(ImgActivity::class.java, "图片纹理"))
            add(RenderItem(JniActivity::class.java, "原生层绘制"))
            add(RenderItem(ObjViewActivity::class.java, "3d max obj 模型绘制"))
            add(RenderItem(V3Activity::class.java, "OpenGL 3.0"))
            add(RenderItem(FrameBufferActivity::class.java, "FBO 初探"))
            add(RenderItem(GameMainActivity::class.java, "GL 小游戏"))
        })
    }

    override fun onItemClick(index: Int, data: RenderItem) {
        if (data.a is BasePreRender) {
            GlActivity.start(this, data.a)
        } else if (data.a is Class<*>) {
            startActivity(Intent(this, data.a))
        }
    }
}
