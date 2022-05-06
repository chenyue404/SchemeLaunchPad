package com.cy.schemelaunchpad

/**
 * Created by cy on 2022/5/5.
 */
class TableConfigBean(
    var columnSize: Int = 1,
    var rowSize: Int = 1,
    var width: Int = 100,
    var height: Int = 100,
)

class ItemConfigBean(
    var text: String = "",
    var clickStr: String = "",
    var longClickStr: String = "",
)

class ConfigBean(
    var tableConfig: TableConfigBean = TableConfigBean(),
    var itemMap: HashMap<String, ItemConfigBean> = hashMapOf()
)

fun getItemConfigKey(column: Int, row: Int) = "$column,$row"