package edu.put.calculator.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import edu.put.calculator.R
import edu.put.calculator.databinding.ActivityCalculatorMainBinding
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.services.MainCalculationService
import kotlin.math.abs

private const val TAG = "MainCalculatorActivity"

class MainCalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalculatorMainBinding
    private lateinit var gestureDetectorCompat: GestureDetectorCompat
    private lateinit var mainCalculationService: MainCalculationService
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))
        gestureDetectorCompat = GestureDetectorCompat(this, MainCalculatorGestureListener())
        mainCalculationService = MainCalculationService()
        binding.calculatorStackArea.setBackgroundColor(mainCalculationService.settings.backgroundColorValue)
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val newSettings: CalculatorSettings = result.data?.getParcelableExtra("settings")!!
                Log.d(TAG, "New settings: $newSettings")
                mainCalculationService.settings = newSettings
                binding.calculatorStackArea.setBackgroundColor(mainCalculationService.settings.backgroundColorValue)
                drawStackArea()
            }
        }

    }

    fun onNumberButtonClicked(view: View) {
        mainCalculationService.onNumberButtonClicked(view as Button)
        drawStackArea()
    }

    fun onCalculationButtonClicked(view: View) {
        mainCalculationService.onCalculationButtonClicked(view as Button)
        drawStackArea()
    }

    fun onActionButtonClicked(view: View) {
        mainCalculationService.onActionButtonClicked(view as Button)
        drawStackArea()
    }

    @SuppressLint("SetTextI18n")
    fun drawStackArea() {
        if (mainCalculationService.currentState.numberInputString.isBlank()) {
            binding.numberInputTextRow.visibility = GONE
        } else {
            binding.numberInputText.text = mainCalculationService.currentState.numberInputString
            binding.numberInputTextRow.visibility = VISIBLE
        }
        binding.stackCounter.text = "STACK: ${mainCalculationService.currentState.numberStack.size}"
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetectorCompat.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private inner class MainCalculatorGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d(TAG, "onFling: $event1 $event2")
            val diffY: Float = event2.y - event1.y
            val diffX: Float = event2.x - event1.x
            if (abs(diffX) > abs(diffY) && diffX > 0) {
                onSwipeRight()
            }
            return true
        }

        private fun onSwipeRight() {
            Log.d(TAG, "onSwipeRight")
            mainCalculationService.undoCalculatorState()
            drawStackArea()
        }
    }

}