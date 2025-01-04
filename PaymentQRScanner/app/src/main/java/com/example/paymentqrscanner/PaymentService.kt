package com.example.paymentqrscanner

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

object PaymentService {
    fun pay(pozivNaBroj: String, context: Context) {
        val cardDetails = CardDetails()
        cardDetails.PaymentRequestId = pozivNaBroj
        val jsonBody = cardDetails.getJsonString()
        val client = OkHttpClient()
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)

        val request = Request.Builder()
            .url("http://192.168.0.16:8052/api/payments/payWithQr")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                (context as? Activity)?.runOnUiThread {
                    Toast.makeText(context, "Payment unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let {
                        val message = if (it.toLowerCase(Locale.ROOT) == "true") {
                            "Payment successful"
                        } else {
                            "Payment unsuccessful"
                        }
                        (context as? Activity)?.runOnUiThread {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "Payment unsuccessful", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
