package edu.put.calculator.models

import java.math.BigDecimal
import java.util.*

class CalculatorState {

    var numberStack: Stack<BigDecimal>
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

    constructor(numberStack: Stack<BigDecimal>, numberInputString: String) {
        this.numberStack = numberStack
        this.numberInputString = numberInputString
    }


}