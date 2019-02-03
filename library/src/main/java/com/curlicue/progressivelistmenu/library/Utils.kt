import android.content.Context
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout

object Utils {
    const val LOG_TAG = "progressivemenu"
    const val EXPAND_COLOR = "#237dd2"

    /**
     * Convert Density Independent Pixels (DP) to Pixels
     */
    fun getDp(context: Context, dp: Double): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
        ).toInt()
    }

    fun makeLinearLayout(
        context: Context,
        orientation: Int = LinearLayout.VERTICAL,
        height: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    ): LinearLayout {
        val ll = LinearLayout(context)
        ll.orientation = orientation
        val sectionContainerLp = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        ll.layoutParams = sectionContainerLp
        return ll
    }
}

