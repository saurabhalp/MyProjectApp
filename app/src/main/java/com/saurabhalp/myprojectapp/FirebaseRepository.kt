package com.saurabhalp.myprojectapp
import ChildDocument
import ParentDocument
import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreRepository {
    @SuppressLint("StaticFieldLeak")
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getParentDocuments(): List<ParentDocument> {
        return try {
            val result = firestore.collection("SubjectCollection")
                .get()
                .await()
            result.documents.map { it.toObject(ParentDocument::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getChildDocuments(parentId: String): List<ChildDocument> {
        return try {
            val result = firestore.collection("parentCollection")
                .document(parentId)
                .collection("childCollection")
                .get()
                .await()
            result.documents.map { it.toObject(ChildDocument::class.java)!! }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
