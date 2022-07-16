package com.document.officereader.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amazic.ads.callback.InterCallback
import com.amazic.ads.util.Admod
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.document.officereader.R
import com.document.officereader.util.Utils
import kotlinx.android.synthetic.main.layout_item_list_file.view.*
import java.io.File
import java.util.*

class ListFileOfficeAdapter(var mFiles: ArrayList<File>, var onItemListener: OnItemListener) :
    RecyclerView.Adapter<ListFileOfficeAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        /*for ( file in mFiles){
            Log.e("xxx",file.name);
        }*/
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_list_file, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // if ((mFiles.size == 0&& position==0) || (mFiles.size > 0 && position == 1)) {
//            val itemView = holder.itemView
//            itemView.my_main.visibility = View.GONE
//            itemView.my_template.visibility = View.VISIBLE
//            val adLoader = AdLoader.Builder(
//                itemView.context,
//                itemView.context.getString(R.string.ads_native_id)
//            )
//                .forNativeAd { nativeAd ->
//                    val styles = NativeTemplateStyle.Builder().build()
//                    itemView.my_template.setStyles(styles)
//                    itemView.my_template.setNativeAd(nativeAd)
//                }
//                .withAdListener(object : AdListener() {
//                    override fun onAdFailedToLoad(adError: LoadAdError) {
//                        itemView.my_template.visibility = View.GONE
//                    }
//                })
//                .withNativeAdOptions(
//                    NativeAdOptions.Builder()
//                        .build()
//                )
//                .build()
//
//            adLoader.loadAd(AdRequest.Builder().build())
        // } else {
        val itemView = holder.itemView
        itemView.my_main.visibility = View.VISIBLE
        itemView.my_template.visibility = View.GONE
        var pos = position
//            if (position > 0) {
//                pos = position - 1
//            }
        val file = mFiles[pos]
        val name = file.name
        if (name.endsWith(".pdf")) {
            itemView.imgType.setImageResource(R.drawable.sub_pdf)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#FF000B"),
                PorterDuff.Mode.SRC_ATOP
            )
        } else if (name.endsWith(".doc") || name.endsWith(".docx")) {
            itemView.imgType.setImageResource(R.drawable.sub_doc)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#0059D3"),
                PorterDuff.Mode.SRC_ATOP
            )
        } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
            itemView.imgType.setImageResource(R.drawable.sub_xlsx)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#08A747"),
                PorterDuff.Mode.SRC_ATOP
            )
        } else if (name.endsWith(".pptx") || name.endsWith(".ppt")) {
            itemView.imgType
                .setImageResource(R.drawable.sub_ppt)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#DD7719"),
                PorterDuff.Mode.SRC_ATOP
            )
        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
            itemView.imgType
                .setImageResource(R.drawable.sub_png)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#FF9900"),
                PorterDuff.Mode.SRC_ATOP
            )
        } else if (name.endsWith(".txt")) {
            itemView.imgType.setImageResource(R.drawable.sub_txt)
            itemView.view_item.background.setColorFilter(
                Color.parseColor("#4D8CBD"),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        itemView.tvName.text = name
        val time = file.lastModified()
        itemView.tvTime.text =
            android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", Date(time))
        var isFavorite = Utils.isFileFavorite(file.absolutePath, itemView.context)
        if (isFavorite) {
            itemView.imgFavorite.setImageResource(R.drawable.ic_select_book_mark)

        } else {
            itemView.imgFavorite.setImageResource(R.drawable.ic_unselect_book_mark)
        }

        itemView.imgFavorite.setOnClickListener {
            if (isFavorite) {
                isFavorite = false
                itemView.imgFavorite.setImageResource(R.drawable.ic_unselect_book_mark)
            } else {
                isFavorite = true
                itemView.imgFavorite.setImageResource(R.drawable.ic_select_book_mark)
            }
            Utils.setFileFavorite(file.absolutePath, itemView.context, isFavorite)
        }

        itemView.setOnClickListener {
            onItemListener.onItemClick(name, file.absolutePath)

        }
        //   }
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    interface OnItemListener {
        fun onItemClick(name: String, url: String)
    }


}