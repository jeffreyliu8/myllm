package com.jeffreyliu.myllm.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.jeffreyliu.myllm.Model


@PreviewScreenSizes
@Composable
internal fun StartGameScreen(
    onStartGameClick: (Model) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        JellyfishAnimation()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(Model.entries) { model ->
                Button(
                    onClick = {
                        onStartGameClick(model)
                    },
                ) {
                    Text(model.toString())
                }
            }
        }
    }
}
