package com.cy.schemelaunchpad

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import com.cy.fixtableview.FixTableView
import com.cy.schemelaunchpad.json.JsonUtil
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

/**
 * Created by chenyue on 2022/5/3 0003.
 */
class MainActivity : Activity() {

    companion object {
        const val SP_NAME = "SP_NAME"
        const val SP_KEY_CURRENT = "SP_KEY_CURRENT"
        const val SP_KEY_CONFIG = "SP_KEY_CONFIG"
        const val DEFAULT_CONFIG_NAME = "Default"
    }

    private val btName: Button by lazy { findViewById(R.id.btName) }
    private val ibEdit: ImageButton by lazy { findViewById(R.id.ibEdit) }
    private val ibTable: ImageButton by lazy { findViewById(R.id.ibTable) }
    private val ibSetting: ImageButton by lazy { findViewById(R.id.ibSetting) }
    private val ftvList: FixTableView by lazy { findViewById(R.id.ftvList) }

    private val tableConfigDialog: TableConfigDialog by lazy { TableConfigDialog(this) }
    private val sp by lazy {
        getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    private var allConfigMap: HashMap<String, ConfigBean> = hashMapOf()
    private var currentConfigName = ""
    private var currentConfig: ConfigBean? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main)
        readStorage()

        ibEdit.click {
            if (isEditMode) {
                saveCurrent()
            }
            isEditMode = !isEditMode
            val otherBtnVisibility = if (isEditMode) {
                View.VISIBLE
            } else {
                View.GONE
            }
            ibSetting.visibility = otherBtnVisibility
            ibTable.visibility = otherBtnVisibility
            ibEdit.setImageDrawable(
                getDrawable(
                    if (isEditMode) R.drawable.ic_save
                    else R.drawable.ic_edit
                )
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ibEdit.tooltipText = getString(
                    if (isEditMode) R.string.save
                    else R.string.edit
                )
            }
        }
        ibTable.click {
            tableConfigDialog.show(currentConfig?.tableConfig)
        }
        tableConfigDialog.onSaveListener = {
            currentConfig?.tableConfig = it
            updateView()
        }
        updateView()
    }

    private fun saveCurrent() {
        allConfigMap[currentConfigName] = currentConfig ?: return
        sp.edit()
            .putString(SP_KEY_CONFIG, JsonUtil.toJson(allConfigMap))
            .apply()

    }

    private fun updateView() {
        val tableConfig = currentConfig?.tableConfig ?: return
        val itemList = currentConfig?.list
        btName.text = currentConfigName

        ftvList.getNewChild = { column, row ->
            LayoutInflater.from(this@MainActivity).inflate(
                R.layout.item_list,
                ftvList,
                false
            ).apply {
                val item = itemList?.find { it.column == column && it.row == row }
                (this as Button).apply {
                    text = item?.text ?: "$column, $row"
                    item?.clickStr?.let { command ->
                        setOnClickListener { launchActivity(command) }
                    }
                    item?.longClickStr?.let { command ->
                        setOnLongClickListener {
                            launchActivity(command)
                            true
                        }
                    }
                }
            }
        }
        ftvList.updateTable(tableConfig.columnSize, tableConfig.rowSize)
        (ftvList.layoutParams as ConstraintLayout.LayoutParams).apply {
            matchConstraintPercentWidth = tableConfig.width * 0.01f
            matchConstraintPercentHeight = tableConfig.height * 0.01f
        }
    }

    private fun readStorage() {
        (sp.getString(SP_KEY_CURRENT, "") ?: "").takeIf { it.isNotEmpty() }?.let {
            currentConfigName = it
        } ?: kotlin.run {
            sp.edit().putString(SP_KEY_CURRENT, DEFAULT_CONFIG_NAME).apply()
        }

        (sp.getString(SP_KEY_CONFIG, "") ?: "")
            .takeIf {
                it.isNotEmpty()
            }?.let {
                log(it)
                try {
                    JsonUtil.fromJson<HashMap<String, ConfigBean>>(
                        it,
                        object : TypeToken<HashMap<String, ConfigBean>>() {}.type
                    )
                } catch (e: JsonSyntaxException) {
                    null
                }
            }?.let {
                allConfigMap = it
            }

        if (allConfigMap.isEmpty()) {
            val configBean = ConfigBean(DEFAULT_CONFIG_NAME)
            allConfigMap[DEFAULT_CONFIG_NAME] = configBean
            currentConfig = configBean
            saveCurrent()
        } else {
            currentConfig = allConfigMap[currentConfigName]
        }
    }

    private fun launchActivity(command: String) {
        startActivity(Intent(Intent.ACTION_VIEW, command.toUri()))
    }

    private fun log(str: String) {
        Log.d(localClassName, str)
    }
}