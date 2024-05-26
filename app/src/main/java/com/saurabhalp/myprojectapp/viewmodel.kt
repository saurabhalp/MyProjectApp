package com.saurabhalp.myprojectapp
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    var isLoggedIn  = _isLoggedIn.value
    private var _items = MutableStateFlow<List<PdfItem>>(emptyList())
    lateinit var auth: FirebaseAuth
    var tems: StateFlow<List<PdfItem>> = _items
    private var _clicked :String? = null
    var clicked = _clicked
    fun login() {
            viewModelScope.launch {
                // Simulate login process
                _isLoggedIn.value = true
            }
        }

        fun logout() {
            viewModelScope.launch {
                _isLoggedIn.value = false
            }
        }
        fun getItems(id:String){
             _items.value = ItemRepository().fetchItems(id)
            _clicked = id;
        }
}

