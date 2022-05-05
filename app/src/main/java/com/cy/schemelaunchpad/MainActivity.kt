package com.cy.schemelaunchpad

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.cy.fixtableview.FixTableView

/**
 * Created by chenyue on 2022/5/3 0003.
 */
class MainActivity : Activity() {

    private val btName: Button by lazy { findViewById(R.id.btName) }
    private val ibEdit: ImageButton by lazy { findViewById(R.id.ibEdit) }
    private val ibTable: ImageButton by lazy { findViewById(R.id.ibTable) }
    private val ibSetting: ImageButton by lazy { findViewById(R.id.ibSetting) }
    private val ftvList: FixTableView by lazy { findViewById(R.id.ftvList) }

    private val tableConfigDialog: TableConfigDialog by lazy { TableConfigDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
            updateTable(1, 1)
        }
        ibEdit.click {
            tableConfigDialog.show()
        }
        tableConfigDialog.onSaveListener = {
            ftvList.updateTable(it.columnSize, it.rowSize)
            (ftvList.layoutParams as ConstraintLayout.LayoutParams).apply {
                matchConstraintPercentWidth = it.width * 0.01f
                matchConstraintPercentHeight = it.height * 0.01f
            }
        }
    }

}