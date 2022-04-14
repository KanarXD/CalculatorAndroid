package edu.put.calculator.services

import android.graphics.Color
import android.util.Log
import android.widget.Button
import edu.put.calculator.models.CalculatorSettings
import edu.put.calculator.models.CalculatorState
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "MainCalculationService"

class MainCalculationService {
    private val lastStates: Stack<CalculatorState> = Stack()
    var currentState: CalculatorState = CalculatorState()
    var settings: CalculatorSettings = CalculatorSettings(5, Color.GREEN)

    fun onNumberButtonClicked(button: Button) {
        val buttonString: String = button.text as String
        Log.d(TAG, "Clicked number button text: $buttonString")
        if (buttonString == ".") {
            Log.d(TAG, "Number button clicked: .")
            if (currentState.numberInputString.contains('.')) {
                Log.d(TAG, "NumberInputString already contains: .")
                return
            }
            currentState.numberInputString += "."
            return
        }
        val number: Int? = buttonString.toIntOrNull()
        if (number != null) {
            Log.d(TAG, "Number button clicked: $number")
            saveCurrentState()
            currentState.numberInputString += number.toString()
            return
        }
    }

    fun onCalculationButtonClicked(button: Button) {
        val operator: String = button.text as String
        Log.d(TAG, "Clicked calculation button text: $operator")
        if (operator == "√") {
            if (currentState.numberStack.size < 1) {
                Log.d(
                    TAG,
                    "Stack has less than 1 number, cannot perform calculation. Stack size: ${currentState.numberStack.size}"
                )
                return
            }
            saveCurrentState()
            val number: Double = currentState.numberStack.pop()
            Log.d(TAG, "Performing calculation: √ $number")
            val result: Double = sqrt(number)
            Log.d(TAG, "Performed calculation: √ $number = $result")
            currentState.numberStack.push(result)
            return
        }
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
    }

    fun onActionButtonClicked(button: Button) {
        val buttonString: String = button.text as String
        Log.d(TAG, "Clicked action button text: $buttonString")
        when (buttonString) {
            "ac" -> {
                Log.d(TAG, "AC button clicked")
                saveCurrentState()
                currentState.numberStack.clear()
                currentState.numberInputString = ""
            }
            "c" -> {
                Log.d(TAG, "C button clicked")
                if (currentState.numberInputString.isNotEmpty()) {
                    saveCurrentState()
                    currentState.numberInputString = currentState.numberInputString
                        .substring(0, currentState.numberInputString.length - 1)
                }
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
            }
            "drop" -> {
                Log.d(TAG, "DROP button clicked")
                currentState.numberInputString = ""
                if (currentState.numberStack.size > 0) {
                    saveCurrentState()
                    val poppedNumber: Double = currentState.numberStack.pop()
                    currentState.numberInputString =
                        DecimalFormat("#." + "#".repeat(settings.numberPrecision))
                            .format(poppedNumber)
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
            "y^x" -> number1.pow(number2)
            else -> {
                throw Exception("Not supported operation: $operation")
            }
        }
        return result
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

}