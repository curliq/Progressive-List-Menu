package example.curlicue.progressivemenu

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.curlicue.progressivelistmenu.library.ProgressiveMenu
import com.curlicue.progressivelistmenu.library.ProgressiveMenuItem


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<LinearLayout>(R.id.root)
        val progressiveMenu = ProgressiveMenu(this)
        progressiveMenu.iconsBackgroundColor = Color.parseColor("#0D7FC1")
        progressiveMenu
//            .addSection(
//                "Help",
//                arrayOf(
//                    ProgressiveMenuItem("Search a topic", R.drawable.ic_launcher_foreground, null),
//                    ProgressiveMenuItem("Live chat with us", R.drawable.ic_launcher_foreground, null),
//                    ProgressiveMenuItem("Call us", R.drawable.ic_launcher_foreground, null),
//                    ProgressiveMenuItem("Email us", R.drawable.ic_launcher_foreground, null)
//                )
//            )
            .addSection(
                "Community",
                arrayOf(
                    ProgressiveMenuItem(
                        "Your points",
                        R.drawable.ic_launcher_foreground,
                        null
                    ),
                    ProgressiveMenuItem(
                        "Buy & Sell",
                        R.drawable.ic_launcher_foreground,
                        null
                    ),
                    ProgressiveMenuItem(
                        "Your friends",
                        R.drawable.ic_launcher_foreground,
                        null
                    ),
                    ProgressiveMenuItem(
                        "Forum",
                        R.drawable.ic_launcher_foreground,
                        null
                    ),
                    ProgressiveMenuItem(
                        "Profile",
                        R.drawable.ic_launcher_foreground,
                        null
                    )
                )
            )

        root.addView(progressiveMenu)
    }
}
