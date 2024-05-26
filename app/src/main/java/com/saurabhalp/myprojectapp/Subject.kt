package com.saurabhalp.myprojectapp

import androidx.annotation.StringRes

data class Subject(
    @StringRes var nameId: Int,
    var id:String
)

data class PdfItem(
    var name: String,
    var url: String
){}
