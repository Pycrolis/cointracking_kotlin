package net.tubylaris.cointracking

import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val okHttpClient = OkHttpClient()
private val logger: Logger = LoggerFactory.getLogger("cointraking")

val apiKey: String = System.getenv("API_KEY")
val apiSecret: String = System.getenv("API_SECRET")

fun getBalance(): String {
    logger.info("Getting balance")
    return callCointrackingAPI("getBalance")
}

fun getHistoricalCurrency(): String {
    logger.info("Getting historical currency")
    return callCointrackingAPI("getHistoricalCurrency")
}

fun getTrades(parameters: Map<String,String>): String {
    logger.info("Getting trades")
    return callCointrackingAPI("getTrades", parameters)
}

private fun callCointrackingAPI(method: String, parameters: Map<String,String>): String {
    val payload = mapOf(
        "method" to method,
        "nonce" to System.currentTimeMillis().toString(),
        ) + parameters
    return callCointrackingAPI(payload)
}

private fun callCointrackingAPI(method: String): String {
    return callCointrackingAPI(method, mapOf())
}

private fun callCointrackingAPI(payload: Map<String, String>): String {
    val request = buildRequest(payload)

    return okHttpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        response.body!!.string()
    }
}

private fun multipartBody(payload: Map<String, String>): MultipartBody {
    val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
    payload.entries.forEach { builder.addFormDataPart(it.key, it.value) }
    return builder.build()
}

private fun generatePayloadHMAC(payload: Map<String, String>): String {
    val data = payload.entries.joinToString("&") { "${it.key}=${it.value}" }
    return generateHMAC(data)
}

private fun buildRequest(payload: Map<String, String>): Request {
    val bodyHMAC = generatePayloadHMAC(payload)
    val multipartBody = multipartBody(payload)

    return Request.Builder()
        .url(getHttpUrl())
        .addHeader("key", apiKey)
        .addHeader("sign", bodyHMAC)
        .post(multipartBody)
        .build()
}

private fun getHttpUrl(): HttpUrl {
    val httpUrl = HttpUrl.Builder()
        .scheme("https")
        .host("cointracking.info")
        .addPathSegments("api/v1/")
        .build()
    return httpUrl
}

private fun generateHMAC(data: String): String {
    val secret = apiSecret
    val algorithm = "HmacSHA512"
    val keySpec = SecretKeySpec(secret.toByteArray(Charsets.UTF_8), algorithm)
    val mac = Mac.getInstance(algorithm)
    mac.init(keySpec)
    val hmacBytes = mac.doFinal(data.toByteArray(Charsets.UTF_8))

    return hmacBytes.joinToString("") {
        String.format("%02x", it)
    }
}