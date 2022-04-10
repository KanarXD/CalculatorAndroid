package edu.put.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var numberStack: Stack<Number> = Stack()
    private var numberInputString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButtonClicked(view: View) {
        if (view !is Button) {
            return
        }
        val button: Button = view
        val buttonString: String = button.text as String
        val number: Int? = buttonString.toIntOrNull()

        if (number != null) {
            println(number)
            numberInputString += number.toString()
            numberInputText.text = numberInputString
            return
        }

        if (buttonString.matches("/+|/-|/*|//".toRegex())) {
            println("math operator")
            return
        }

        when (buttonString) {
            "ac" -> {
                numberStack.clear()
                numberInputString = ""
                drawStackArea()
            }
            "enter" -> {
                val numberInput: Number? = numberInputString.toBigDecimalOrNull()
                if (numberInput != null) {
                    numberInputString = ""
                    numberStack.push(numberInput)
                    drawStackArea()
                }
            }
            "drop" -> if (numberStack.size > 0) {
                val poppedNumber: Number = numberStack.pop()
                numberInputString = poppedNumber.toString()
                drawStackArea()
            }

            else -> {

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawStackArea() {
        stack_counter.text = "STACK: ${numberStack.size}"
        numberInputText.text = numberInputString
        stack_level_1.text =
            if (numberStack.size > 0) numberStack[numberStack.size - 1].toString() else ""
        stack_level_2.text =
            if (numberStack.size > 1) numberStack[numberStack.size - 2].toString() else ""
        stack_level_3.text =
            if (numberStack.size > 2) numberStack[numberStack.size - 3].toString() else ""
        stack_level_4.text =
            if (numberStack.size > 3) numberStack[numberStack.size - 4].toString() else ""
    }

}