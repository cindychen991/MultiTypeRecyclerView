package com.cindy.library

/**
 * BinderNotFoundException
 *
 * @author ChenDanDan
 * @date 2018/9/29
 */
open class BinderNotFoundException : java.lang.RuntimeException {

    constructor() : super()

    constructor(msg: String) : super(msg)
}