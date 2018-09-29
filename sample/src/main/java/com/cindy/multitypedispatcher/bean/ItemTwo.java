package com.cindy.multitypedispatcher.bean;

/**
 * @author ChenDanDan
 * @date 2018/9/26
 */

public class ItemTwo {

    /**
     * 标志判断使用不同布局
     */
    public int type;
    public String title;
    public String des;


    public boolean isTypeTwo() {
        return this.type == 0;
    }
}
