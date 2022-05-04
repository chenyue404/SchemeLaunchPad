package com.cy.schemelaunchpad

import android.view.View

fun View.click(listener: View.OnClickListener) {
    setOnClickListener(listener)
}