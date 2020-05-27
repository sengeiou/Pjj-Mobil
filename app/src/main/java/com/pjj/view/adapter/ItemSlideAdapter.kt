package com.pjj.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import java.lang.RuntimeException

/**
 * Created by XinHeng on 2019/03/07.
 * describe：
 */
abstract class ItemSlideAdapter<C : ItemSlideAdapter.ContentViewHolder, M : ItemSlideAdapter.MenuViewHolder> :
        RecyclerView.Adapter<ItemSlideAdapter.ItemSlideViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSlideViewHolder {
        var contentViewHolder = getContentViewHolder(parent, viewType)
        var menuViewHolder = getMenuViewHolder(parent, viewType)
        return ItemSlideViewHolder(
                createView(
                        parent.context,
                        contentViewHolder.view,
                        menuViewHolder.view
                ), contentViewHolder, menuViewHolder
        )
    }

    override fun onBindViewHolder(holder: ItemSlideViewHolder, position: Int) {
        var contentViewHolder = holder.contentViewHolder
        var menuViewHolder = holder.menuViewHolder
        onBindHolder(contentViewHolder, menuViewHolder, position)
    }

    private fun createView(context: Context, contentView: View, menuView: View): ViewGroup {
        return LinearLayout(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.HORIZONTAL
            addView(contentView)
            var layoutParams = menuView.layoutParams
            if (null == layoutParams || layoutParams.width <= 0) {
                throw RuntimeException("getMenuView方法得到的view，必须设置固定的宽度")
            }
            addView(menuView)
        }
    }

    abstract fun onBindHolder(contentViewHolder: ContentViewHolder, menuViewHolder: MenuViewHolder, position: Int)
    abstract fun getMenuViewHolder(parent: ViewGroup, viewType: Int): M
    abstract fun getContentViewHolder(parent: ViewGroup, viewType: Int): C
    class ItemSlideViewHolder(
            view: ViewGroup,
            var contentViewHolder: ContentViewHolder,
            var menuViewHolder: MenuViewHolder
    ) : RecyclerView.ViewHolder(view)

    abstract class ContentViewHolder(val view: View)
    abstract class MenuViewHolder(val view: View)
}