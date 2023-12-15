package com.capstone.naghamalquran.ui.vip

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.naghamalquran.R


class VipAdapter (private val listNaghamTypesVip: List<VipDataList>) : RecyclerView.Adapter<VipAdapter.NaghamViewHolderVip>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NaghamViewHolderVip {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nagham_list, parent, false)
        return NaghamViewHolderVip(view)
    }

    override fun onBindViewHolder(holder: NaghamViewHolderVip, position: Int) {
        val (type, desc, img) = listNaghamTypesVip[position]
        Log.d("NaghamAdapter", "Position: $position, Type: $type, Desc: $desc, Photo: $img")


        holder.naghamTypeVip.text = type
        holder.naghamShortDescVip.text = desc
        holder.naghamPictVip.setImageResource(img)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, VipDetail::class.java)
            intentDetail.putExtra("key_nagham", listNaghamTypesVip[holder.adapterPosition])

            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class NaghamViewHolderVip(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naghamPictVip: ImageView = itemView.findViewById(R.id.img_nagham_pict)
        val naghamTypeVip: TextView = itemView.findViewById(R.id.tv_nagham_type)
        val naghamShortDescVip: TextView = itemView.findViewById(R.id.tv_short_description)
    }

    override fun getItemCount(): Int = listNaghamTypesVip.size
}
