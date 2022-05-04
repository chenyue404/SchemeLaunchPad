package com.cy.fixtableview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

/**
 * Created by chenyue on 2022/5/3 0003.
 */
class FixTableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    var rowSize = 0
        set(value) {
            if (value < 1 || value == field) return
            field = value
            updateColumn()
        }
    var columnSize = 0
        set(value) {
            if (value < 1 || value == field) return
            field = value
            updateColumn()
        }

    private val childList: MutableList<MutableList<View>> = mutableListOf()

    init {

    }

    var getNewChild: (column: Int, row: Int) -> View = { _, _ ->
        View(context)
    }

    private fun updateColumn() {
        for (column in 0 until columnSize) {
            // 去除多余的行
            while (column < childList.size && rowSize < childList[column].size) {
                val lastView = childList[column].last()
                childList[column].remove(lastView)
                removeView(lastView)
            }

            // 增加缺少的元素
            for (row in 0 until rowSize) {
                if (childList.getOrNull(column) == null) {
                    childList.add(mutableListOf())
                }
                if (childList[column].getOrNull(row) == null) {
                    val newView = getNewChild(column, row).apply {
                        layoutParams = LayoutParams(0, 0)
                        id = generateViewId()
                    }
                    childList[column].add(newView)
                    addView(newView)
                }
            }
        }

        // 去除多余的列
        if (columnSize < childList.size) {
            for (column in columnSize until childList.size) {
                while (childList[column].isNotEmpty()) {
                    childList[column].last().let {
                        childList[column].remove(it)
                        removeView(it)
                    }
                }
            }
        }

        val constraintSet = ConstraintSet().apply {
            clone(this@FixTableView)
        }
        for (column in 0 until columnSize) {
            for (row in 0 until rowSize) {
                val thisViewId = childList[column][row].id
                constraintSet.apply {
                    clear(thisViewId)

                    // Top
                    if (row == 0) {
                        connect(
                            thisViewId,
                            ConstraintSet.TOP,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP
                        )
                    } else {
                        connect(
                            thisViewId,
                            ConstraintSet.TOP,
                            childList[0][row - 1].id,
                            ConstraintSet.BOTTOM
                        )
                    }

                    // Start
                    if (column == 0) {
                        connect(
                            thisViewId,
                            ConstraintSet.START,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.START
                        )
                    } else {
                        connect(
                            thisViewId,
                            ConstraintSet.START,
                            childList[column - 1][0].id,
                            ConstraintSet.END
                        )
                    }

                    // Bottom
                    if (row == rowSize - 1) {
                        connect(
                            thisViewId,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM
                        )
                    } else {
                        connect(
                            thisViewId,
                            ConstraintSet.BOTTOM,
                            childList[0][row + 1].id,
                            ConstraintSet.TOP
                        )
                    }

                    // End
                    if (column == columnSize - 1) {
                        connect(
                            thisViewId,
                            ConstraintSet.END,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.END
                        )
                    } else {
                        connect(
                            thisViewId,
                            ConstraintSet.END,
                            childList[column + 1][0].id,
                            ConstraintSet.START
                        )
                    }
                }
            }
        }
        constraintSet.applyTo(this@FixTableView)
    }
}