package com.curlicue.progressivelistmenu.library

import Utils
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.view.ContextThemeWrapper
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.R.attr.data
import android.util.TypedValue




class ProgressiveMenu constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    /**
     * Background color for the items' icons.
     * Leave null for no background.
     * Shape will be a circle.
     * Must set before adding items.
     */
    var iconsBackgroundColor: Int? = null

    /**
     * Style for the sections' title TextView
     */
    var sectionTitleStyle: Int? = null

    /**
     * Style for the items' TextView
     */
    var itemsStyle: Int? = null

    init {
        orientation = LinearLayout.VERTICAL
    }

    /**
     * Add a new section to the menu. Order of which sections are added is respected.
     * @param title Section's title
     * @param items `Array<ProgressiveMenuItem>` with the items for this section
     *
     */
    fun addSection(title: String, items: Array<ProgressiveMenuItem>): ProgressiveMenu {

        val sectionContainer = Utils.makeLinearLayout(context)

        val itemsContainer = object : LinearLayout(context) {
            override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
                return true
            }
        }
        itemsContainer.orientation = orientation
        val sectionContainerLp = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT)
        itemsContainer.layoutParams = sectionContainerLp

        itemsContainer.post {
            sectionContainer.addTouchListener()
            itemsContainer.addOnHeightChange()
        }

        val titleTextViewContext = ContextThemeWrapper(context, sectionTitleStyle ?: R.style.SectionTitleText)
        val titleTextView = TextView(titleTextViewContext)
        titleTextView.text = title

        items.forEach {
            val itemContainer = Utils.makeLinearLayout(context, LinearLayout.HORIZONTAL, Utils.getDp(context, 56.0))
            itemContainer.setOnClickListener(it.clickListener)
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            if (typedValue.resourceId != 0)
                itemContainer.setBackgroundResource(typedValue.resourceId)
            else
                itemContainer.setBackgroundColor(typedValue.data)

            it.icon?.let { icon ->
                val itemIcon = ImageView(context)
                val itemIconLp = LinearLayout.LayoutParams(Utils.getDp(context, 32.0), Utils.getDp(context, 32.0))
                itemIconLp.gravity = Gravity.CENTER_VERTICAL
                itemIconLp.rightMargin = Utils.getDp(context, 12.0)
                itemIcon.layoutParams = itemIconLp
//                itemIcon.setPadding(
//                    Utils.getDp(context, 7.0),
//                    Utils.getDp(context, 7.0),
//                    Utils.getDp(context, 7.0),
//                    Utils.getDp(context, 7.0)
//                )
                itemIcon.setImageResource(icon)
                iconsBackgroundColor?.let {
                    val itemIconBackground = GradientDrawable()
                    itemIconBackground.shape = GradientDrawable.OVAL
                    itemIconBackground.setColor(iconsBackgroundColor!!)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        itemIcon.background = itemIconBackground
                    else
                        itemIcon.setBackgroundDrawable(itemIconBackground)
                }

                itemContainer.addView(itemIcon)
            }
            val itemTextViewContext = ContextThemeWrapper(context, itemsStyle ?: R.style.SectionItemText)
            val itemTextView = TextView(itemTextViewContext)
            itemTextView.text = it.name
            val itemTextViewLp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            itemTextViewLp.gravity = Gravity.CENTER_VERTICAL
            itemTextView.layoutParams = itemTextViewLp

            itemContainer.addView(itemTextView)
            itemsContainer.addView(itemContainer)
        }

        // Create "expand" indicator at the bottom of the section
        val expandContainer = Utils.makeLinearLayout(context)
        expandContainer.gravity = Gravity.CENTER_HORIZONTAL
        val expandText = TextView(context)
        expandText.gravity = Gravity.CENTER_HORIZONTAL
        expandText.text = "Drag to expand $title"
        expandText.setTextColor(Color.parseColor(Utils.EXPAND_COLOR))
        expandText.textSize = 12f
        expandText.setPadding(0, 0, 0, Utils.getDp(context, 8.0))
        val expandGlyph = View(context)
        val expandGlyphLp = LinearLayout.LayoutParams(Utils.getDp(context, 20.0), Utils.getDp(context, 4.0))
        expandGlyph.layoutParams = expandGlyphLp
        val expandGlyphShape = GradientDrawable()
        expandGlyphShape.shape = GradientDrawable.RECTANGLE
        expandGlyphShape.setColor(Color.parseColor(Utils.EXPAND_COLOR))
        expandGlyphShape.cornerRadius = 1000f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            expandGlyph.background = expandGlyphShape
        else
            expandGlyph.setBackgroundDrawable(expandGlyphShape)
        expandContainer.addView(expandText)
        expandContainer.addView(expandGlyph)

        // Add everything to the view
        sectionContainer.addView(titleTextView)
        sectionContainer.addView(itemsContainer)
        sectionContainer.addView(expandContainer)
        this.addView(sectionContainer)
        return this
    }
}


