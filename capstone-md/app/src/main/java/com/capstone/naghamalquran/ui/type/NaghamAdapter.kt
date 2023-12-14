package com.capstone.naghamalquran.ui.type

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.naghamalquran.R

class NaghamAdapter(private val listNaghamTypes: List<NaghamList>) : RecyclerView.Adapter<NaghamAdapter.NaghamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NaghamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.nagham_list, parent, false)
        return NaghamViewHolder(view)
    }

    override fun onBindViewHolder(holder: NaghamViewHolder, position: Int) {
        val (type, desc, img) = listNaghamTypes[position]
        Log.d("NaghamAdapter", "Position: $position, Type: $type, Desc: $desc, Photo: $img")


        holder.naghamType.text = type
        holder.naghamShortDesc.text = desc
        holder.naghamPict.setImageResource(img)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, NaghamDetail::class.java)
            intentDetail.putExtra("key_nagham", listNaghamTypes[holder.adapterPosition])

            holder.itemView.context.startActivity(intentDetail)
        }
    }


    class NaghamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val naghamPict: ImageView = itemView.findViewById(R.id.img_nagham_pict)
        val naghamType: TextView = itemView.findViewById(R.id.tv_nagham_type)
        val naghamShortDesc: TextView = itemView.findViewById(R.id.tv_short_description)
    }

    override fun getItemCount(): Int = listNaghamTypes.size
}
