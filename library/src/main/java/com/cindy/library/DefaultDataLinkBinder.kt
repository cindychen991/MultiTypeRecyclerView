package com.cindy.library

import android.support.v7.widget.RecyclerView

/**
 * 管理 data 与 itemViewAdapter 的映射关系,支持一对多
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */
open class DefaultDataLinkBinder {

    val clazz: Class<*>
    /** binders 与 viewTypeBinders 对应*/
    val binders: MutableList<BaseCellAdapter<*, out RecyclerView.ViewHolder>> = mutableListOf()
    val viewTypeBinders: MutableList<Int> = mutableListOf()

    var filter: OnFilterCellListener? = null

    constructor(clazz: Class<*>, vararg binders: BaseCellAdapter<*, out RecyclerView.ViewHolder>,
                viewTypeIndex: Int) {
        this.clazz = clazz
        this.binders.addAll(binders)
        var preViewType = viewTypeIndex
        for (index in this.binders.indices) {
            viewTypeBinders.add(preViewType)
            // make sure binder's viewType is different
            ++preViewType
        }
    }

    fun onFilter(filter: OnFilterCellListener?) {
        this.filter = filter
    }

}