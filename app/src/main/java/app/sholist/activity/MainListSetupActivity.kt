package app.sholist.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import app.sholist.R
import app.sholist.db.DBHandler
import app.sholist.model.MainListItem
import kotlinx.android.synthetic.main.activity_main_setup.*

class MainListSetupActivity : AppCompatActivity() {

    private var dbHandler: DBHandler? = null
    private var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_setup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDB()
        initOperations()
        initSpinner()
    }

    private fun initDB() {
        dbHandler = DBHandler(this)
        btn_delete.visibility = View.INVISIBLE
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val item: MainListItem = dbHandler!!.readMainListItem(intent.getIntExtra("Id",0))
            mli_setup_name.setText(item.name)
            mli_setup_descr.setText(item.desc)
            mli_setup_type_spinner.setSelection(item.typeId!!)
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener {
            val success: Boolean
            if (isEditMode) {
                val item = MainListItem()
                item.id = intent.getIntExtra("Id", 0)
                item.name = mli_setup_name.text.toString()
                item.desc = mli_setup_descr.text.toString()
                item.typeId = getListTypeId(mli_setup_type_spinner.selectedItem.toString())
                success = dbHandler?.updateMainListItem(item) as Boolean
            } else {
                val item = MainListItem()
                item.name = mli_setup_name.text.toString()
                item.desc = mli_setup_descr.text.toString()
                item.typeId = 1
                item.typeId = getListTypeId(mli_setup_type_spinner.selectedItem.toString())
                success = dbHandler?.createMainListItem(item) as Boolean
            }

            if (success) {
                finish()
            }
        }

        btn_delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Click 'YES' Delete the Sho List.")
                .setPositiveButton("YES") { dialog, _ ->
                    val success = dbHandler?.deleteMainListItem(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    private fun initSpinner() {
        val listTypes = dbHandler!!.listTypeItems()
        val listTypesNames : List<String> = listTypes.map { lt -> lt.name.toString() }
        val spinner: Spinner = findViewById(R.id.mli_setup_type_spinner)
        val spinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listTypesNames)
        spinner.adapter = spinnerAdapter
    }

    private fun getListTypeId(name: String) : Int {
        val listTypes = dbHandler!!.listTypeItems()
        val listType = listTypes.filter { e -> e.name.equals(name) }[0]
        return listType.id!!
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