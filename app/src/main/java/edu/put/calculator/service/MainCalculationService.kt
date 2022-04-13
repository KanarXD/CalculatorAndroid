package edu.put.calculator.service

import android.util.Log
import android.view.View
import android.widget.Button
import edu.put.calculator.activities.MainCalculatorActivity
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.models.CalculatorState
import java.text.DecimalFormat
import java.util.*

class MainCalculationService(private val mainCalculatorActivity: MainCalculatorActivity) {
    @Suppress("PrivatePropertyName")
    private val TAG = "MainCalculationService"
    private val lastStates: Stack<CalculatorState> = Stack()
    var currentState: CalculatorState = CalculatorState()
    var settings: CalculatorSettings = CalculatorSettings(5)

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
            mainCalculatorActivity.drawStackArea()
            return
        }

        val number: Int? = buttonString.toIntOrNull()
        if (number != null) {
            Log.d(TAG, "Number button clicked: $number")
            saveCurrentState()
            currentState.numberInputString += number.toString()
            mainCalculatorActivity.drawStackArea()
            return
        }

        when (buttonString) {
            "ac" -> {
                Log.d(TAG, "AC button clicked")
                saveCurrentState()
                currentState.numberStack.clear()
                currentState.numberInputString = ""
                mainCalculatorActivity.drawStackArea()
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
                mainCalculatorActivity.drawStackArea()
            }
            "drop" -> {
                Log.d(TAG, "DROP button clicked")
                if (currentState.numberStack.size > 0) {
                    saveCurrentState()
                    val poppedNumber: Number = currentState.numberStack.pop()
                    currentState.numberInputString = poppedNumber.toString()
                    mainCalculatorActivity.drawStackArea()
                }
            }
            else -> {

            }
        }
    }

    fun undoCalculatorState() {
        if (lastStates.size <= 0) {
            Log.d(TAG, "Cannot undo (no last states)")
            return
        }
        Log.d(TAG, "Performing undo")
        currentState = lastStates.pop()
        mainCalculatorActivity.drawStackArea()
    }

    fun prepareStackValue(stackLevel: Int): String {
        if (currentState.numberStack.size > stackLevel - 1) {
            val number: Double =
                currentState.numberStack[currentState.numberStack.size - stackLevel]
            return DecimalFormat("#." + "#".repeat(settings.numberPrecision)).format(number)
        }
        return ""
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

}