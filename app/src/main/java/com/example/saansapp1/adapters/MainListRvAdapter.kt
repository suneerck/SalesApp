package com.example.saansapp1.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.saansapp1.MainActivity
import com.example.saansapp1.R
import com.example.saansapp1.model.MainListModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class MainListRvAdapter(
        private var arrayList: ArrayList<MainListModel>,
        private val context: MainActivity
):
        RecyclerView.Adapter<MainListRvAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_main_list, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = arrayList[position].name
        holder.tvNo.text = arrayList[position].ticket.toString()
        holder.tvCount.text = arrayList[position].count.toString()
        holder.tvDAmt.text = arrayList[position].dAmt.toString()

        holder.fab.setOnClickListener {

            val builder =AlertDialog.Builder(context)
            builder.setTitle("Delete..!?")
            builder.setMessage("Delete this item..?")

            builder.setPositiveButton("Yes"){ dialogInterface: DialogInterface, i: Int ->

                context.nameFromAdapter = arrayList[position].name
                context.dateTimeFromAdapter = arrayList[position].dateTime
                context.ticketFromAdapter = arrayList[position].ticket.toString()

                context.dropSales()
            }
            builder.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->

            }
            val alertDialog: AlertDialog? = builder.create()
            alertDialog?.show()
        }

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
        var clAdapter : ConstraintLayout = itemView.findViewById(R.id.clAdapter)
        var fab : FloatingActionButton = itemView.findViewById(R.id.fab)
    }

}