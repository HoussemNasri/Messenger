package com.nasri.messenger.data.gson

import android.net.Uri
import com.google.gson.*
import java.lang.reflect.Type


class UriTypeAdapter : JsonSerializer<Uri?>, JsonDeserializer<Uri?> {
    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        src: JsonElement, srcType: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(src.asString)
    }
}
