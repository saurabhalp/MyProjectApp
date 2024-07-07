package com.saurabhalp.myprojectapp

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(title: String){
    TopAppBar(title = { Text(title, fontWeight = FontWeight.Bold) }, colors =
    TopAppBarColors(
        Color(0xffe3f1fb),
        Color(0xfff1f9fe),
        Color.Blue,
        Color(0xff0d2c3f),
        Color(0xfff1f9fe)
    ),
        navigationIcon = {
            Box(modifier = Modifier) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher),
                    contentDescription = "icon",
                    Modifier
                        .padding(1.dp)
                        .height(100.dp)
                )
                Modifier
                    .align(Alignment.TopStart)
            }
        }
    )
}

@Composable
@Preview
fun pre(){
    Topbar(title = "Hello")
}
