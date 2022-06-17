package com.wangxingxing.ndkfilepatch

/**
 * author : 王星星
 * date : 2022/5/31 11:58
 * email : 1099420259@qq.com
 * description :
 */
object NDKFileUtils {

    /**
     * 拆分
     *
     * @param path
     * @param path_pattern
     * @param count
     */
    external fun split(path: String, path_pattern: String, count: Int)

    /**
     * 合并
     *
     * @param path_pattern
     * @param count
     * @param merge_path
     */
    external fun merge(path_pattern: String, count: Int, merge_path: String)

    init {
        System.loadLibrary("ndkfilepatch")
    }
}