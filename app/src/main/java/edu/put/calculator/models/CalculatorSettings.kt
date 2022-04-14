package edu.put.calculator.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CalculatorSettings(val numberPrecision: Int) : Parcelable {

//    constructor(parcel: Parcel) : this(
//        parcel.readInt()
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(numberPrecision)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Creator<CalculatorSettings> {
//        override fun createFromParcel(parcel: Parcel): CalculatorSettings {
//            return CalculatorSettings(parcel)
//        }
//
//        override fun newArray(size: Int): Array<CalculatorSettings?> {
//            return arrayOfNulls(size)
//        }
//
//    }

    override fun toString(): String {
        return "CalculatorSettings(numberPrecision=$numberPrecision)"
    }

}