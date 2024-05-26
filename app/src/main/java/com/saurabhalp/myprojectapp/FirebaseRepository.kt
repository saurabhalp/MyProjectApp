package com.saurabhalp.myprojectapp

import android.annotation.SuppressLint
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.saurabhalp.myprojectapp.PdfItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await


class ItemRepository { private var db = Firebase.firestore
 @SuppressLint("SuspiciousIndentation")
 fun fetchItems(id:String): List<PdfItem> {
        // Simulate network delay
    val items = mutableListOf<PdfItem>()
        items.add(PdfItem("ppppp", "ppppp"))
        try {
            val documents = db.collection("pdfs").document("fdsa").collection("pdf1").get()

//            for (document in documents) {
//                val name = document.getString("name") ?: "Not Found"
//                val url = document.getString("url") ?: "Url Not Accessible"
//                items.add(PdfItem(name, url))
//
//            }

        }catch (_:Exception){

        }



        return items
    }


    }

