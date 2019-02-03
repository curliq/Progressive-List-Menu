package com.curlicue.progressivelistmenu.library

import android.view.View

data class ProgressiveMenuItem(
    var name: String,
    var icon: Int?,
    var clickListener: View.OnClickListener?
)
