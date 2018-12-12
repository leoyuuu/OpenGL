package me.leoyuu.utils

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor

/**
 * date 2018/6/15
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
object DownloadUtil {

    fun downloadFile(urlStr:String, fileName:String, executor: Executor, callback:DownloadCallback) {
        val file = File(fileName)
        if (!file.isFile) {
            executor.execute { callback.onFailed("文件名不合法") }
            return
        }
        if (!file.parentFile.mkdirs()) {
            executor.execute { callback.onFailed("创建文件失败") }
            return
        }
        if (!file.createNewFile()) {
            executor.execute { callback.onFailed("写文件失败") }
            return
        }
        try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 10 * 1000
            val total = conn.contentLength
            var cur = 0
            val input = BufferedInputStream(conn.inputStream)
            val output = BufferedOutputStream(FileOutputStream(file))
            val buffer = ByteArray(1024 * 8)
            var count: Int = input.read(buffer)
            var percent = 0
            while (count > 0) {
                cur += count
                output.write(buffer, 0, count)
                val curPercent = cur * 100 / total
                if (curPercent != percent) {
                    executor.execute { callback.onUpdate(curPercent) }
                    percent = curPercent
                }
                count = input.read(buffer)
            }
            executor.execute { callback.onSuccess(fileName) }
            output.close()
            input.close()
        } catch (e:IOException) {
            callback.onFailed(e.localizedMessage)
        }
    }

    interface DownloadCallback{
        fun onSuccess(fileName: String)
        fun onFailed(msg:String)
        fun onUpdate(percent:Int)
    }

    open class SimpleCallback :DownloadCallback {
        override fun onFailed(msg: String) {}
        override fun onSuccess(fileName: String) {}
        override fun onUpdate(percent: Int) {}
    }

}
