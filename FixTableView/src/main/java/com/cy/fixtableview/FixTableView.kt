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
        private set
    var columnSize = 0
        private set

    private val _childList: MutableList<MutableList<View>> = mutableListOf()

    var childList: List<List<View>> = _childList

    var getNewChild: (column: Int, row: Int) -> View = { _, _ ->
        View(context)
    }

    fun updateTable(newColumn: Int, newRow: Int) {
        if (columnSize == newColumn && rowSize == newRow) return
        columnSize = newColumn
        rowSize = newRow
        for (column in 0 until columnSize) {
            // 去除多余的行
            while (column < _childList.size && rowSize < _childList[column].size) {
                val lastView = _childList[column].last()
                _childList[column].remove(lastView)
                removeView(lastView)
            }

            // 增加缺少的元素
            for (row in 0 until rowSize) {
                if (_childList.getOrNull(column) == null) {
                    _childList.add(mutableListOf())
                }
                if (_childList[column].getOrNull(row) == null) {
                    val newView = getNewChild(column, row).apply {
                        layoutParams = LayoutParams(0, 0)
                        id = generateViewId()
                    }
                    _childList[column].add(newView)
                    addView(newView)
                }
            }
        }

        // 去除多余的列
        if (columnSize < _childList.size) {
            for (column in columnSize until _childList.size) {
                while (_childList[column].isNotEmpty()) {
                    _childList[column].last().let {
                        _childList[column].remove(it)
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
                val thisViewId = _childList[column][row].id
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
                            _childList[0][row - 1].id,
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
                            _childList[column - 1][0].id,
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
                            _childList[0][row + 1].id,
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
                            _childList[column + 1][0].id,
                            ConstraintSet.START
                        )
                    }
                }
            }
        }
        constraintSet.applyTo(this@FixTableView)
    }
}