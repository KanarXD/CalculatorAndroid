package edu.put.calculator.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.service.MainCalculationService
import kotlinx.android.synthetic.main.activity_calculator_main.*

class MainCalculatorActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "MainCalculatorActivity"
    private lateinit var mainCalculationService: MainCalculationService
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_main)
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))
        mainCalculationService = MainCalculationService(this)
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val newSettings: CalculatorSettings = result.data?.getParcelableExtra("settings")!!
                Log.d(TAG, "New settings: $newSettings")
                mainCalculationService.settings = newSettings
                drawStackArea()
            }
        }
    }

    fun onButtonClicked(view: View) {
        mainCalculationService.onButtonClicked(view)
    }

    fun undoCalculatorState(@Suppress("UNUSED_PARAMETER") view: View) {
        mainCalculationService.undoCalculatorState()
    }

    @SuppressLint("SetTextI18n")
    fun drawStackArea() {
        stack_counter.text = "STACK: ${mainCalculationService.currentState.numberStack.size}"
        numberInputText.text = mainCalculationService.currentState.numberInputString
        stack_level_1.text = mainCalculationService.prepareStackValue(1)
        stack_level_2.text = mainCalculationService.prepareStackValue(2)
        stack_level_3.text = mainCalculationService.prepareStackValue(3)
        stack_level_4.text = mainCalculationService.prepareStackValue(4)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSettingsButtonClicked(view: View) {
        val intent = Intent(this, CalculatorSettingsActivity::class.java)
        intent.putExtra("settings", mainCalculationService.settings)
        activityResultLauncher.launch(intent)
    }

}