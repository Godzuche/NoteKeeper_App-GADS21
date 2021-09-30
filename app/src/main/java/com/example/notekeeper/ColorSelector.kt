package com.example.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyleAttr, defStyleRes) {

    }

    private val colorSelectorArrowLeft: ImageView by lazy {findViewById(R.id.colorSelectorArrowLeft)}
    private val colorSelectorArrowRight: ImageView by lazy { findViewById(R.id.colorSelectorArrowRight)}
    private val colorEnabled: CheckBox by lazy { findViewById(R.id.colorEnabled)}
    private val selectedColor: View by lazy { findViewById(R.id.selectedColor)}

    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.DKGRAY, Color.BLACK, Color.GRAY, Color.LTGRAY, Color.MAGENTA, Color.YELLOW)
    private var selectedColorIndex = 0
    init {
        orientation = HORIZONTAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)

        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        colorSelectorArrowLeft.setOnClickListener {
            selectPreviousColor()
        }

        colorSelectorArrowRight.setOnClickListener {
            selectNextColor()
        }

        colorEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            broadcastColor()
        }
    }

    var selectedColorValue : Int = android.R.color.transparent
    set(value) {
        var index = listOfColors.indexOf(value)
        if (index == -1) {
            colorEnabled.isChecked = false
            index = 0
        } else {
            colorEnabled.isChecked = true
        }
        selectedColorIndex = index
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
    }
    private var colorSelectedListener: ((Int) -> Unit)? =null

    fun setListener(color: (Int) -> Unit) {
        colorSelectedListener = color
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex++
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex--
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun broadcastColor() {
        var color = if (colorEnabled.isChecked)
            listOfColors[selectedColorIndex]
        else
            Color.TRANSPARENT
        colorSelectedListener?.let { function ->
            function(color)
        }
    }
}