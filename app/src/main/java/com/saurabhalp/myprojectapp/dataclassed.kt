package com.saurabhalp.myprojectapp

import androidx.annotation.StringRes

data class Subject(
    @StringRes var nameId: Int,
    var id:String
)

data class PdfItem(
    var name: String,
    var url: String
)
data class NotesPdf(
    var name: String,
    var url : String
)
data class Update(
    val title: String,
    var message: String,
    var date : String

)
