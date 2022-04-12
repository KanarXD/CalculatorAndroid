package edu.put.calculator.models

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class CalculatorSettings(val numberPrecision: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt()
    ) {
    }

    override fun toString(): String {
        return "CalculatorSettings(numberPrecision=$numberPrecision)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(numberPrecision)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<CalculatorSettings> {
        override fun createFromParcel(parcel: Parcel): CalculatorSettings {
            return CalculatorSettings(parcel)
        }

        override fun newArray(size: Int): Array<CalculatorSettings?> {
            return arrayOfNulls(size)
        }
    }

}