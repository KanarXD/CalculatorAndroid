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

@Suppress("PrivatePropertyName")
class CalculatorSettingsActivity : AppCompatActivity() {
    private val TAG = "CalculatorSettingsActivity"
    private lateinit var binding: ActivityCalculatorSettingsBinding
    private lateinit var currentSettings: CalculatorSettings
    private val COLOR_VALUES: Array<NamedColor> = arrayOf(
        NamedColor("GREEN", Color.GREEN),
        NamedColor("BLUE", Color.BLUE),
        NamedColor("YELLOW", Color.YELLOW),
        NamedColor("CYAN", Color.CYAN),
        NamedColor("WHITE", Color.WHITE)
    )

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

        binding.backgroundColorPicker.minValue = 0
        binding.backgroundColorPicker.maxValue = COLOR_VALUES.size - 1
        binding.backgroundColorPicker.wrapSelectorWheel = true
        binding.backgroundColorPicker.displayedValues =
            COLOR_VALUES.map { namedColor: NamedColor -> namedColor.name }.toTypedArray()

        val currentColorIndex: Int =
            COLOR_VALUES.indices.firstOrNull { index: Int -> COLOR_VALUES[index].value == currentSettings.backgroundColorValue }
                ?: 0
        binding.backgroundColorPicker.value = currentColorIndex
        binding.backgroundColorPicker.background = getGradientForColorIndex(currentColorIndex)

        binding.backgroundColorPicker.setOnValueChangedListener { _, _, newValue ->
            binding.backgroundColorPicker.background = getGradientForColorIndex(newValue)
        }

    }

    private fun getGradientForColorIndex(index: Int): GradientDrawable {
        val colorArray: IntArray = intArrayOf(
            COLOR_VALUES[if (index - 1 >= 0) index - 1 else COLOR_VALUES.size - 1].value,
            COLOR_VALUES[index].value,
            COLOR_VALUES[if (index + 1 < COLOR_VALUES.size) index + 1 else 0].value
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
                    COLOR_VALUES[binding.backgroundColorPicker.value].value
                )
            )
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}