package com.pjj.contract

interface PayParentView : BaseView {
    fun paySuccess()
    fun startOrderView(index: Int)
    fun preMakeOrder(typePay: Int): Boolean
}