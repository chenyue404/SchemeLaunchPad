package com.mason.libs.utils.json

import com.google.gson.*
import java.lang.reflect.Type

/**
 * 解决服务器把Int返回String的问题
 */
class IntegerDefaultAdapter : JsonSerializer<Int>, JsonDeserializer<Int> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Int {
        json?.let {
            return try {
                if (it.asString == "" || json.asString == "null") {
                    return 0
                }
                json.asInt
            } catch (e: NumberFormatException) {
                return 0
            } catch (e: UnsupportedOperationException) {
                return 0
            }
        }
        return 0
    }

    override fun serialize(
        src: Int?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src);
    }
}