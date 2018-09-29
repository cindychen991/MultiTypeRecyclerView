package com.cindy.library

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * CellBinder 基类
 *
 * @author ChenDanDan
 * @date 2018/9/25
 */

abstract class BaseCellAdapter<T, VH : RecyclerView.ViewHolder> {


    /**
     * 注册之后自动set与之绑定的 MultiTypeAdapter，注册之后自动被赋值
     */
    lateinit var adapter: MultiTypeAdapter

    open fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {}

    open fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {}

    /**
     * create a new ViewHolder for view which extends RecyclerView.ViewHolder
     */
    abstract fun createHolderHelper(parent: ViewGroup): VH

    /**
     * bind data into view
     */
    abstract fun bindData(holder: VH, data: T?, payloads: List<Any>?)

}
