package edu.put.calculator.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.databinding.ActivityCalculatorSettingsBinding
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.models.NamedColor

private const val TAG = "CalculatorSettingsActivity"


class CalculatorSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorSettingsBinding
    private lateinit var currentSettings: CalculatorSettings
    private lateinit var colorValues: Array<NamedColor>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentSettings = intent.getParcelableExtra("settings")!!

        setSupportActionBar(findViewById(R.id.settings_activity_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        colorValues = arrayOf(
            NamedColor(getString(R.string.green), Color.GREEN),
            NamedColor(getString(R.string.blue), Color.BLUE),
            NamedColor(getString(R.string.yellow), Color.YELLOW),
            NamedColor(getString(R.string.cyan), Color.CYAN),
            NamedColor(getString(R.string.white), Color.WHITE)
        )

        binding.numberPrecisionPicker.minValue = 0
        binding.numberPrecisionPicker.maxValue = 10
        binding.numberPrecisionPicker.value = currentSettings.numberPrecision

        binding.backgroundColorPicker.minValue = 0
        binding.backgroundColorPicker.maxValue = colorValues.size - 1
        binding.backgroundColorPicker.wrapSelectorWheel = true
        binding.backgroundColorPicker.displayedValues =
            colorValues.map { namedColor: NamedColor -> namedColor.name }.toTypedArray()

        val currentColorIndex: Int =
            colorValues.indices.firstOrNull { index: Int -> colorValues[index].value == currentSettings.backgroundColorValue }
                ?: 0
        binding.backgroundColorPicker.value = currentColorIndex
        binding.backgroundColorPicker.background = getGradientForColorIndex(currentColorIndex)

        binding.backgroundColorPicker.setOnValueChangedListener { _, _, newValue ->
            binding.backgroundColorPicker.background = getGradientForColorIndex(newValue)
        }

    }

    private fun getGradientForColorIndex(index: Int): GradientDrawable {
        val colorArray: IntArray = intArrayOf(
            colorValues[if (index - 1 >= 0) index - 1 else colorValues.size - 1].value,
            colorValues[index].value,
            colorValues[if (index + 1 < colorValues.size) index + 1 else 0].value
        )
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorArray)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val resultIntent = Intent()
            resultIntent.putExtra(
                "settings",
                CalculatorSettings(
                    binding.numberPrecisionPicker.value,
                    colorValues[binding.backgroundColorPicker.value].value
                )
            )
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}