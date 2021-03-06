package io.github.airflux.lookup.extension

import io.github.airflux.lookup.JsLookup
import io.github.airflux.value.JsArray
import io.github.airflux.value.JsObject
import io.github.airflux.value.JsValue

@Suppress("unused")

operator fun JsLookup.div(name: String): JsLookup = when (this) {
    is JsLookup.Defined -> lookup(byName = name)
    is JsLookup.Undefined -> this
}

operator fun JsLookup.div(idx: Int): JsLookup = when (this) {
    is JsLookup.Defined -> lookup(byIndex = idx)
    is JsLookup.Undefined -> this
}

fun JsLookup.Defined.lookup(byName: String): JsLookup = when (value) {
    is JsObject -> value[byName]
        ?.let { JsLookup.Defined(path = path / byName, value = it) }
        ?: JsLookup.Undefined.PathMissing(path = path / byName)

    else -> JsLookup.Undefined.InvalidType(path = path, expected = JsValue.Type.OBJECT, actual = value.type)
}

fun JsLookup.Defined.lookup(byIndex: Int): JsLookup = when (value) {
    is JsArray<*> -> value[byIndex]
        ?.let { JsLookup.Defined(path = path / byIndex, value = it) }
        ?: JsLookup.Undefined.PathMissing(path = path / byIndex)

    else -> JsLookup.Undefined.InvalidType(path = path, expected = JsValue.Type.ARRAY, actual = value.type)
}
