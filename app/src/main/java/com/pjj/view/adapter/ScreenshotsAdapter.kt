package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.module.ScreenshotsBean
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.ImageDialog
import com.pjj.view.dialog.SaveImageDialog
import android.content.Intent
import android.provider.MediaStore
import android.net.Uri
import android.view.*
import android.widget.Toast
import com.pjj.R
import java.io.File


/**
 * Created by XinHeng on 2019/04/01.
 * describe：
 */
class ScreenshotsAdapter(private var activity: Activity) : RecyclerView.Adapter<ScreenshotsAdapter.ScreenshotsHolder>() {
    var imgs: MutableList<ScreenshotsBean.ImgsBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotsHolder {
        return ScreenshotsHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_screenshots_item, parent, false))
    }

    override fun getItemCount(): Int {
        return imgs?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScreenshotsHolder, position: Int) {
        holder.tv.text = "截屏${position + 1}"
        holder.iv.setOnClickListener {
            //PreviewScreenShotsActivity.newInstance(activity, PjjApplication.filePath + imgs!![position].printScreen)
            showDialog(PjjApplication.filePath + imgs!![position].printScreen)
        }
        Glide.with(holder.itemView).load(PjjApplication.filePath + imgs!![position].printScreen).into(holder.iv)
    }

    class ScreenshotsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = itemView.findViewById<ImageView>(R.id.iv)!!
        var tv = itemView.findViewById<TextView>(R.id.tv)!!
    }


    private fun showDialog(path: String) {
        val dialog = ImageDialog(activity).apply {
            onImageDialogListener = object : ImageDialog.OnImageDialogListener {
                override fun saveImage(path: String?) {
                    path?.let {
                        showSaveDialog(it)
                    }
                }
            }
        }
        dialog.setOnCancelListener {
            activity.window.statusBarColor = ViewUtils.getColor(R.color.color_theme)
            activity.window.navigationBarColor = Color.WHITE
        }
        dialog.setImagePath(path, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.statusBarColor = Color.BLACK
            activity.window.navigationBarColor = Color.BLACK
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    private fun showSaveDialog(path: String) {
        val saveImageDialog = SaveImageDialog(activity).apply {
            onDownloadListener = object : SaveImageDialog.OnSaveImageDialogListener {
                override fun success(filePath: String?) {
                    //onScreenshotsAdapterListener?.dissmissNotice()
                    activity.runOnUiThread {
                        onScreenshotsAdapterListener?.notice("保存成功")
                        val file = File(filePath!!)
                        MediaStore.Images.Media.insertImage(activity.contentResolver, filePath, file.name, "广告屏截屏")
                        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        val uri = Uri.fromFile(file)
                        intent.data = uri
                        activity.sendBroadcast(intent)
                        this@apply.dismiss()
                    }
                }

                override fun fail() {
                    activity.runOnUiThread {
                        onScreenshotsAdapterListener?.notice("保存失败")
                        //onScreenshotsAdapterListener?.dissmissNotice()
                        this@apply.dismiss()
                    }
                }

                override fun showWaiteNotice() {
                    onScreenshotsAdapterListener?.showWaiteNotice()
                }
            }
        }
        saveImageDialog.path = path
        saveImageDialog.show()
    }

    var onScreenshotsAdapterListener: OnScreenshotsAdapterListener? = null

    interface OnScreenshotsAdapterListener {
        fun notice(msg: String)
        fun showWaiteNotice()
        fun dissmissNotice()
    }
}