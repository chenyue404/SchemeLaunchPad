package com.cy.schemelaunchpad

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.cy.fixtableview.FixTableView

/**
 * Created by chenyue on 2022/5/3 0003.
 */
class MainActivity : Activity() {

    private val btName: Button by lazy { findViewById(R.id.btName) }
    private val ibEdit: ImageButton by lazy { findViewById(R.id.ibEdit) }
    private val tvSpaceVertical: TextView by lazy { findViewById(R.id.tvSpaceVertical) }
    private val sbVertical: SeekBar by lazy { findViewById(R.id.sbVertical) }
    private val tvSpaceHorizontal: TextView by lazy { findViewById(R.id.tvSpaceHorizontal) }
    private val sbHorizontal: SeekBar by lazy { findViewById(R.id.sbHorizontal) }
    private val ibHorizontalAdd: ImageButton by lazy { findViewById(R.id.ibHorizontalAdd) }
    private val ibHorizontalDelete: ImageButton by lazy { findViewById(R.id.ibHorizontalDelete) }
    private val ibVerticalAdd: ImageButton by lazy { findViewById(R.id.ibVerticalAdd) }
    private val ibVerticalDelete: ImageButton by lazy { findViewById(R.id.ibVerticalDelete) }
    private val gpEditLayout: Group by lazy { findViewById(R.id.gpEditLayout) }
    private val ftvList: FixTableView by lazy { findViewById(R.id.ftvList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ftvList.apply {
            getNewChild = { column, row ->
                LayoutInflater.from(this@MainActivity).inflate(
                    R.layout.item_list,
                    ftvList,
                    false
                ).apply {
                    (this as Button).text = "$column, $row"
                }
            }
            columnSize = 1
            rowSize = 1
        }
        ibEdit.click {
            gpEditLayout.visibility =
                if (gpEditLayout.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }
        ibHorizontalAdd.click {
            ftvList.columnSize += 1
        }
        ibHorizontalDelete.click {
            ftvList.columnSize -= 1
        }
        ibVerticalAdd.click {
            ftvList.rowSize += 1
        }
        ibVerticalDelete.click {
            ftvList.rowSize -= 1
        }
    }
}