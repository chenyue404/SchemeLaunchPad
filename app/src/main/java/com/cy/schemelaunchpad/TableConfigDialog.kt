package com.cy.schemelaunchpad

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView

/**
 * Created by cy on 2022/5/5.
 */
class TableConfigDialog(private val mContext: Context) {
    private val rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_table_config, null)
    private val sbColumnSize: SeekBar = rootView.findViewById(R.id.sbColumnSize)
    private val sbWidth: SeekBar = rootView.findViewById(R.id.sbWidth)
    private val sbRowSize: SeekBar = rootView.findViewById(R.id.sbRowSize)
    private val sbHeight: SeekBar = rootView.findViewById(R.id.sbHeight)
    private val tvColumnSize: TextView = rootView.findViewById(R.id.tvColumnSize)
    private val tvWidth: TextView = rootView.findViewById(R.id.tvWidth)
    private val tvRowSize: TextView = rootView.findViewById(R.id.tvRowSize)
    private val tvHeight: TextView = rootView.findViewById(R.id.tvHeight)

    var onSaveListener: ((tableConfig: TableConfigBean) -> Unit)? = null

    private var dialog: AlertDialog = AlertDialog.Builder(mContext)
        .setView(rootView)
        .setTitle(R.string.table_config)
        .setPositiveButton(
            android.R.string.ok
        ) { dialog, which ->
            onSaveListener?.invoke(
                TableConfigBean(
                    columnSize = sbColumnSize.progress,
                    rowSize = sbRowSize.progress,
                    width = sbWidth.progress,
                    height = sbHeight.progress,
                )
            )
            dialog.cancel()
        }
        .setNegativeButton(android.R.string.cancel)
        { dialog, which ->
            dialog.cancel()
        }
        .create()

    init {
        sbColumnSize.setOnChangeListener {
            if (it == 0) {
                sbColumnSize.progress = 1
                return@setOnChangeListener
            }
            tvColumnSize.text = it.toString()
        }
        sbWidth.setOnChangeListener {
            tvWidth.text = mContext.xPercentStr(it)
        }
        sbRowSize.setOnChangeListener {
            if (it == 0) {
                sbRowSize.progress = 1
                return@setOnChangeListener
            }
            tvRowSize.text = it.toString()
        }
        sbHeight.setOnChangeListener {
            tvHeight.text = mContext.xPercentStr(it)
        }
    }

    fun show(tableConfig: TableConfigBean? = null) {
        tableConfig?.let {
            sbColumnSize.progress = it.columnSize
            sbWidth.progress = it.width
            sbRowSize.progress = it.rowSize
            sbHeight.progress = it.height
        }
        dialog.show()
    }

    private fun SeekBar.setOnChangeListener(
        change: (progress: Int) -> Unit
    ) {
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                change(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun Context.xPercentStr(percent: Int) = getString(R.string.x_percent, percent)

}