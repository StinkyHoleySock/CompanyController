package com.example.companycontroller

import android.app.Activity
import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class FcmNotificationsSender(
    var userFcmToken: String,
    var title: String,
    var body: String,
    var mContext: Context,
    var mActivity: Activity
) {
    private var requestQueue: RequestQueue? = null
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey = "BK9yXl4ZEAVgJIqn5sOrEVvIzPuGu2S4J2g5fM9hMw3HjiIknO77n_UGiXQ5mjQD6Quwq73yXxaLZD-CE7augEU"

    fun SendNotifications() {
        requestQueue = Volley.newRequestQueue(mActivity)
        val mainObj = JSONObject()
        try {
            mainObj.put("to", userFcmToken)
            val notiObject = JSONObject()
            notiObject.put("title", title)
            notiObject.put("body", body)
            notiObject.put("icon", "icon") // enter icon that exists in drawable only
            mainObj.put("notification", notiObject)
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST, postUrl, mainObj,
                Response.Listener {
                    // code run is got response
                },
                Response.ErrorListener {
                    // code run is got error
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["Content-Type"] = "application/json"
                    header["Authorization"] = "key=$fcmServerKey"
                    return header
                }
            }
            requestQueue!!.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
