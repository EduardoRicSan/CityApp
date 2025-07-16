package com.tablegroup.designsystem.loader

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

/**
 * Displays a vertical list of shimmer loading placeholders.
 * Typically used while waiting for a list of data items to load.
 */
@Composable
fun SkeletonLoader() {
    LazyColumn {
        items(7) {
            SkeletonComponent()
        }
    }
}
