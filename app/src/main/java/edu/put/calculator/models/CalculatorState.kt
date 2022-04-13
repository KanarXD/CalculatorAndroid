package edu.put.calculator.models

import java.util.*

class CalculatorState {

    var numberStack: Stack<Double>
    var numberInputString: String


    constructor() {
        this.numberStack = Stack()
        this.numberInputString = ""
    }

    constructor(calculatorState: CalculatorState) {
        this.numberInputString = calculatorState.numberInputString
        this.numberStack = Stack()
        calculatorState.numberStack.stream().forEach { number -> this.numberStack.push(number) }
    }

    constructor(numberStack: Stack<Double>, numberInputString: String) {
        this.numberStack = numberStack
        this.numberInputString = numberInputString
    }

    override fun toString(): String {
        return "CalculatorState(numberStack=$numberStack, numberInputString='$numberInputString')"
    }


}