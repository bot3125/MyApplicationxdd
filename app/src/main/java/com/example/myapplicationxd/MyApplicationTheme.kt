// MyApplicationTheme.kt (opcional si deseas un tema personalizado)
package com.example.myapplicationxd

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
