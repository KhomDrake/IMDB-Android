package com.example.imdb.ui

import android.view.View

fun View.becomeVisible() { this.visibility = View.VISIBLE }

fun View.becomeInvisible() { this.visibility = View.INVISIBLE }

fun View.becomeVisibleOrInvisible(isToBeVisible: Boolean) = if(isToBeVisible) becomeVisible() else becomeInvisible()
