package com.example.paymentqrscanner

import android.app.Activity
import android.content.Context
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Locale

object PaymentService {

    private val ip = "192.168.186.9"
    private fun getCustomOkHttpClient(context: Context): OkHttpClient {
        // Load your certificate from raw resource
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val inputStream = context.resources.openRawResource(R.raw.bank)
        val certificate = certificateFactory.generateCertificate(inputStream)
        inputStream.close()

        // Create a KeyStore containing your trusted cert
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("bank", certificate)

        // Create a TrustManager that trusts the cert in your KeyStore
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(keyStore)
        val trustManagers = trustManagerFactory.trustManagers
        val trustManager = trustManagers[0] as X509TrustManager

        // Create an SSLContext that uses your TrustManager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        // Disable hostname verification (for dev/testing only!)
        val hostnameVerifier = HostnameVerifier { hostname, session ->
            // Accept any hostname, or you can restrict to your IP:
            hostname == ip  // or simply: true
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier(hostnameVerifier)
            .build()
    }

    fun pay(acqAcc:Boolean, pozivNaBroj: String, context: Context) {
        val cardDetails = CardDetails(acqAcc)
        cardDetails.PaymentRequestId = pozivNaBroj
        val jsonBody = cardDetails.getJsonString()
        val client = getCustomOkHttpClient(context)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)

        val request = Request.Builder()
            .url("https://$ip:8052/api/payments/payWithQr")
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
                        val message = if (it.lowercase(Locale.ROOT) == "true") {
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
