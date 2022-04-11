package edu.put.calculator.models

import android.graphics.Color

class CalculatorSettings {
    val numberPrecision: Int
    val backgroundColor: Color

    constructor(numberPrecision: Int, backgroundColor: Color) {
        this.numberPrecision = numberPrecision
        this.backgroundColor = backgroundColor
    }
}