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
    var column: Int,
    var row: Int,
    var text: String?,
    var clickStr: String?,
    var longClickStr: String?,
)

class ConfigBean(
    var name: String,
    var tableConfig: TableConfigBean = TableConfigBean(),
    var list: List<ItemConfigBean> = listOf(),
)