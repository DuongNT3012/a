package com.ntdapp.document.viewer.reader.officereader.pdfreader.language_nav

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LanguageModelNav(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null
) : Parcelable {
    constructor() : this("", "", false, 0) {}
}

