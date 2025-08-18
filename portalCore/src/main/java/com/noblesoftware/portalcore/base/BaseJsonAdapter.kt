package com.noblesoftware.portalcore.base

import com.google.gson.TypeAdapter
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken

/**
 * Created by Hafizh Anbiya on 06 September 2023
 * https://github.com/Fizhu
 */
abstract class BaseJsonAdapter<T> : TypeAdapter<T>() {

    fun readObject(reader: JsonReader): Any? {
        return when (reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                val list: MutableList<Any?> = java.util.ArrayList()
                reader.beginArray()
                while (reader.hasNext()) {
                    list.add(readObject(reader))
                }
                reader.endArray()
                list
            }

            JsonToken.BEGIN_OBJECT -> {
                val map: MutableMap<String, Any?> = LinkedTreeMap()
                reader.beginObject()
                while (reader.hasNext()) {
                    map[reader.nextName()] = readObject(reader)
                }
                reader.endObject()
                map
            }

            JsonToken.STRING -> reader.nextString()
            JsonToken.NUMBER -> reader.nextDouble()
            JsonToken.BOOLEAN -> reader.nextBoolean()
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }

            else -> throw IllegalStateException()
        }
    }
}