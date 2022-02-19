package me.felwal.android.widget

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.felwal.android.R
import me.felwal.android.databinding.FwItemFloatingactionmenuMiniBinding
import me.felwal.android.databinding.FwItemFloatingactionmenuOverlayBinding
import me.felwal.android.databinding.FwViewFloatingactionmenuBinding
import me.felwal.android.util.backgroundTint
import me.felwal.android.util.contentView
import me.felwal.android.util.crossfadeIn
import me.felwal.android.util.crossfadeOut
import me.felwal.android.util.getActivity
import me.felwal.android.util.getColorByAttr
import me.felwal.android.util.getDrawableCompat
import me.felwal.android.util.layoutInflater
import me.felwal.android.util.log
import me.felwal.android.util.withFilter

class FloatingActionMenu(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    var isMenuOpen = false

    private val animators: MutableList<ViewPropertyAnimator> = mutableListOf()

    private val inflater = context.layoutInflater

    private var binding = FwViewFloatingactionmenuBinding.inflate(inflater, this, true)
    private var itemBindings: MutableList<FwItemFloatingactionmenuMiniBinding> = mutableListOf()
    private lateinit var overlayBinding: FwItemFloatingactionmenuOverlayBinding

    val fab get() = binding.fwFab
    val overlay get() = overlayBinding.root
    val miniFabs get() = itemBindings.map { it.fabMenuItem }
    val closedImageView get() = binding.fwIvFabClosedIcon
    val openedImageView get() = binding.fwIvFabOpenedIcon

    @ColorInt private var closedFabColor: Int
    @ColorInt private var openedFabColor: Int
    @ColorInt private var miniFabColor: Int

    @ColorInt private var closedIconTint: Int
    @ColorInt private var openedIconTint: Int
    @ColorInt private var miniIconTint: Int

    private var closedIconSrc: Drawable?
    private var openedIconSrc: Drawable?

    private var overlayAlpha: Float
    private var animDuration: Long
    private var animRotation: Float

    private var firstMenuItemAsMainFab: Boolean

    //

    init {
        // get attrs
        context.theme.obtainStyledAttributes(attrs, R.styleable.FloatingActionMenu, 0, 0).apply {
            try {
                // container colors
                closedFabColor = getColor(
                    R.styleable.FloatingActionMenu_fw_closedFabColor,
                    context.getColorByAttr(R.attr.colorSecondary)
                )
                openedFabColor = getColor(
                    R.styleable.FloatingActionMenu_fw_openedFabColor,
                    context.getColorByAttr(R.attr.colorSurface)
                )
                miniFabColor = getColor(
                    R.styleable.FloatingActionMenu_fw_miniFabColor,
                    context.getColorByAttr(R.attr.colorSecondary)
                )

                // icon tints
                closedIconTint = getColor(
                    R.styleable.FloatingActionMenu_fw_closedIconTint,
                    context.getColorByAttr(R.attr.colorOnSecondary)
                )
                openedIconTint = getColor(
                    R.styleable.FloatingActionMenu_fw_openedIconTint,
                    context.getColorByAttr(R.attr.colorOnSurface)
                )
                miniIconTint = getColor(
                    R.styleable.FloatingActionMenu_fw_miniIconTint,
                    context.getColorByAttr(R.attr.colorOnSecondary)
                )

                // icon drawables
                closedIconSrc = getDrawable(R.styleable.FloatingActionMenu_fw_closedIconSrc)
                    ?: context.getDrawableCompat(R.drawable.fw_ic_add_24)
                openedIconSrc = getDrawable(R.styleable.FloatingActionMenu_fw_openedIconSrc)
                    ?: context.getDrawableCompat(R.drawable.fw_ic_clear_24)

                //
                overlayAlpha = getFloat(R.styleable.FloatingActionMenu_fw_overlayAlpha, 96f)
                animDuration = getInt(R.styleable.FloatingActionMenu_fw_animDuration, 150).toLong()
                animRotation = getFloat(R.styleable.FloatingActionMenu_fw_animRotation, 135f)

                //
                firstMenuItemAsMainFab = getBoolean(R.styleable.FloatingActionMenu_fw_firstMenuItemAsMainFab, false)
            }
            finally {
                recycle()
            }
        }

        //
        closedImageView.isVisible = false
        openedImageView.isVisible = false

        // open/close
        fab.setOnClickListener { toggleMenu() }
    }

    /**
     * Inflates and initiates the overlay. Must be called after [Activity.setContentView].
     */
    fun onSetContentView() {
        val contentView = getActivity()!!.contentView!! as ViewGroup
        overlayBinding = FwItemFloatingactionmenuOverlayBinding.inflate(inflater, contentView, true)

        // close on outside click
        overlay.setOnClickListener { closeMenu() }
    }

    //

    /**
     * Only use this if [View.setOnScrollChangeListener] is not called somewhere else.
     * Otherwise use [updateVisibilityOnScroll].
     */
    fun setAutoUpdateVisibilityOnScroll(scrollingContainer: View) =
        scrollingContainer.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            // show/hide fab
            updateVisibilityOnScroll(scrollY - oldScrollY)
        }

    fun updateVisibilityOnScroll(dy: Int) {
        // prevent the menu from staying open while the fab is being hidden
        if (!isMenuOpen && fab.isOrWillBeShown && dy > 0) fab.hide()
        else if (fab.isOrWillBeHidden && dy < 0) fab.show()
    }

    fun addItem(title: String, @DrawableRes iconRes: Int, onClick: (View) -> Unit) {
        val itemBinding = FwItemFloatingactionmenuMiniBinding.inflate(inflater, binding.root, false)
        // neccessary for getting the right index
        binding.root.addView(itemBinding.root, 0)

        itemBinding.tvMenuItemTitle.text = title
        context.getDrawableCompat(iconRes)?.withFilter(miniIconTint)?.let {
            itemBinding.fabMenuItem.setImageDrawable(it)
        }
        itemBinding.fabMenuItem.setOnClickListener(onClick)

        itemBindings.add(itemBinding)

        if (itemBindings.size > 6) log.w(
            "A fab menu should per Material guidelines not contain more than 6 items: " +
                "https://material.io/components/buttons-floating-action-button#types-of-transitions"
        )
    }

    //

    fun toggleMenu() = if (isMenuOpen) closeMenu() else openMenu()

    fun openMenu() {
        // dont open the menu while the fab is hiding
        if (fab.isOrWillBeHidden) return
        isMenuOpen = true

        animators.cancelAndClear()

        // overlay
        animators += overlay.crossfadeIn(animDuration, overlayAlpha)

        // container
        fab.animateFab(closedFabColor, openedFabColor)

        // icons
        closedImageView.apply {
            rotate(0f, animRotation)
            isVisible = true
            // remove fab icon, as we cannot rotate it (since that rotates the container as well)
            fab.setImageDrawable(null)
            animators += crossfadeOut(animDuration)
        }
        openedImageView.apply {
            animators += crossfadeIn(animDuration)
            rotate(-animRotation, 0f)
        }

        // items
        for (itemBinding in itemBindings) {
            itemBinding.fabMenuItem.show()
            animators += itemBinding.root.crossfadeIn(animDuration)
        }
    }

    fun closeMenu() {
        isMenuOpen = false

        animators.cancelAndClear()

        // overlay
        animators += overlay.crossfadeOut(animDuration)

        // container
        fab.animateFab(openedFabColor, closedFabColor)

        // icons
        closedImageView.apply {
            rotate(animRotation, 0f)
            animators += crossfadeIn(animDuration) {
                // readd fab icon, as we want the fab hiding animation
                fab.setImageDrawable(closedIconSrc)
                isVisible = false
            }
        }
        openedImageView.apply {
            animators += crossfadeOut(animDuration)
            rotate(0f, -animRotation)
        }

        // items
        for (itemBinding in itemBindings) {
            itemBinding.fabMenuItem.hide()
            animators += itemBinding.root.crossfadeOut(animDuration)
        }
    }

    private fun MutableList<ViewPropertyAnimator>.cancelAndClear() {
        forEach { it.cancel() }
        clear()
    }

    private fun View.rotate(from: Float, to: Float) {
        ObjectAnimator.ofFloat(this, "rotation", from, to).apply {
            duration = animDuration
            start()
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun FloatingActionButton.animateFab(@ColorInt fromColor: Int, @ColorInt toColor: Int) {
        ObjectAnimator.ofInt(this, "backgroundTint", fromColor, toColor).apply {
            duration = animDuration
            setEvaluator(ArgbEvaluator())

            addUpdateListener { animator: ValueAnimator ->
                backgroundTint = animator.animatedValue as Int
            }

            start()
        }
    }
}