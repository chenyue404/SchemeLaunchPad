package com.cy.schemelaunchpad

import android.app.Activity
import android.content.ActivityNotFoundException
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
import android.widget.TextView
import android.widget.Toast
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
    private val editItemDialog: EditItemDialog by lazy { EditItemDialog(this) }
    private val sp by lazy { getSharedPreferences(SP_NAME, Context.MODE_PRIVATE) }

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
            switchItemStatus(isEditMode)
        }
        ibTable.click {
            tableConfigDialog.show(currentConfig?.tableConfig)
        }
        tableConfigDialog.onSaveListener = {
            currentConfig?.tableConfig = it
            updateView()
        }
        editItemDialog.onSaveListener = { column, row, itemConfigBean ->
            currentConfig?.itemMap?.put(getItemConfigKey(column, row), itemConfigBean)
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
        val itemMap = currentConfig?.itemMap
        btName.text = currentConfigName

        ftvList.getNewChild = { column, row ->
            LayoutInflater.from(this@MainActivity).inflate(
                R.layout.item_list,
                ftvList,
                false
            ).apply {
                val item = itemMap?.get(getItemConfigKey(column, row)) ?: ItemConfigBean()
                getBtn().text = item.text
                item.clickStr.let { command ->
                    getBtn().click { launchActivity(command) }
                }
                item.longClickStr.let { command ->
                    getBtn().setOnLongClickListener {
                        launchActivity(command)
                        true
                    }
                }
                getTv().click {
                    editItemDialog.show(column, row, item)
                }
                getTv().visibility =
                    if (isEditMode) {
                        View.VISIBLE
                    } else {
                        View.GONE
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
            currentConfigName = DEFAULT_CONFIG_NAME
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
            val configBean = ConfigBean()
            allConfigMap[DEFAULT_CONFIG_NAME] = configBean
            currentConfig = configBean
        } else {
            allConfigMap.remove("")
            currentConfig = allConfigMap[currentConfigName]
        }
        saveCurrent()
    }

    private fun launchActivity(command: String) {
        if (command.isEmpty()) return
        try {
            startActivity(Intent(Intent.ACTION_VIEW, command.toUri()))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.tip_no_activity), Toast.LENGTH_SHORT).show()
        }
    }

    private fun log(str: String) {
        Log.d(localClassName, str)
    }

    private fun switchItemStatus(editMode: Boolean) {
        ftvList.childList.forEachIndexed { columnIndex, list ->
            list.forEachIndexed { rowIndex, view ->
                view.getTv().visibility =
                    if (editMode) View.VISIBLE
                    else View.GONE
            }
        }
    }

    private fun View.getBtn() = findViewById<Button>(R.id.btTitle)
    private fun View.getTv() = findViewById<TextView>(R.id.tvEdit)
}