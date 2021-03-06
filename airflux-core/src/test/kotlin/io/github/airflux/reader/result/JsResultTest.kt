package io.github.airflux.reader.result

import io.github.airflux.common.JsonErrors
import io.github.airflux.common.assertAsFailure
import io.github.airflux.common.assertAsSuccess
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class JsResultTest {

    @Nested
    inner class Success {

        @Test
        fun `Testing 'map' function of the Success class`() {
            val originalValue = "10"
            val original: JsResult<String> = JsResult.Success(path = JsResultPath.Root / "id", value = originalValue)
            val result = original.map { it.toInt() }

            result.assertAsSuccess(path = JsResultPath.Root / "id", value = originalValue.toInt())
        }

        @Test
        fun `Testing 'flatMap' function of the Success class`() {
            val originalValue = "10"
            val original: JsResult<String> = JsResult.Success(path = JsResultPath.Root / "id", value = originalValue)

            val result = original.flatMap { v, p -> JsResult.Success(v.toInt(), p) }

            result.assertAsSuccess(path = JsResultPath.Root / "id", value = originalValue.toInt())
        }

        @Test
        fun `Testing 'orElse' function of the Success class`() {
            val originalValue = "10"
            val elseValue = "20"
            val original: JsResult<String> = JsResult.Success(path = JsResultPath.Root, value = originalValue)

            val result = original.orElse { elseValue }

            assertEquals(originalValue, result)
        }

        @Test
        fun `Testing 'getOrElse' function of the Success class`() {
            val originalValue = "10"
            val elseValue = "20"
            val original: JsResult<String> = JsResult.Success(path = JsResultPath.Root, value = originalValue)

            val result = original.getOrElse(elseValue)

            assertEquals(originalValue, result)
        }

        @Test
        fun `Testing 'onFailure' function of the Success class`() {
            val originalValue = "10"

            val original: JsResult<String> = JsResult.Success(path = JsResultPath.Root, value = originalValue)
            val error: JsResult.Failure? = getErrorOrNull(original)

            assertNull(error)
        }
    }

    @Nested
    inner class Failure {

        @Nested
        inner class Constructors {

            @Test
            fun `Testing the constructor of the Failure class with only error description`() {

                val original = JsResult.Failure(path = JsResultPath.Root, error = JsonErrors.PathMissing)

                original.assertAsFailure(JsResultPath.Root to listOf(JsonErrors.PathMissing))
            }

            @Test
            fun `Testing the constructor of the Failure class with path and one error description`() {
                val original = JsResult.Failure(path = JsResultPath.Root / "user", error = JsonErrors.PathMissing)

                original.assertAsFailure(JsResultPath.Root / "user" to listOf(JsonErrors.PathMissing))
            }

            @Test
            fun `Testing the constructor of the Failure class with path and errors description`() {
                val original = JsResult.Failure(path = JsResultPath.Root / "user", error = JsonErrors.PathMissing)

                original.assertAsFailure(JsResultPath.Root / "user" to listOf(JsonErrors.PathMissing))
            }
        }

        @Test
        fun `Testing 'map' function of the Failure class`() {
            val original: JsResult<String> =
                JsResult.Failure(path = JsResultPath.Root / "name", error = JsonErrors.PathMissing)

            val result = original.map { it.toInt() }

            result.assertAsFailure(JsResultPath.Root / "name" to listOf(JsonErrors.PathMissing))
        }

        @Test
        fun `Testing 'flatMap' function of the Failure class`() {
            val original: JsResult<String> =
                JsResult.Failure(path = JsResultPath.Root / "name", error = JsonErrors.PathMissing)

            val result = original.flatMap { v, p ->
                JsResult.Success(v.toInt(), p)
            }

            result.assertAsFailure(JsResultPath.Root / "name" to listOf(JsonErrors.PathMissing))
        }

        @Test
        fun `Testing 'orElse' function of the Failure class`() {
            val elseValue = "20"
            val original: JsResult<String> = JsResult.Failure(path = JsResultPath.Root, error = JsonErrors.PathMissing)

            val result = original.orElse { elseValue }

            assertEquals(elseValue, result)
        }

        @Test
        fun `Testing 'getOrElse' function of the Failure class`() {
            val elseValue = "20"
            val original: JsResult<String> = JsResult.Failure(path = JsResultPath.Root, error = JsonErrors.PathMissing)

            val result = original.getOrElse(elseValue)

            assertEquals(elseValue, result)
        }

        @Test
        fun `Testing 'onFailure' function of the Failure class`() {
            val original: JsResult<String> = JsResult.Failure(path = JsResultPath.Root, error = JsonErrors.PathMissing)

            val error: JsResult.Failure? = getErrorOrNull(original)

            assertNotNull(error)
        }
    }

    fun <T> getErrorOrNull(result: JsResult<T>): JsResult.Failure? {
        result.onFailure { return it }
        return null
    }
}
