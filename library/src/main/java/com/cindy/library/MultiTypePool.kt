package com.cindy.library

import android.support.annotation.CheckResult
import android.support.v7.widget.RecyclerView
import android.util.Log
import java.util.*


/**
 * 多样式布局管理：作为 DefaultDataLinkBinder 与 MultiTypeAdapter 的桥梁
 *
 * @author ChenDanDan
 * @date 2018/9/26
 *
 */
open class MultiTypePool : TypePool {

    companion object {
        private const val TAG = "MultiTypePool"
    }

    private val linkers: MutableList<DefaultDataLinkBinder>
    /**
     * 当前最后一个 binder viewType 索引
     */
    private var preViewTypeIndex: Int = 0

    init {
        linkers = ArrayList()
    }

    /**
     * Registers a type class and its item view binder.
     *
     * @param clazz  the class of a item
     * @param multiTypeAdapter current adapter
     * @return LinkerDataBinder   a temp data
     */
    @CheckResult
    override fun <T> register(clazz: Class<out T>, multiTypeAdapter: MultiTypeAdapter): LinkerDataBinder<T> {
        return LinkerDataBinder(multiTypeAdapter = multiTypeAdapter,
                multiTypePool = this,
                clazz = clazz)
    }

    fun to(clazz: Class<*>,
           filter: OnFilterCellListener?,
           vararg binders: BaseCellAdapter<*, out RecyclerView.ViewHolder>) {
        //不能重复注册
        if (this.unregister(clazz)) {
            Log.e(MultiTypePool.TAG, "you have registered ${clazz::javaClass} this binder")
        }
        linkers.add(DefaultDataLinkBinder(clazz, binders = *binders, viewTypeIndex = this.preViewTypeIndex).apply {
            onFilter(filter)
        })
        // make sure binder's viewType is different
        preViewTypeIndex += binders.size
    }

    override fun unregister(clazz: Class<*>): Boolean {
        var result = false
        for (linker in linkers) {
            if (linker.clazz == clazz) {
                linkers.remove(linker)
                result = true
            }
        }
        return result
    }

    override fun size(): Int {
        return linkers.size
    }

    override fun firstIndexOfDataClazz(dataClazz: Class<*>): Int {
        for (index in linkers.indices) {
            if (linkers[index].clazz == dataClazz) {
                return index
            }
        }
        return -1
    }


    override fun getFilter(index: Int): OnFilterCellListener? {
        return linkers[index].filter
    }

    override fun getItemViewBinder(viewType: Int): BaseCellAdapter<*, out RecyclerView.ViewHolder>? {
        for (index in linkers.indices) {
            val linker = linkers[index]
            val viewTypes = linker.viewTypeBinders
            // viewType 是递增的，根据此特性可先取list最后一位判断 viewType 是否在此区间以提高效率
            if (viewTypes[viewTypes.size - 1] < viewType) {
                continue
            }
            for (position in viewTypes.indices) {
                if (viewTypes[position] == viewType) {
                    return linker.binders[position]
                }
            }
        }
        return null
    }

    override fun getViewTagOfBinder(binderClazz: Class<*>): Int {
        for (index in linkers.indices) {
            val linker = linkers[index]
            for (position in linker.binders.indices) {
                if (linker.binders[position]::class.java == binderClazz) {
                    return linker.viewTypeBinders[position]
                }
            }
        }
        return -1
    }

    override fun getViewTagByDefault(dataClazz: Class<*>): Int {
        for (index in linkers.indices) {
            val linker = linkers[index]
            if (linker.clazz == dataClazz) {
                return linker.viewTypeBinders[0]
            }
        }
        return -1
    }
}
