package com.cindy.multitypedispatcher

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.cindy.library.MultiTypeAdapter
import com.cindy.library.OnFilterCellListener
import com.cindy.multitypedispatcher.adapter.ItemOneAdapter
import com.cindy.multitypedispatcher.adapter.ItemThreeAdapter
import com.cindy.multitypedispatcher.adapter.ItemTwoAdapter
import com.cindy.multitypedispatcher.bean.ItemOne
import com.cindy.multitypedispatcher.bean.ItemTwo
import kotlinx.android.synthetic.main.activity_sample.*

/**
 * sample one 2 one , one 2 more
 */
class SampleActivity : Activity() {

    private lateinit var multiTypeAdapter: MultiTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        Toast.makeText(this, "loading", Toast.LENGTH_SHORT).show()
        recyclerView.layoutManager = LinearLayoutManager(this)
        multiTypeAdapter = MultiTypeAdapter(this).apply {
            register(ItemOne::class.java).to(ItemOneAdapter())
            register(ItemTwo::class.java).to(filter = onAdapterFilter, binders =
            *arrayOf(ItemTwoAdapter(), ItemThreeAdapter()))
        }
        recyclerView.adapter = multiTypeAdapter
    }

    override fun onResume() {
        super.onResume()
        // 模拟网络回传数据
        recyclerView.postDelayed({
            val list = mutableListOf<Any>()
            for (index in 0..20) {
                if (index == 0 || index == 1 || index == 2) {
                    list.add(ItemOne().apply {
                        title = "title_one - position:$index"
                        des = "dec_one - position:$index"
                    })
                    continue
                }
                list.add(ItemTwo().apply {
                    type = if (index == 3 || index == 4 || index == 5) {
                        title = "title_two - position:$index"
                        des = "dec_two - position:$index"
                        0
                    } else {
                        title = "title_three - position:$index"
                        des = "dec_three - position:$index"
                        1
                    }
                })
            }
            multiTypeAdapter.clearAll()
            multiTypeAdapter.appendToList(list)
        }, 1500)
    }

    private val onAdapterFilter = object : OnFilterCellListener {
        override fun <T> onFilter(position: Int, any: T): Class<*> {
            return if ((any as ItemTwo).isTypeTwo) {
                ItemTwoAdapter::class.java
            } else {
                ItemThreeAdapter::class.java
            }
        }
    }

}
