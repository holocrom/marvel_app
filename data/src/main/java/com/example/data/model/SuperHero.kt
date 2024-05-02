package com.example.data.model

import android.os.Parcel
import android.os.Parcelable

data class SuperHero(
    val id : Int,
    val name: String?,
    var description: String?,
    val imageURL: String?
    ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(imageURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SuperHero> {
        override fun createFromParcel(parcel: Parcel): SuperHero {
            return SuperHero(parcel)
        }

        override fun newArray(size: Int): Array<SuperHero?> {
            return arrayOfNulls(size)
        }
    }
}

