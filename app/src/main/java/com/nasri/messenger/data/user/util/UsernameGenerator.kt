package com.nasri.messenger.data.user.util

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object UsernameGenerator {
    private const val REQUEST_BODY =
        "{ \"snr\": { \"UserName\": \"\", \"Hobbies\": \"\", \"ThingsILike\": \"\", \"Numbers\": \"\", \"WhatAreYouLike\": \"\", \"Words\": \"\", \"Stub\": \"reddit\", \"LanguageCode\": \"en\", \"NamesLanguageID\": \"45\", \"Rhyming\": false, \"OneWord\": false, \"UseExactWords\": false, \"ScreenNameStyleString\": \"Any\", \"GenderAny\": false, \"GenderMale\": false, \"GenderFemale\": false } }"
    private const val API_URL = "https://www.spinxo.com/services/NameService.asmx/GetNames"

    //TODO('Pass more information to the generate function like gender, hobbies, language... to get accurate results')
    suspend fun generate(): String = withContext(Dispatchers.IO) {
        val response = post(API_URL)
        if (response.names.isNotEmpty() ?: false) {
            response.names[Random().nextInt(response.names.size)]
        } else {
            "Jhon Doe"
        }
    }

    private fun post(url: String) = Fuel.post(url)
        .jsonBody(REQUEST_BODY)
        .responseObject(ApiResponseWrapper.Deserializer()).third.get().response


    private data class ApiResponse(@SerializedName("Names") val names: Array<String>)

    private data class ApiResponseWrapper(@SerializedName("d") val response: ApiResponse) {
        class Deserializer : ResponseDeserializable<ApiResponseWrapper> {
            override fun deserialize(content: String): ApiResponseWrapper =
                Gson().fromJson(content, ApiResponseWrapper::class.java)
        }
    }
}