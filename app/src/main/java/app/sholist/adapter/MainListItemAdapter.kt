package app.sholist.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.sholist.activity.MainListSetupActivity
import app.sholist.R
import app.sholist.activity.ShoListActivity
import app.sholist.db.DBHandler
import app.sholist.model.ListType
import app.sholist.model.MainListItem

class MainListItemAdapter(mainList: MutableList<MainListItem>, internal var context: Context) : RecyclerView.Adapter<MainListItemAdapter.MainListViewHolder>() {

    private var mainList: MutableList<MainListItem> = ArrayList()

    init {
        this.mainList = mainList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_list_item, parent, false)
        return MainListViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val item = mainList[position]
        val listType = getListType(item)
        holder.name.text = item.name
        if (item.desc!!.isNotEmpty()) {
            holder.desc.text = String.format(context.resources.getString(R.string.mli_decr_tv), listType.name, item.desc)
        } else {
            holder.desc.text = String.format(context.resources.getString(R.string.mli_no_decr_tv), listType.name)
        }
        holder.image.setImageResource(context.resources.getIdentifier(listType.image, "drawable", "app.sholist"))
//        holder.image.setImageResource(context.resources.getIdentifier("shopping_cart", "drawable", "app.sholist"))

        holder.itemView.setOnLongClickListener {
            val i = Intent(context, MainListSetupActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", item.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            val i = Intent(context, ShoListActivity::class.java)
            i.putExtra("MainId", item.id)
            i.putExtra("MainName", item.name)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    private fun getListType(item: MainListItem) : ListType {
        val dbHandler = DBHandler(context)
        return dbHandler.readListTypeItem(item.typeId!!)
    }

    inner class MainListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.mli_name) as TextView
        var desc: TextView = view.findViewById(R.id.mli_descr) as TextView
        var image: ImageView = view.findViewById(R.id.mli_image) as ImageView
    }
}