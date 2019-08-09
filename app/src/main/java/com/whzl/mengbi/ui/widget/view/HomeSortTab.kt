package com.whzl.mengbi.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.whzl.mengbi.R
import kotlinx.android.synthetic.main.view_home_sort_tab.view.*

/**
 *
 * @author nobody
 * @date 2019-08-09
 */
class HomeSortTab : LinearLayout {
    private var select: Boolean
    private var resourceId: Int
    private lateinit var view: View

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HomeSortTab)
        resourceId = array.getResourceId(R.styleable.HomeSortTab_image, R.drawable.selector_composite_home)
        select = array.getBoolean(R.styleable.HomeSortTab_select, false)
        array.recycle()
        init()
    }

    fun init() {
        view = LayoutInflater.from(context).inflate(R.layout.view_home_sort_tab, this)

        if (select) view.view.visibility = View.VISIBLE
        else view.view.visibility = View.INVISIBLE

        view.image.setImageResource(resourceId)
        view.image.isSelected = select
    }

    fun setSelect(select: Boolean) {
        if (select) view.view.visibility = View.VISIBLE
        else view.view.visibility = View.INVISIBLE

        view.image.isSelected = select
    }
}