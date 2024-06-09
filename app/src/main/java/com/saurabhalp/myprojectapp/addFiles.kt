import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

@Composable
fun UploadPdfScreen() {
    var filePath by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }
    var url by remember {
        mutableStateOf("")
    }
    var loading by remember {
        mutableStateOf(false)
    }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        filePath = uri
    }
    Column (modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (loading) {
            CircularProgressIndicator()
        } else {


            OutlinedTextField(value = name,
                onValueChange = { name = it },
                label ={ Text(text = "Title")})

            Spacer(modifier = Modifier.height(16.dp))
            var filechoosed by remember {
                mutableStateOf(false) }
            if(!filechoosed) {
                Button(onClick = { pdfPickerLauncher.launch("application/pdf")
                    if(filePath!=null)
                      filechoosed = true
                }) {
                    Text(text = "Choose PDF")
                }
            }else{
                filePath?.lastPathSegment?.let { Text(text = it) }
            }



            Spacer(
                modifier =
                Modifier.height(16.dp)
            )

            Button(onClick = {
                loading= true
                filePath?.let {
                    uploadPdf(it) { Arli ->

                        url = Arli
                        loading = false
                        addFilesViewModel(Files(name, url, "5"), context = context)
                    }


                }






            }) {
                Text(text = "Upload PDF")
            }
        }
    }
}

private fun uploadPdf(filePath: Uri, onUrlReady: (String) -> Unit){
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.reference

    val ref = storageReference.child("pdfs/" + UUID.randomUUID().toString())
    ref.putFile(filePath)
        .addOnSuccessListener { taskSnapshot ->
            ref.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Log.d("Firebase", "Download URL: $downloadUrl")
                onUrlReady(downloadUrl)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firebase", "Failed to upload PDF", exception)
        }
}
@SuppressLint("SuspiciousIndentation")
fun addFilesViewModel(data : Files,context: Context) {
    val db = Firebase.firestore
    val notes = hashMapOf(
        "name" to data.title,
        "url" to data.url
    )
        db.collection(data.subject).document("fdsa").collection("pdf1").add(notes)
            .addOnSuccessListener {
                Log.d("Uppload","uploaded with id ${it.id}")
                Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("upload","error in uploading",e)
            }

}


data class Files(var title:String,var url : String,var subject:String)

@Preview(showBackground = true)
@Composable
fun UploadPdfScreenPreview() {
    UploadPdfScreen()
}
