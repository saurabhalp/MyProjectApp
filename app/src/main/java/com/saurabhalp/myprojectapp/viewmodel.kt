import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saurabhalp.myprojectapp.FirestoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
data class ParentDocument(
    val id: String = "",
    val name: String = ""
)

data class ChildDocument(
    val id: String = "",
    val value: String = ""
)
class MainViewModel : ViewModel() {

    private val _parentDocuments = MutableStateFlow<List<ParentDocument>>(emptyList())
    val parentDocuments: StateFlow<List<ParentDocument>> = _parentDocuments

    private val _childDocuments = MutableStateFlow<List<ChildDocument>>(emptyList())
    val childDocuments: StateFlow<List<ChildDocument>> = _childDocuments

    init {
        fetchParentDocuments()
    }

    private fun fetchParentDocuments() {
        viewModelScope.launch {
            val parents = FirestoreRepository.getParentDocuments()
            _parentDocuments.value = parents
        }
    }

    fun fetchChildDocuments(parentId: String) {
        viewModelScope.launch {
            val children = FirestoreRepository.getChildDocuments(parentId)
            _childDocuments.value = children
        }
    }
}
