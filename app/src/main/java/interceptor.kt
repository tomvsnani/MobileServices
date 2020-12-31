import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.Exception


class interceptor : Interceptor {
    @Throws(IOException::class)
    @Override
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val rawJson: String = response.body()?.string().toString()
//        Log.d("jsonLog111", rawJson)
        try {
            val `object` = JSONTokener(rawJson).nextValue()
            val jsonLog =
                if (`object` is JSONObject) `object`.toString(4) else (`object` as JSONArray).toString(
                    4
                )
            Log.d("jsonLog", jsonLog)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
    }
}