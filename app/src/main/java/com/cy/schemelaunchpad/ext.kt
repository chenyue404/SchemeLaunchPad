package com.cy.schemelaunchpad

import android.view.View

fun View.click(listener: View.OnClickListener) {
    setOnClickListener(listener)
}

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.gone() {
    visibility = View.GONE
}