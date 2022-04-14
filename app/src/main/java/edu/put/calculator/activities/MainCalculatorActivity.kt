package edu.put.calculator.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.databinding.ActivityCalculatorMainBinding
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.services.MainCalculationService

class MainCalculatorActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "MainCalculatorActivity"
    private lateinit var binding: ActivityCalculatorMainBinding
    private lateinit var mainCalculationService: MainCalculationService
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))
        mainCalculationService = MainCalculationService(this)
        binding.calculatorStackArea.setBackgroundColor(mainCalculationService.settings.backgroundColorValue)
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

    fun onNumberButtonClicked(view: View) {
        mainCalculationService.onNumberButtonClicked(view as Button)
    }

    fun onCalculationButtonClicked(view: View) {
        mainCalculationService.onCalculationButtonClicked(view as Button)
    }

    fun onActionButtonClicked(view: View) {
        mainCalculationService.onActionButtonClicked(view as Button)
    }

    fun undoCalculatorState(@Suppress("UNUSED_PARAMETER") view: View) {
        mainCalculationService.undoCalculatorState()
    }

    @SuppressLint("SetTextI18n")
    fun drawStackArea() {
        binding.calculatorStackArea.setBackgroundColor(mainCalculationService.settings.backgroundColorValue)
        binding.stackCounter.text = "STACK: ${mainCalculationService.currentState.numberStack.size}"
        binding.numberInputText.text = mainCalculationService.currentState.numberInputString
        binding.stackLevel1.text = mainCalculationService.prepareStackValue(1)
        binding.stackLevel2.text = mainCalculationService.prepareStackValue(2)
        binding.stackLevel3.text = mainCalculationService.prepareStackValue(3)
        binding.stackLevel4.text = mainCalculationService.prepareStackValue(4)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSettingsButtonClicked(view: View) {
        val intent = Intent(this, CalculatorSettingsActivity::class.java)
        intent.putExtra("settings", mainCalculationService.settings)
        activityResultLauncher.launch(intent)
    }

}