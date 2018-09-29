package com.cindy.library

/**
 * binder 筛选
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */
interface OnFilterCellListener {

    /**
     * 返回需要处理该事件的binder对应的class
     * note ：如返回未注册过的 class,则会抛出异常
     */
    fun <T> onFilter(position: Int, any: T): Class<*>
}