package me.leoyuu.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * date 2018/5/24
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
object ThreadUtil {
    private var isAndroid = false
    private var otherExecutor = Executors.newCachedThreadPool()

    private var uiExecutor = Executor {
        if (isAndroid) {
            checkInitHandler()
            handler.post(it)
        } else {
            uiExecutorImpl.submit(it)
        }
    }

    internal fun initUtil(isAndroid: Boolean) {
        ThreadUtil.isAndroid = isAndroid
    }

    fun runOnUiThread(runnable: () -> Unit) {
        uiExecutor.execute(runnable)
    }

    fun runOnChildThread(runnable: () -> Unit) {
        otherExecutor.submit(runnable)
    }

    private fun checkInitHandler(){
        if (!initialized) {
            handler = Handler(Looper.getMainLooper())
        }
    }

    private var initialized = false
    private lateinit var handler:Handler
    private var uiExecutorImpl = Executors.newSingleThreadExecutor()
}
