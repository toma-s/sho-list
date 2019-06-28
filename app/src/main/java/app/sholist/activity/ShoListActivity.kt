package app.sholist.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import app.sholist.adapter.ShoListItemAdapter
import app.sholist.db.DBHandler
import app.sholist.model.ShoListItem
import app.sholist.R

class ShoListActivity : AppCompatActivity() {

    private var shoListItemAdapter: ShoListItemAdapter? = null
    private var fab: FloatingActionButton? = null
    private var dbHandler: DBHandler? = null
    private var recyclerView: RecyclerView? = null
    private var listSho: MutableList<ShoListItem>? = ArrayList()
    private var linearLayoutManager: LinearLayoutManager? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sho)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        initOperations()
        initDB()
        supportActionBar!!.title = intent.getStringExtra("MainName")
    }


    private fun initDB() {
        val mainId = intent.getIntExtra("MainId", 0)
        dbHandler = DBHandler(this)
        listSho = (dbHandler as DBHandler).shoListItemsByMainId(mainId)
        shoListItemAdapter = ShoListItemAdapter(listSho!!, this)
        (recyclerView as RecyclerView).adapter = shoListItemAdapter
    }

    private fun initViews() {
        fab = findViewById(R.id.sli_fab)
        recyclerView = findViewById(R.id.sho_list_view)
        shoListItemAdapter = ShoListItemAdapter(this.listSho!!, this)
        linearLayoutManager = LinearLayoutManager(this)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    private fun initOperations() {
        fab?.setOnClickListener {
            val i = Intent(applicationContext, ShoListAddActivity::class.java)
            i.putExtra("Mode", "A")
            i.putExtra("ShoId", intent.getIntExtra("MainId", 0))
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}