package com.cindy.library

import android.support.v7.widget.RecyclerView

/**
 * dataBinder 暂存器
 *
 * @author ChenDanDan
 * @date 2018/9/30
 */
class LinkerDataBinder<T> {

    private var multiTypePool: MultiTypePool
    private var clazz: Class<out T>
    private var multiTypeAdapter: MultiTypeAdapter

    constructor(multiTypeAdapter: MultiTypeAdapter, multiTypePool: MultiTypePool, clazz: Class<out T>) {
        this.multiTypeAdapter = multiTypeAdapter
        this.multiTypePool = multiTypePool
        this.clazz = clazz
    }

    /**
     * register data to binder
     */
    fun to(binder: BaseCellAdapter<T, out RecyclerView.ViewHolder>) {
        this.to(filter = null, binders = binder)
    }

    /**
     * register data to binders
     */
    fun to(filter: OnFilterCellListener?, vararg binders: BaseCellAdapter<T, out RecyclerView.ViewHolder>) {
        for (binder in binders) {
            binder.adapter = multiTypeAdapter
        }
        multiTypePool.to(clazz, filter, *binders)
    }


}