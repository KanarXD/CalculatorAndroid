package edu.put.calculator.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CalculatorSettings(val numberPrecision: Int, val backgroundColorValue: Int) : Parcelable {

    override fun toString(): String {
        return "CalculatorSettings(numberPrecision=$numberPrecision, backgroundColorValue=$backgroundColorValue)"
    }

}