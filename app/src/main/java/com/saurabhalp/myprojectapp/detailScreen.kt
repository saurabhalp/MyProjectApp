package com.saurabhalp.myprojectapp

import ChildDocument
import MainViewModel
import ParentDocument
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentListScreen(viewModel: MainViewModel = viewModel()) {
    val parentDocuments by viewModel.parentDocuments.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Subjects") })
        }
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding).padding(16.dp)) {
            Text(text = "Welcome")



            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(parentDocuments.size) { index ->
                    val parent = parentDocuments[index]
                    ParentItem(
                        parent = parent,
                        onParentSelected = { viewModel.fetchChildDocuments(parent.id) })
                }
            }
        }
    }
}

@Composable
fun ParentItem(parent: ParentDocument, onParentSelected: (String) -> Unit ) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {(  onParentSelected(parent.id)) }) {
        Text(text = parent.name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ChildListScreen(viewModel: MainViewModel) {
    val childDocuments by viewModel.childDocuments.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(childDocuments.size) { index ->
            val child = childDocuments[index]
            ChildItem(child = child)
        }
    }
}

@Composable
fun ChildItem(child: ChildDocument) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = child.value, style = MaterialTheme.typography.bodyLarge)
    }
}
