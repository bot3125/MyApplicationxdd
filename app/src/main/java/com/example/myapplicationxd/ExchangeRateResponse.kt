// ExchangeRateResponse.kt
package com.example.myapplicationxd

// Mapea la respuesta de la API para el tipo de cambio.
data class ExchangeRateResponse(
    val rates: Map<String, Double>,  // Mapa de las tasas de cambio
    val base: String,                // La moneda base (por ejemplo, "USD")
    val date: String                 // Fecha de la tasa de cambio
)
