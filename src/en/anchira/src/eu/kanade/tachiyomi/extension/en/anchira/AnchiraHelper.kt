package eu.kanade.tachiyomi.extension.en.anchira

import android.util.Base64
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import okio.ByteString.Companion.decodeBase64

object AnchiraHelper {
    const val KEY = "ZnVjayBuaWdnZXJzIGFuZCBmYWdnb3RzLCBhbmQgZGVhdGggdG8gYWxsIGpld3M="

    val json = Json { ignoreUnknownKeys = true }

    fun getPathFromUrl(url: String) = "${url.split("/").reversed()[1]}/${url.split("/").last()}"

    inline fun <reified T> decodeBytes(body: ResponseBody): T {
        val encryptedText = body.string().decodeBase64()!!
        return json.decodeFromString(
            XXTEA.decryptToString(
                encryptedText.toByteArray(),
                key = Base64.decode(KEY, Base64.DEFAULT).decodeToString(),
            )!!,
        )
    }

    fun prepareTags(tags: List<Tag>) = tags.map {
        if (it.namespace == null) {
            it.namespace = 6
        }
        it
    }.sortedBy { it.namespace }.map {
        return@map it.name.lowercase()
    }.joinToString(", ") { it }
}
