package edu.put.calculator.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import edu.put.calculator.R
import edu.put.calculator.models.CalculatorState
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val lastStates: Stack<CalculatorState> = Stack()
    private var currentState: CalculatorState = CalculatorState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            val number2: BigDecimal = currentState.numberStack.pop()
            val number1: BigDecimal = currentState.numberStack.pop()
            Log.d(TAG, "Performing calculation: $number1 $operator $number2")
            val result: BigDecimal = calculateOperation(operator, number1, number2)
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
                val numberInput: BigDecimal? = currentState.numberInputString.toBigDecimalOrNull()
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
            "drop" -> if (currentState.numberStack.size > 0) {
                Log.d(TAG, "DROP button clicked")
                saveCurrentState()
                val poppedNumber: Number = currentState.numberStack.pop()
                currentState.numberInputString = poppedNumber.toString()
                drawStackArea()
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
        number1: BigDecimal,
        number2: BigDecimal
    ): BigDecimal {
        return when (operation) {
            "+" -> number1.add(number2)
            "-" -> number1.minus(number2)
            "*" -> number1.multiply(number2)
            "/" -> number1.div(number2)
            else -> {
                throw Exception("Not supported operation: $operation")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawStackArea() {
        stack_counter.text = "STACK: ${currentState.numberStack.size}"
        numberInputText.text = currentState.numberInputString
        stack_level_1.text =
            if (currentState.numberStack.size > 0) currentState.numberStack[currentState.numberStack.size - 1].toString() else ""
        stack_level_2.text =
            if (currentState.numberStack.size > 1) currentState.numberStack[currentState.numberStack.size - 2].toString() else ""
        stack_level_3.text =
            if (currentState.numberStack.size > 2) currentState.numberStack[currentState.numberStack.size - 3].toString() else ""
        stack_level_4.text =
            if (currentState.numberStack.size > 3) currentState.numberStack[currentState.numberStack.size - 4].toString() else ""
    }

}