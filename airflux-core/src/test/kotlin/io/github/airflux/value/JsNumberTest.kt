package io.github.airflux.value

import io.github.airflux.common.ObjectContract
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsNumberTest {

    @Test
    fun `Testing 'valueOf' function of the JsNumber class for Byte type`() {
        val min: Byte = Byte.MIN_VALUE
        val max: Byte = Byte.MAX_VALUE

        assertEquals(min, JsNumber.valueOf(min).underlying.toByte())
        assertEquals(max, JsNumber.valueOf(max).underlying.toByte())
    }

    @Test
    fun `Testing 'valueOf' function of the JsNumber class for Short type`() {
        val min: Short = Short.MIN_VALUE
        val max: Short = Short.MAX_VALUE

        assertEquals(min, JsNumber.valueOf(min).underlying.toShort())
        assertEquals(max, JsNumber.valueOf(max).underlying.toShort())
    }

    @Test
    fun `Testing 'valueOf' function of the JsNumber class for Int type`() {
        val min: Int = Int.MIN_VALUE
        val max: Int = Int.MAX_VALUE

        assertEquals(min, JsNumber.valueOf(min).underlying.toInt())
        assertEquals(max, JsNumber.valueOf(max).underlying.toInt())
    }

    @Test
    fun `Testing 'valueOf' function of the JsNumber class for Long type`() {
        val min: Long = Long.MIN_VALUE
        val max: Long = Long.MAX_VALUE

        assertEquals(min, JsNumber.valueOf(min).underlying.toLong())
        assertEquals(max, JsNumber.valueOf(max).underlying.toLong())
    }

    @TestFactory
    fun `Testing 'valueOf' function of the JsNumber class for String type`() = listOf(
        Triple("false", "false", null),
        Triple(".0", ".0", null),
        Triple("+.0", "+.0", null),
        Triple("-.0", "-.0", null),
        Triple("1", "1", "1"),
        Triple("-1", "-1", "-1"),
        Triple("1.5", "1.5", "1.5"),
        Triple("-1.5", "-1.5", "-1.5"),
        Triple("1.50", "1.50", "1.50"),
        Triple("-1.50", "-1.50", "-1.50")
    ).map { (displayName: String, text: String, expected: String?) ->
        DynamicTest.dynamicTest(displayName) {
            assertEquals(expected, JsNumber.valueOf(text)?.underlying)
        }
    }

    @TestFactory
    fun `Testing the internal representation as an integer number`() = listOf(
        "0" to "0", "-0" to "-0",
        "1" to "1", "-1" to "-1",
        "100" to "100", "-100" to "-100"
    ).map { (displayName: String, text: String) ->
        DynamicTest.dynamicTest(displayName) {
            val number = JsNumber.valueOf(text)!!
            assertTrue(number.isInteger)
            assertFalse(number.isReal)
        }
    }

    @TestFactory
    fun `Testing the internal representation as a real number`() = listOf(
        "0.0" to "0.0", "-0.0" to "-0.0",
        "0.1" to "0.1", "-0.1" to "-0.1",
        "1.5" to "1.5", "-1.5" to "-1.5",
        "1.50" to "1.50", "-1.50" to "-1.50"
    ).map { (displayName: String, text: String) ->
        DynamicTest.dynamicTest(displayName) {
            val number = JsNumber.valueOf(text)!!
            assertFalse(number.isInteger)
            assertTrue(number.isReal)
        }
    }

    @TestFactory
    fun `Testing 'toString' function of the JsNumber class`() = listOf(
        "1" to "1", "-1" to "-1",
        "1.5" to "1.5", "-1.5" to "-1.5",
        "1.50" to "1.50", "-1.50" to "-1.50"
    ).map { (displayName: String, text: String) ->
        DynamicTest.dynamicTest(displayName) {

            ObjectContract.checkToString(JsNumber.valueOf(text)!!.toString(), text)
        }
    }

    @Test
    fun `Testing 'equals contract' of the JsNumber class`() {
        ObjectContract.checkEqualsContract(
            JsNumber.valueOf(10),
            JsNumber.valueOf(10),
            JsNumber.valueOf(100)
        )
    }
}
