package com.m1x.gymmer.data.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class CurrencyManagerTest {

    @Test
    fun `formatAmount should format correctly for default INR locale`() {
        val amount = 1234.56
        val formatted = CurrencyManager.formatAmount(amount)
        // Note: The exact format might depend on the JVM's locale implementation, 
        // but it should contain the Rupee symbol or code and the numbers.
        // For English (India) it's usually ₹ 1,234.56 or similar.
        assert(formatted.contains("₹"))
        assert(formatted.contains("1,234.56"))
    }

    @Test
    fun `setCurrency should update current currency and locale`() {
        val usLocale = Locale.US
        CurrencyManager.setCurrency(usLocale)
        
        val amount = 1234.56
        val formatted = CurrencyManager.formatAmount(amount)
        
        assert(formatted.contains("$"))
        assert(formatted.contains("1,234.56"))
        assertEquals("USD", CurrencyManager.getCurrentCurrencyCode())
        
        // Reset to INR for other tests
        CurrencyManager.setCurrency(Locale("en", "IN"))
    }

    @Test
    fun `convertAmount should return correct converted value`() {
        val amount = 100.0
        val fromCurrency = Currency.getInstance("USD")
        val toCurrency = Currency.getInstance("INR")
        
        val converted = CurrencyManager.convertAmount(amount, fromCurrency, toCurrency)
        
        // 100 USD * 83.0 = 8300 INR (based on mock rates in CurrencyManager)
        assertEquals(8300.0, converted, 0.001)
    }

    @Test
    fun `getCurrentCurrencyCode should return INR by default`() {
        // Since we set it to INR in the object's init (via property initialization)
        assertEquals("INR", CurrencyManager.getCurrentCurrencyCode())
    }
}
