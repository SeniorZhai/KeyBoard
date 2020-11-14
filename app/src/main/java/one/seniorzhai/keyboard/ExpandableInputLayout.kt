/*
 * Copyright 2020 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package one.seniorzhai.keyboard

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.view.View
import android.widget.EditText
import com.squareup.contour.ContourLayout

class ExpandableInputLayout(context: Context) : ContourLayout(context) {
    fun expand() {
        isExpand = true
    }

    fun collapse() {
        isExpand = false
    }

    private var isExpand: Boolean = false
        set(value) {
            field = value
            var start = 8.dip.toFloat()
            var end = 30.dip.toFloat()
            if (value) {
                start = end
                end = 8.dip.toFloat()
            }
            ObjectAnimator.ofFloat(this, "CornerRadius", start, end).setDuration(
                context.resources.getInteger(android.R.integer.config_shortAnimTime)
                    .toLong()
            ).start()
            requestLayout()
        }

    private val editText = EditText(context).apply {
        background = null
        setHintTextColor(Color.LTGRAY)
        setTextColor(Color.BLACK)
        hint = "Write a new task"
    }

    init {
        setBackgroundColor(Color.WHITE)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        clipToOutline = true
        val cw = {
            if (isExpand) {
                320.xdip
            } else {
                200.xdip
            }
        }
        contourWidthOf { cw() }
        contourHeightOf { 60.ydip }

        editText.layoutBy(
            x = leftTo { parent.left() + 16.dip }.rightTo { parent.right() - 16.dip },
            y = topTo { parent.top() }.bottomTo { parent.bottom() }
        )
    }

    private var mCornerRadius: Float = 30.dip.toFloat()
    fun getCornerRadius(): Float {
        return mCornerRadius
    }

    fun setCornerRadius(cornerRadius: Float) {
        mCornerRadius = cornerRadius
        requestLayout()
    }

    override fun draw(canvas: Canvas) {
        val count: Int = canvas.save()
        val path = Path()
        path.addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            mCornerRadius,
            mCornerRadius,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        super.draw(canvas)
        canvas.restoreToCount(count)
    }
}