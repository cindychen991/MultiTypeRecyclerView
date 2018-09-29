package com.cindy.multitypedispatcher.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.cindy.library.BaseCellAdapter
import com.cindy.multitypedispatcher.R
import com.cindy.multitypedispatcher.bean.ItemTwo

/**
 * item-two
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */
class ItemTwoAdapter : BaseCellAdapter<ItemTwo, ItemTwoAdapter.Companion.ViewHolder>() {


    override fun createHolderHelper(parent: ViewGroup): ViewHolder {
        return ViewHolder(adapter.mInflater.inflate(R.layout.layout_item_two, parent, false))
    }

    override fun bindData(holder: ViewHolder, data: ItemTwo?, payloads: List<Any>?) {
        if (data == null) {
            return
        }
        with(data) {
            holder.tvTitle.text = title ?: ""
            holder.tvDes.text = des ?: ""
        }
        holder.itemView.setOnClickListener {
            Toast.makeText(adapter.mContext, "这是第${holder.layoutPosition}项", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    companion object {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
            val tvDes: TextView = itemView.findViewById(R.id.tv_des)
        }
    }
}