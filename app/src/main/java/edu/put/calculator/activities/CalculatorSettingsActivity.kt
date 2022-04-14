package edu.put.calculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.databinding.ActivityCalculatorSettingsBinding
import edu.put.calculator.models.CalculatorSettings

class CalculatorSettingsActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "CalculatorSettingsActivity"
    private lateinit var binding: ActivityCalculatorSettingsBinding
    private lateinit var currentSettings: CalculatorSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentSettings = intent.getParcelableExtra("settings")!!

        setSupportActionBar(findViewById(R.id.settings_activity_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        binding.numberPrecisionPicker.minValue = 0
        binding.numberPrecisionPicker.maxValue = 10
        binding.numberPrecisionPicker.value = currentSettings.numberPrecision

        binding.backgroundColorPicker.displayedValues
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val resultIntent = Intent()
            resultIntent.putExtra(
                "settings",
                CalculatorSettings(binding.numberPrecisionPicker.value)
            )
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}