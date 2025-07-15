package com.tablegroup.designsystem.loader

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun SkeletonLoader() {
    LazyColumn {
        items(7) { index ->
            SkeletonComponent()
        }
    }
}