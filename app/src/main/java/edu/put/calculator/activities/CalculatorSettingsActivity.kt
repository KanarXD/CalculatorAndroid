package edu.put.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.models.CalculatorSettings
import kotlinx.android.synthetic.main.activity_calculator_settings.*

class CalculatorSettingsActivity : AppCompatActivity() {
    private lateinit var currentSettings: CalculatorSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSettings = intent.getParcelableExtra("settings")!!


        setContentView(R.layout.activity_calculator_settings)
        setSupportActionBar(findViewById(R.id.settings_activity_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        number_precision_picker.minValue = 0
        number_precision_picker.maxValue = 10
        number_precision_picker.value = currentSettings.numberPrecision
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val resultIntent = Intent()
            resultIntent.putExtra("settings", CalculatorSettings(number_precision_picker.value))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}