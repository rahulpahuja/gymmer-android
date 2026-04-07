package com.m1x.gymmer.data.utils

import java.text.NumberFormat
import java.util.*

/**
 * Manager to handle multi-currency support and formatting.
 */
object CurrencyManager {

    private var currentLocale: Locale = Locale("en", "IN")
    private var currentCurrency: Currency = Currency.getInstance("INR")

    /**
     * Set the current currency/locale for the app.
     */
    fun setCurrency(locale: Locale) {
        currentLocale = locale
        currentCurrency = Currency.getInstance(locale)
    }

    /**
     * Formats an amount based on the current currency and locale.
     */
    fun formatAmount(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(currentLocale)
        format.currency = currentCurrency
        return format.format(amount)
    }

    /**
     * Converts an amount from one currency to another (Mock implementation).
     * In a real app, this would fetch latest rates from an API.
     */
    fun convertAmount(amount: Double, from: Currency, to: Currency): Double {
        // Mock rates
        val rateMap = mapOf(
            "USD" to 1.0,
            "EUR" to 0.92,
            "GBP" to 0.79,
            "INR" to 83.0,
            "JPY" to 150.0
        )
        
        val fromRate = rateMap[from.currencyCode] ?: 1.0
        val toRate = rateMap[to.currencyCode] ?: 1.0
        
        return (amount / fromRate) * toRate
    }

    fun getCurrentCurrencyCode(): String = currentCurrency.currencyCode
}
