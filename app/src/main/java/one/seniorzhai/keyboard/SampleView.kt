package one.seniorzhai.keyboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.squareup.contour.ContourLayout

@SuppressLint("SetTextI18n")
class SampleView(context: Context) : ContourLayout(context) {
    private val editText = ExpandableInputLayout(context)

    private var keyBottom = 0
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
        set(value) {
            field = value
            if (value > 30.dip) {
                editText.expand()
            } else {
                editText.collapse()
            }
            TransitionManager.beginDelayedTransition(
                this, AutoTransition()
                    .setInterpolator(if (value>30.dip){
                        DecelerateInterpolator()
                    }else{
                        AccelerateInterpolator()
                    })
                    .setDuration(context.resources.getInteger(android.R.integer.config_shortAnimTime)
                        .toLong())
            )
            requestLayout()
        }

    init {
        setBackgroundColor(Color.parseColor("#303030"))
        contourHeightMatchParent()
        val bp = { keyBottom + 16.dip }
        editText.layoutBy(
            x = centerHorizontallyTo { parent.centerX() },
            y = bottomTo { parent.bottom() - bp() }
        )
        ViewCompat.setOnApplyWindowInsetsListener(this) { _: View?, insets: WindowInsetsCompat ->
            val systemBarInsets = insets.getInsets(
                WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.systemBars()
            )
            keyBottom = systemBarInsets.bottom
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        updatePadding(top = insets.systemWindowInsetTop)
        return super.onApplyWindowInsets(insets)
    }
}
