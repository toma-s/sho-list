package app.sholist.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import app.sholist.R
import app.sholist.db.DBHandler
import app.sholist.model.ShoListItem

class ShoListItemAdapter(shoList: MutableList<ShoListItem>, internal var context: Context) : RecyclerView.Adapter<ShoListItemAdapter.ShoListViewHolder>() {

    private var shoList: MutableList<ShoListItem> = ArrayList()
    private var dbHandler: DBHandler? = null

    init {
        this.shoList = shoList
        this.dbHandler = DBHandler(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sho_list_item, parent, false)
        return ShoListViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ShoListViewHolder, position: Int) {
        val item = shoList[position]
        holder.name.text = item.name
        holder.completed.isChecked = item.completed!!
        holder.amount.text = String.format(context.resources.getString(R.string.sli_unit_amount_tv), item.amount.toString(), item.unit)
        holder.completed.setOnClickListener {
            item.completed = !item.completed!!
            dbHandler!!.updateShoListItem(item)
            holder.completed.isChecked = item.completed!!
            shoList.sortBy { it.completed }
            notifyDataSetChanged()
        }
        holder.itemView.setOnLongClickListener{
            val dialog = AlertDialog.Builder(context)
                .setTitle("Delete item ${item.name}")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") {dialog, _ ->
                    dbHandler!!.deleteShoListItem(item.id!!)
                    holder.listItem.visibility = ListView.INVISIBLE
                    shoList.removeAt(position)
                    notifyItemRemoved(position)
//                    notifyItemRangeChanged(position, shoList.size)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return shoList.size
    }

    inner class ShoListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.sli_name) as TextView
        var amount: TextView = view.findViewById(R.id.sli_amount) as TextView
        var completed: CheckBox = view.findViewById(R.id.sli_check) as CheckBox
        var listItem: LinearLayout = view.findViewById(R.id.sli_list_item) as LinearLayout
    }
}