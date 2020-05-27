package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_seek_bar_int_iem.*

class SeekBarIntDialog(context: Context, themeResId: Int) : FullWithNoTitleDialog(context, themeResId) {
    private var volume = 0

    init {
        setContentView(R.layout.layout_seek_bar_int_iem)
        tv_cancel.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //onSeekBarIntDialogListener?.selectVolume(progress)
                volume = progress
                this@SeekBarIntDialog.tv_volume.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> {
                dismiss()
            }

            R.id.tv_sure -> {
                onSeekBarIntDialogListener?.selectVolume(volume)
                dismiss()
            }
        }
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.TRANSPARENT)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_5)
        }
    }

    var onSeekBarIntDialogListener: OnSeekBarIntDialogListener? = null

    interface OnSeekBarIntDialogListener {
        fun selectVolume(volume: Int)
    }
}