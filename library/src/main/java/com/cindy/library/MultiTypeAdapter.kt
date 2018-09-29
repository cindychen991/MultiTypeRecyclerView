/*
 * Copyright 2016 drakeet. https://github.com/drakeet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cindy.library

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import java.util.Collections.emptyList

/**
 * 多样式 adapter，注册、分发到相应处理 Binder(以单个布局为最小单位，一个 Binder 对应一个布局方式)
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */

open class MultiTypeAdapter @JvmOverloads constructor(context: Context, list: MutableList<Any>? = null)
    : BaseRecyclerViewAdapter<Any, RecyclerView.ViewHolder>(context, list) {

    companion object {
        private const val TAG = "MultiTypeAdapter"
    }

    private val multiTypePool: MultiTypePool = MultiTypePool()


    /**
     * one 2 one : 注册 CellAdapter
     *
     * @param clazz  data 的类型
     * @param binder which extends BaseCellAdapter
     */
    fun <T> register(clazz: Class<out T>, binder: BaseCellAdapter<T, out RecyclerView.ViewHolder>)
            : MultiTypeAdapter {
        return register(clazz, binders = *arrayOf(binder), filterCellListener = null)
    }

    /**
     * one 2 more : 注册 CellAdapter
     *
     * @param clazz  data 的类型
     * @param binders BaseCellAdapters
     * @param filterCellListener 自定义返回该 item 需要处理的 binder class, 默认返回数据绑定的第一个
     */
    fun <T> register(clazz: Class<out T>,
                     vararg binders: BaseCellAdapter<T, out RecyclerView.ViewHolder>,
                     filterCellListener: OnFilterCellListener?)
            : MultiTypeAdapter {
        //不能重复注册
        if (multiTypePool.unregister(clazz)) {
            // todo
            Log.e(TAG, "you have registered ${clazz::javaClass} this binder")
        }
        for (binder in binders) {
            binder.adapter = this
        }
        multiTypePool.register(clazz, binders = *binders, filterCellListener = filterCellListener)
        return this
    }

    fun getViewTypes(): Int {
        return multiTypePool.size()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getList()[position]
        checkNotNull(item)
        var index = multiTypePool.firstIndexOfDataClazz(item.javaClass)
        if (index == -1) {
            throw NullPointerException("${item.javaClass} have not register，you should register first!")
        }
        val binderClazz = multiTypePool.getFilter(index)?.onFilter(position = position, any = item)
        return when (binderClazz) {
        // if filter is null or onFilter() return null,use getViewTagOfDefalut's binder viewType default.
            null -> {
                multiTypePool.getViewTagByDefault(item::class.java)
            }
            else -> {
                index = multiTypePool.getViewTagOfBinder(binderClazz)
                if (index == -1) {
                    throw NullPointerException("$binderClazz have not register，you should register first!")
                }
                index
            }
        }
    }

    /**
     * 下发给 ItemViewCreator 处理
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewTypeInTypePool: Int): RecyclerView.ViewHolder {
        val binder = multiTypePool.getItemViewBinder(viewTypeInTypePool)
        checkBinderIsNull(binder)
        return binder!!.createHolderHelper(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>?) {
        val binder = multiTypePool.getItemViewBinder(getItemViewType(position)) as BaseCellAdapter<Any, RecyclerView.ViewHolder>
        binder.bindData(holder, getList()[position], payloads)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val binder = multiTypePool.getItemViewBinder(holder.itemViewType)
        checkBinderIsNull(binder)
        binder!!.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val binder = multiTypePool.getItemViewBinder(holder.itemViewType)
        checkBinderIsNull(binder)
        binder!!.onViewDetachedFromWindow(holder)
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     * @see .onDetachedFromRecyclerView
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {}

    /**
     * Called by RecyclerView when it stops observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which stopped observing this adapter.
     * @see .onAttachedToRecyclerView
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {}


    private fun checkNotNull(obj: Any?) {
        if (obj == null) {
            throw NullPointerException("obj can not be null")
        }
    }

    private fun checkBinderIsNull(binder: BaseCellAdapter<*, *>?) {
        if (binder == null) {
            throw  BinderNotFoundException("Do you have registered the binder?")
        }
    }
}