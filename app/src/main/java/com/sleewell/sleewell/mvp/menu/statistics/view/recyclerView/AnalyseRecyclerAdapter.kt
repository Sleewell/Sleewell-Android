package com.sleewell.sleewell.mvp.menu.statistics.view.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sleewell.sleewell.R
import com.sleewell.sleewell.mvp.menu.statistics.model.dataClass.AnalyseDetail

class AnalyseRecyclerAdapter(private var statsList: ArrayList<AnalyseDetail>) :
    RecyclerView.Adapter<AnalyseRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconView: ImageView = itemView.findViewById(R.id.IconAnalyseDetail)
        val textView: TextView = itemView.findViewById(R.id.TextAnalyseDetail)
        val divider: View = itemView.findViewById(R.id.StatsItemDivider)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.statistic_info_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = statsList[position].detail
        holder.iconView.setImageIcon(statsList[position].icon)

        if (position == 0)
            holder.divider.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return statsList.size
    }
}