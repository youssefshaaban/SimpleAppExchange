package com.paypay.taskapp.data.util


import org.junit.Assert
import org.junit.Test

class UtilKtTest {

    @Test
    fun `test dataStale when date when date is More than current then data is stale`() {
        Assert.assertTrue(isDateStale(System.currentTimeMillis() - 86400000))
    }
    @Test
    fun `test dataStale when date when date is More than current then data not stale`() {
        Assert.assertFalse(isDateStale(System.currentTimeMillis() + 86400000))
    }
}