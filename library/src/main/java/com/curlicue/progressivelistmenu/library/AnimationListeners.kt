package com.curlicue.progressivelistmenu.library

import Utils
import android.animation.ValueAnimator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout

private const val flingExpandDuration = 150.toLong()

fun LinearLayout.addTouchListener() {
    val v = this.getChildAt(1) as LinearLayout
    val itemsCount = v.childCount
    if (itemsCount == 0) {
        Log.e(Utils.LOG_TAG, "A container has to have at least one item in it")
        return
    }
    val itemHeight = v.getChildAt(0).height
    val expandedHeight = itemHeight * itemsCount

    // history of the last 2 Y positions to calculate direction
    val downY = FloatArray(2)

    this.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.y - downY[0]
                    if (deltaY > 0) {
                        // scrolling from top to bottom
                        if (v.height < expandedHeight) {
                            val lp = v.layoutParams
                            var result = v.height + deltaY.toInt()
                            if (result > expandedHeight)
                                result = expandedHeight
                            lp.height = result
                            v.layoutParams = lp
                        }
                    }
                    else {
                        // scrolling from bottom to top
                        if (v.height > itemHeight) {
                            val lp = v.layoutParams
                            var result = v.height + deltaY.toInt()
                            if (result > expandedHeight)
                                result = expandedHeight
                            lp.height = result
                            v.layoutParams = lp
                        }
                    }
                    downY[1] = downY[0]
                    downY[0] = event.y
                }
                MotionEvent.ACTION_UP -> {
                    val deltaY = event.y - downY[1]
                    if (deltaY > 0)
                        toggleGroup(true, v, expandedHeight)
                    else
                        toggleGroup(false, v, itemHeight)

                    return true
                }
            }
            return true
        }
    })
}

private fun toggleGroup(expand: Boolean, v: View, endHeight: Int) {
    if (expand) {
        // expand it
        val anim = ValueAnimator.ofInt(v.measuredHeight, endHeight)
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val lp = v.layoutParams
            lp.height = valueAnimator.animatedValue as Int
            v.layoutParams = lp
        }
        anim.duration = flingExpandDuration
        anim.start()
    }
    else {
        // collapse
        val anim = ValueAnimator.ofInt(v.measuredHeight, endHeight)
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val lp = v.layoutParams
            lp.height = valueAnimator.animatedValue as Int
            v.layoutParams = lp
        }
        anim.duration = flingExpandDuration
        anim.start()
    }
}


/**
 * Handle animation to show/hide each item
 */
fun LinearLayout.addOnHeightChange() {
    val itemHeight = this.getChildAt(0).height
    val itemsCount = this.childCount

    // create array of Triple(item, itemStartPosition, itemEndPosition) where the positions are relative to the
    // start of the container they're in
    val items = Array(itemsCount) { i ->
        Triple(this.getChildAt(i), itemHeight * i, itemHeight * (i + 1))
    }

    this.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
        val containerHeight = bottom - top
        items.forEachIndexed { index, item ->
            // add transparency to the element current coming to screen
            if (index > 0) {
                val a = 100 / (itemHeight * if (index + 1 == itemsCount) 1f else 1.5f)
                item.first.alpha = (containerHeight - item.second) * a / 100f
            }
        }
    }
}
