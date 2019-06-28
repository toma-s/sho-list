package app.sholist.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import app.sholist.R
import app.sholist.adapter.MainListItemAdapter
import app.sholist.db.DBHandler
import app.sholist.model.ListType
import app.sholist.model.MainListItem
import app.sholist.model.ShoListItem
import kotlinx.android.synthetic.main.activity_main.*

class MainListActivity : AppCompatActivity() {

    private var mainListItemAdapter: MainListItemAdapter? = null
    private var fab: FloatingActionButton? = null
    private var dbHandler: DBHandler? = null
    private var recyclerView: RecyclerView? = null
    private var listMain: MutableList<MainListItem>? = ArrayList()
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mli_toolbar)

        initViews()
        initOperations()
        initDB()
//        this.deleteDatabase("SHO_LIST_APP")
        if (dbHandler!!.listTypeItems().isEmpty()) {
            loadTypes()
        }
    }

    private fun initDB() {
        dbHandler = DBHandler(this)
        listMain = (dbHandler as DBHandler).mainListItems()
        mainListItemAdapter = MainListItemAdapter(listMain!!, applicationContext)
        (recyclerView as RecyclerView).adapter = mainListItemAdapter
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.mli_toolbar)
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.mli_fab)
        recyclerView = findViewById(R.id.main_list_view)
        mainListItemAdapter = MainListItemAdapter(this.listMain!!, applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    private fun initOperations() {
        fab?.setOnClickListener {
            val i = Intent(applicationContext, MainListSetupActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete all the lists").setMessage("Are you sure you want to delete all the lists?")
                .setPositiveButton("Yes") { dialog, _ ->
                    dbHandler!!.deleteAllMainListItems()
                    dbHandler!!.deleteAllShoListItems()
                    initDB()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            return true
        }
        if (id == R.id.action_load_sample) {
            loadMains()
            loadShos()
            super.onResume()
            initDB()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }

    private fun loadTypes() {
        val type = ListType()

        type.name = "Groceries"
        type.image = "ingredients"
        dbHandler!!.createListTypeItem(type)

        type.name = "Home"
        type.image = "home"
        dbHandler!!.createListTypeItem(type)

        type.name = "Clothes"
        type.image = "clothes"
        dbHandler!!.createListTypeItem(type)

        type.name = "Other"
        type.image = "shopping_cart"
        dbHandler!!.createListTypeItem(type)
    }

    private fun loadMains() {
        val item = MainListItem()

        item.name = "Strudel"
        item.desc = "lacking ingredients"
        item.typeId = 1
        dbHandler!!.createMainListItem(item)

        item.name = "Dorm"
        item.desc = "cleaning supplies"
        item.typeId = 2
        dbHandler!!.createMainListItem(item)

        item.name = "Other"
        item.desc = "buy this week"
        item.typeId = 4
        dbHandler!!.createMainListItem(item)
    }

    private fun loadShos() {
        val sho = ShoListItem()
        sho.mainId = 1
        sho.amount = 0.25
        sho.unit = resources.getStringArray(R.array.units)[0]
        sho.name = "Cherries"
        sho.completed = false
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 1
        sho.amount = 1.0
        sho.unit = resources.getStringArray(R.array.units)[5]
        sho.name = "Cinnamon"
        sho.completed = true
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 1
        sho.amount = 1.0
        sho.unit = resources.getStringArray(R.array.units)[0]
        sho.name = "Flour"
        sho.completed = false
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 1
        sho.amount = 10.0
        sho.unit = resources.getStringArray(R.array.units)[4]
        sho.name = "Eggs"
        sho.completed = true
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 2
        sho.amount = 1.0
        sho.unit = resources.getStringArray(R.array.units)[2]
        sho.name = "Dishwashing liquid"
        sho.completed = true
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 2
        sho.amount = 1.0
        sho.unit = resources.getStringArray(R.array.units)[4]
        sho.name = "SAVO"
        sho.completed = false
        dbHandler!!.createShoListItem(sho)

        sho.mainId = 3
        sho.amount = 1.0
        sho.unit = resources.getStringArray(R.array.units)[4]
        sho.name = "Computer mouse"
        sho.completed = false
        dbHandler!!.createShoListItem(sho)
    }

}
