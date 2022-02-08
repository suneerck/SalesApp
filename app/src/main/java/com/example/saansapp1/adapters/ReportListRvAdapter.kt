package com.example.saansapp1.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.saansapp1.R
import com.example.saansapp1.model.MainListModel
import java.util.ArrayList

class ReportListRvAdapter (
        var arrayList: ArrayList<MainListModel>
):
        RecyclerView.Adapter<ReportListRvAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_report_list, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = arrayList[position].name
        holder.tvNo.text = arrayList[position].ticket.toString()
        holder.tvCount.text = arrayList[position].count.toString()
        holder.tvDAmt.text = arrayList[position].dAmt.toString()

        var a = 1
        a = arrayList[position].dAmt * arrayList[position].count

        holder.Amt.text = a.toString()

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvName : TextView = itemView.findViewById(R.id.tvName)
        var tvNo : TextView = itemView.findViewById(R.id.tvNo)
        var tvCount : TextView = itemView.findViewById(R.id.tvCount)
        var tvDAmt : TextView = itemView.findViewById(R.id.tvDAmt)
        var Amt : TextView = itemView.findViewById(R.id.Amt)
    }

}