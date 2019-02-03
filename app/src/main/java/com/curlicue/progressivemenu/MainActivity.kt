package com.curlicue.progressivemenu

import Utils
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.view.animation.DecelerateInterpolator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val container1 = findViewById<LinearLayout>(R.id.container_1)

        container1.post {
            container1.addDrawerListener()
            container1.addOnHeightChange()
        }

    }

    private val flingExpandDuration = 150.toLong()

    private fun LinearLayout.addDrawerListener() {
        val v = this
        val itemsCount = v.childCount
        if (itemsCount == 0) {
            Log.e(Utils.LOG_TAG, "A container has to have at least one item in it")
            return
        }
        val itemHeight = v.getChildAt(0).height
        val expandedHeight = itemHeight * itemsCount

        val flingAction = GestureDetector(v.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                if (distanceY < 0) {
                    // Scrolled top to bottom and expand
                    val lp = v.layoutParams
                    if (v.height < expandedHeight) {
                        var result = v.height - distanceY.toInt()
                        if (result > expandedHeight)
                            result = expandedHeight
                        lp.height = result
                        v.layoutParams = lp
                    }
                }
                else if (distanceY > 0) {
                    // Scrolled bottom to top and collapse
                    val lp = v.layoutParams
                    if (v.height > itemHeight) {
                        var result = v.height - distanceY.toInt()
                        if (result < itemHeight)
                            result = itemHeight
                        lp.height = result
                        v.layoutParams = lp
                    }
                }
                return true
            }
        })

        // history of the last 2 Y positions to calculate direction
        val downY = FloatArray(2)

        v.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                flingAction.onTouchEvent(event)
                when (event.actionMasked) {
                    MotionEvent.ACTION_MOVE -> {
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
    private fun LinearLayout.addOnHeightChange() {
        val itemHeight = this.getChildAt(0).height
        val itemsCount = this.childCount

        // create array of Triple(item, itemStartPosition, itemEndPosition) where the positions are relative to the
        // start of the container they're in
        val items = Array(itemsCount) { i ->
            Triple(this.getChildAt(i), itemHeight * i, itemHeight * (i + 1))
        }
        items.forEach { Log.i("tagg", it.toString()) }

        this.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            val containerHeight = bottom - top
            Log.i("tagg", "called")
            items.forEachIndexed { index, item ->
                // add transparency to the element current coming to screen
                if (index > 0) {
                    val a = 100 / (itemHeight * if (index + 1 == itemsCount) 1f else 1.5f)
                    item.first.alpha = (containerHeight - item.second) * a / 100f
                }
            }
        }
    }
}
