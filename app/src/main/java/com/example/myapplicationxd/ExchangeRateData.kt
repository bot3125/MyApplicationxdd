// ExchangeRateData.kt
package com.example.myapplicationxd

// Clase de datos para almacenar los valores de tipo de cambio.
data class ExchangeRateData(
    val current: Double,
    val min3Months: Double,
    val min1Year: Double,
    val min5Years: Double
)
