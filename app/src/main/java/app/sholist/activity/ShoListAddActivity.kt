package app.sholist.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import app.sholist.db.DBHandler
import app.sholist.model.ShoListItem
import kotlinx.android.synthetic.main.activity_sho_setup.*
import android.widget.*
import app.sholist.R
import java.lang.Exception

class ShoListAddActivity : AppCompatActivity() {

    private var dbHandler: DBHandler? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sho_setup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initDB()
        initOperations()
        initSpinner()
    }

    private fun initDB() {
        dbHandler = DBHandler(this)
        btn_delete.visibility = View.INVISIBLE
    }

    private fun initSpinner() {
        val spinner: Spinner = findViewById(R.id.sli_setup_unit_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.units,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener {
            var success = false
            val item = ShoListItem()
            try {
                item.name = sli_setup_name_ed.text.toString()
                item.mainId = intent.getIntExtra("ShoId", 0)
                item.unit = sli_setup_unit_spinner.selectedItem.toString()
                item.amount = Math.round(sli_setup_amount_ed.text.toString().toDouble() * 100.0) / 100.0
                item.completed = false
                if (item.name != "" && item.amount!! > 0) {
                    success = dbHandler?.createShoListItem(item) as Boolean
                }
            } catch (e: Exception) {
                failToast()
            }
            if (success) finish()
            else failToast()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun failToast() {
        Toast.makeText(this, "Set correct parameters to add the item", Toast.LENGTH_SHORT).show()
    }
}