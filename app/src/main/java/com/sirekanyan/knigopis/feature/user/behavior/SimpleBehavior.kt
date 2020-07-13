package com.sirekanyan.knigopis.feature.user.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.sirekanyan.knigopis.R
import kotlin.math.abs
import kotlin.math.min

@Suppress("unused")
class SimpleBehavior(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private val dependViewId = R.id.user_app_bar
    private val resources = context.resources
    private val endState = SimpleViewState(
        resources.getDimensionPixelOffset(R.dimen.avatar_target_x),
        resources.getDimensionPixelOffset(R.dimen.avatar_target_y),
        resources.getDimensionPixelOffset(R.dimen.avatar_target_width),
        resources.getDimensionPixelOffset(R.dimen.avatar_target_height)
    )
    private val minHeight = resources.getDimensionPixelOffset(R.dimen.toolbar_height)
    private val maxHeight = resources.getDimensionPixelOffset(R.dimen.app_bar_height)
    private var behaviorHelper: BehaviorHelper? = null

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View) =
        dependency.id == dependViewId

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val ratio = abs(dependency.y) / (maxHeight - minHeight)
        getHelper(child.simpleState).updateDimensions(child, min(1f, ratio))
        return false
    }

    private fun getHelper(startState: SimpleViewState): BehaviorHelper =
        behaviorHelper ?: BehaviorHelper(startState, endState).also {
            behaviorHelper = it
        }

}
