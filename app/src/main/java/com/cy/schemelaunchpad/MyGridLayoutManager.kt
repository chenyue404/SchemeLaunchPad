package com.cy.schemelaunchpad

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by chenyue on 2022/5/3 0003.
 */
class MyGridLayoutManager : RecyclerView.LayoutManager() {
    override fun generateDefaultLayoutParams() =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        //分离并且回收当前附加的所有View
        detachAndScrapAttachedViews(recycler)
        if (itemCount == 0) return

    }
}