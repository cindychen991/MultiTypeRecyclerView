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

import android.support.v7.widget.RecyclerView

/**
 * An ordered collection to hold the types, binders, data type
 *
 * @author ChenDanDan
 * @date 2018/9/26
 */
interface TypePool {

    /**
     * Registers a type class and its item view binder.
     *
     * @param clazz  the class of a item
     * @param binder the item view binder
     * @param <T>    the item data type
    </T> */
    fun <T> register(clazz: Class<out T>,
                     binder: BaseCellAdapter<T, out RecyclerView.ViewHolder>)

    /**
     * Unregister all items with the specified class.
     *
     * @param clazz the class of items
     * @return true if any items are unregistered from the pool
     */
    fun unregister(clazz: Class<*>): Boolean

    /**
     * Returns the number of items in this pool.
     *
     * @return the number of items in this pool
     */
    fun size(): Int

    /**
     * For getting index of the item class.
     *
     * @param dataClazz the item's data class.
     * @return The index of the first occurrence of the specified class
     * in this pool, or -1 if this pool does not contain the class.
     */
    fun firstIndexOfDataClazz(dataClazz: Class<*>): Int

    fun getFilter(index: Int): OnFilterCellListener?
    /**
     * Get the item view binder with viewType.
     *
     * @param viewType binder's viewType
     * @return the binder
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    fun getItemViewBinder(viewType: Int): BaseCellAdapter<*, out RecyclerView.ViewHolder>?

    fun getViewTagOfBinder(binderClazz: Class<*>): Int

    fun getViewTagByDefault(dataClazz: Class<*>): Int

}