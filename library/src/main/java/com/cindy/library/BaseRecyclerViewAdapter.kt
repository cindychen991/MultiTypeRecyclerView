package com.cindy.library

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_ID
import android.view.LayoutInflater
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * RecyclerView BaseAdapter
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */
abstract class BaseRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH> {
    private var mList: MutableList<T>
    val mContext: Context
    val mInflater: LayoutInflater

    constructor(@NotNull context: Context) : this(context, null)

    constructor(@NotNull context: Context, list: MutableList<T>?) {
        mContext = context
        mList = list ?: ArrayList()
        mInflater = LayoutInflater.from(context)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    final override fun getItemId(position: Int): Long {
        return NO_ID
    }

    fun getItem(position: Int): T? {
        return mList[position]
    }

    /**
     * 获取adapter里面的所有数据
     */
    fun getList(): List<T> {
        return mList
    }

    /**
     * 往adapter里面追加数据（往后面追加）
     */
    fun appendToList(list: List<T>?) {
        list?.apply {
            mList.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 往adapter里面追加数据（往前面追加）
     */
    fun addToTopList(list: List<T>?) {
        list?.apply {
            mList.addAll(0, list)
            notifyDataSetChanged()
        }
    }

    /**
     * 清空所有列表数据并且刷新adapter
     */
    fun clearAll() {
        mList.clear()
        notifyDataSetChanged()
    }

}