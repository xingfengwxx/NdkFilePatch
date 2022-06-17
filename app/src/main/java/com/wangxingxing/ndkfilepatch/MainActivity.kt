package com.wangxingxing.ndkfilepatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.ToastUtils
import com.wangxingxing.ndkfilepatch.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mDestPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDestPath = filesDir.absolutePath + File.separator + "test.mp4"

        checkPermission()

        initEvent()
    }

    private fun initEvent() {
        binding.apply {
            btnDiff.setOnClickListener {
                val pathPattern = filesDir.absolutePath + File.separator + "test_%d.mp4"
                lifecycleScope.launch(Dispatchers.IO) {
                    LogUtils.i("split start-----------------")
                    val startTime = System.currentTimeMillis()
                    NDKFileUtils.split(mDestPath, pathPattern, 4)
                    LogUtils.i("split end, use time: ${System.currentTimeMillis() - startTime}ms")
                }
            }

            btnPatch.setOnClickListener {
                val pathPattern = filesDir.absolutePath + File.separatorChar + "test_%d.mp4"
                val mergePath = filesDir.absolutePath + File.separator + "test_merge.mp4"
                lifecycleScope.launch(Dispatchers.IO) {
                    LogUtils.i("merge start-----------------")
                    val startTime = System.currentTimeMillis()
                    NDKFileUtils.merge(pathPattern, 4, mergePath)
                    LogUtils.i("split end, use time: ${System.currentTimeMillis() - startTime}ms")
                }
            }
        }
    }

    private fun init() {
        val isCopySuccess = ResourceUtils.copyFileFromAssets("test.mp4", mDestPath)
        LogUtils.d("isCopySuccess=$isCopySuccess, destFilePath=$mDestPath")
    }

    private fun checkPermission() {
        PermissionUtils.permissionGroup(PermissionConstants.STORAGE)
            .rationale{ activity, shouldRequest -> ToastUtils.showShort("请打开权限设置页面授权")}
            .callback(object : PermissionUtils.FullCallback{
                override fun onGranted(granted: MutableList<String>) {
                    LogUtils.d("申请权限通过")
                    init()
                }

                override fun onDenied(
                    deniedForever: MutableList<String>,
                    denied: MutableList<String>
                ) {
                    ToastUtils.showShort("申请权限被拒绝")
                }

            }).request()
    }

}