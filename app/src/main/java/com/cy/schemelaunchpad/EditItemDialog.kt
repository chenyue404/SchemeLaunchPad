package com.cy.schemelaunchpad

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText

/**
 * Created by cy on 2022/5/6.
 */
class EditItemDialog(private val mContext: Context) {
    private val rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_item, null)
    private val etTitle: EditText = rootView.findViewById(R.id.etTitle)
    private val etClick: EditText = rootView.findViewById(R.id.etClick)
    private val etLongClick: EditText = rootView.findViewById(R.id.etLongClick)

    var onSaveListener: ((column: Int, row: Int, itemConfigBean: ItemConfigBean) -> Unit)? = null

    private var itemConfigBean: ItemConfigBean? = null
    private var column: Int = 0
    private var row: Int = 0

    private val dialog = AlertDialog.Builder(mContext)
        .setView(rootView)
        .setPositiveButton(android.R.string.ok) { dialog, which ->
            onSaveListener?.invoke(
                column,
                row,
                itemConfigBean!!.apply {
                    text = etTitle.text.toString()
                    clickStr = etClick.text.toString()
                    longClickStr = etLongClick.text.toString()
                })
            dialog.cancel()
        }.setNegativeButton(android.R.string.cancel)
        { dialog, which ->
            dialog.cancel()
        }
        .create()

    fun show(column: Int, row: Int, itemConfigBean: ItemConfigBean) {
        this.column = column
        this.row = row
        this.itemConfigBean = itemConfigBean
        dialog.apply {
            setTitle("$column, $row")
            etTitle.setText(itemConfigBean.text)
            etClick.setText(itemConfigBean.clickStr)
            etLongClick.setText(itemConfigBean.longClickStr)
            show()
        }
    }
}