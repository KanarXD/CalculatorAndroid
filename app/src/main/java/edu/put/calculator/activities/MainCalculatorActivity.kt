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
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.models.CalculatorState
import kotlinx.android.synthetic.main.activity_calculator_main.*
import java.util.*

class MainCalculatorActivity : AppCompatActivity() {
    @Suppress("PrivatePropertyName")
    private val TAG = "MainCalculatorActivity"
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private val lastStates: Stack<CalculatorState> = Stack()
    private var currentState: CalculatorState = CalculatorState()
    private var settings: CalculatorSettings = CalculatorSettings(5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator_main)
        setSupportActionBar(findViewById(R.id.main_activity_toolbar))
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                println(intent)

                val newSettings = intent?.getParcelableExtra<CalculatorSettings>("settings")
                println(newSettings)
            }
        }
    }

    fun onButtonClicked(view: View) {
        if (view !is Button) {
            Log.d(TAG, "onButtonClicked view is not Button")
            return
        }
        val button: Button = view
        val buttonString: String = button.text as String

        Log.d(TAG, "Clicked button text: $buttonString")

        if (buttonString.matches("^[-+*/]$".toRegex())) {
            val operator: String = buttonString
            Log.d(TAG, "Math operator button clicked: $operator")
            if (currentState.numberStack.size < 2) {
                Log.d(
                    TAG,
                    "Stack has less than 2 numbers, cannot perform calculation. Stack size: ${currentState.numberStack.size}"
                )
                return
            }
            saveCurrentState()
            val number2: Double = currentState.numberStack.pop()
            val number1: Double = currentState.numberStack.pop()
            Log.d(TAG, "Performing calculation: $number1 $operator $number2")
            val result: Double = calculateOperation(operator, number1, number2)
            Log.d(TAG, "Performed calculation: $number1 $operator $number2 = $result")
            currentState.numberStack.push(result)
            drawStackArea()
            return
        }

        val number: Int? = buttonString.toIntOrNull()
        if (number != null) {
            Log.d(TAG, "Number button clicked: $number")
            saveCurrentState()
            currentState.numberInputString += number.toString()
            drawStackArea()
            return
        }

        when (buttonString) {
            "ac" -> {
                Log.d(TAG, "AC button clicked")
                saveCurrentState()
                currentState.numberStack.clear()
                currentState.numberInputString = ""
                drawStackArea()
            }
            "enter" -> {
                Log.d(TAG, "ENTER button clicked")
                val numberInput: Double? = currentState.numberInputString.toDoubleOrNull()
                if (numberInput == null) {
                    Log.d(TAG, "Empty input, cannot push on stack")
                    return
                }
                Log.d(TAG, "Pushing on stack: $numberInput")
                saveCurrentState()
                currentState.numberInputString = ""
                currentState.numberStack.push(numberInput)
                drawStackArea()
            }
            "drop" -> {
                Log.d(TAG, "DROP button clicked")
                if (currentState.numberStack.size > 0) {
                    saveCurrentState()
                    val poppedNumber: Number = currentState.numberStack.pop()
                    currentState.numberInputString = poppedNumber.toString()
                    drawStackArea()
                }
            }
            else -> {

            }
        }
    }

    fun undoCalculatorState(@Suppress("UNUSED_PARAMETER") view: View) {
        if (lastStates.size <= 0) {
            Log.d(TAG, "Cannot undo (no last states)")
            return
        }
        Log.d(TAG, "Performing undo")
        currentState = lastStates.pop()
        drawStackArea()
    }

    private fun saveCurrentState() {
        lastStates.push(CalculatorState(currentState))
    }

    private fun calculateOperation(
        operation: String,
        number1: Double,
        number2: Double
    ): Double {
        val result: Double = when (operation) {
            "+" -> number1 + number2
            "-" -> number1 - number2
            "*" -> number1 * number2
            "/" -> number1 / number2
            else -> {
                throw Exception("Not supported operation: $operation")
            }
        }
        return result
    }

    @SuppressLint("SetTextI18n")
    private fun drawStackArea() {
        stack_counter.text = "STACK: ${currentState.numberStack.size}"
        numberInputText.text = currentState.numberInputString
        stack_level_1.text = prepareStackValue(1)
        stack_level_2.text = prepareStackValue(2)
        stack_level_3.text = prepareStackValue(3)
        stack_level_4.text = prepareStackValue(4)
    }

    private fun prepareStackValue(stackLevel: Int): String {
        if (currentState.numberStack.size > stackLevel - 1) {
            val number: Double =
                currentState.numberStack[currentState.numberStack.size - stackLevel]
            return String.format("%.${settings.numberPrecision}f", number)
        }
        return ""
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSettingsButtonClicked(view: View) {
        val intent = Intent(this, CalculatorSettingsActivity::class.java)
        intent.putExtra("settings", settings)
        activityResultLauncher.launch(intent)
    }
}