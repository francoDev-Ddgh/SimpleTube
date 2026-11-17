package com.simpletube.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simpletube.app.data.VideoItem

@Composable
fun SimpleTubeApp() {
    val vm: MainViewModel = viewModel()
    val state by vm.state.collectAsState()
    var selected by remember { mutableStateOf<VideoItem?>(null) }

    val current = selected
    if (current != null) {
        PlayerScreen(video = current, onBack = { selected = null })
    } else {
        SearchScreen(
            state = state,
            onQueryChange = vm::onQueryChange,
            onSearch = vm::search,
            onVideoClick = { selected = it }
        )
    }
}
