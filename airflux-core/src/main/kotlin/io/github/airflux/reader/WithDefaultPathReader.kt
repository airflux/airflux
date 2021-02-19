package io.github.airflux.reader

import io.github.airflux.lookup.JsLookup
import io.github.airflux.path.JsPath
import io.github.airflux.reader.result.JsError
import io.github.airflux.reader.result.JsResult
import io.github.airflux.value.JsNull
import io.github.airflux.value.JsValue
import io.github.airflux.value.extension.lookup

/**
 * Reads required field at [JsPath] or return default if a field is not found.
 */
@Suppress("unused")
interface WithDefaultPathReader {

    companion object {

        fun <T : Any> withDefault(
            from: JsLookup,
            using: JsReader<T>,
            defaultValue: () -> T,
            errorInvalidType: (expected: JsValue.Type, actual: JsValue.Type) -> JsError
        ): JsResult<T> =
            when (from) {
                is JsLookup.Defined -> when (from.value) {
                    is JsNull -> JsResult.Success(path = from.path, value = defaultValue())
                    else -> using.read(from.value).repath(from.path)
                }

                is JsLookup.Undefined.PathMissing -> JsResult.Success(path = from.path, value = defaultValue())

                is JsLookup.Undefined.InvalidType ->
                    JsResult.Failure(path = from.path, error = errorInvalidType(from.expected, from.actual))
            }

        /**
         * Reads required field at [JsPath] or return default if a field is not found.
         *
         * - If any node in [JsPath] is not found then returns [defaultValue]
         * - If the last node in [JsPath] is found with value 'null' then returns [defaultValue]
         * - If any node does not match path element type, then returning error [errorInvalidType]
         * - If the entire path is found then applies [reader]
         */
        fun <T : Any> withDefault(
            from: JsValue,
            path: JsPath,
            using: JsReader<T>,
            defaultValue: () -> T,
            errorInvalidType: (expected: JsValue.Type, actual: JsValue.Type) -> JsError
        ): JsResult<T> =
            withDefault(
                from = from.lookup(path),
                using = using,
                defaultValue = defaultValue,
                errorInvalidType = errorInvalidType
            )
    }
}
