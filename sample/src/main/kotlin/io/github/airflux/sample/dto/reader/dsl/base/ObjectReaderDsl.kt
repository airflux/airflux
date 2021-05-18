package io.github.airflux.sample.dto.reader.dsl.base

import io.github.airflux.dsl.reader.`object`.ObjectReader
import io.github.airflux.dsl.reader.`object`.ObjectReaderConfiguration
import io.github.airflux.dsl.reader.`object`.ObjectValidators
import io.github.airflux.dsl.reader.`object`.ObjectValuesMap
import io.github.airflux.reader.result.JsResult
import io.github.airflux.sample.dto.reader.base.ErrorBuilder
import io.github.airflux.sample.json.validation.`object`.isNotEmpty

private val DefaultObjectReaderConfig = ObjectReaderConfiguration.Builder()
    .apply {
        failFast = true
    }
    .build()

private val DefaultObjectValidatorBuilders = ObjectValidators.Builder()
    .apply {
        isNotEmpty = true
    }

val reader = ObjectReader(
    initialConfiguration = DefaultObjectReaderConfig,
    initialValidatorBuilders = DefaultObjectValidatorBuilders,
    pathMissingErrorBuilder = ErrorBuilder.PathMissing,
    invalidTypeErrorBuilder = ErrorBuilder.InvalidType
)

inline fun <T> simpleBuilder(crossinline builder: (values: ObjectValuesMap) -> T): (ObjectValuesMap) -> JsResult<T> =
    { JsResult.Success(builder(it)) }