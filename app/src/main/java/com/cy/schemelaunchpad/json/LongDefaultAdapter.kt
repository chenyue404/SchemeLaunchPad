package com.mason.libs.utils.json

import com.google.gson.*
import java.lang.reflect.Type

/**
 * 解决服务器把Int返回String的问题
 */
class LongDefaultAdapter : JsonSerializer<Long>, JsonDeserializer<Long> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Long {
        json?.let {
            return try {
                if (it.asString == "" || json.asString == "null") {
                    return 0L
                }
                json.asLong
            } catch (e: NumberFormatException) {
                return 0
            } catch (e: UnsupportedOperationException) {
                return 0
            }
        }
        return 0
    }

    override fun serialize(
        src: Long?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src);
    }
}