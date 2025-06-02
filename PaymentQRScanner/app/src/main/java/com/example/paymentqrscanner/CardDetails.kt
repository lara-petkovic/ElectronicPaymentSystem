package com.example.paymentqrscanner

import com.google.gson.annotations.Expose

data class CardDetails (
    val Pan: String = "2345678901234560",
    val HolderName : String = "Dusan Sudjic",
    val ExpirationDate: String = "11/26",
    val SecurityCode: String = "456",
    var PaymentRequestId: String = "",
){
    fun getJsonString(): String {
        return """
            {
                "Pan": "$Pan",
                "HolderName": "$HolderName",
                "ExpirationDate": "$ExpirationDate",
                "SecurityCode": "$SecurityCode",
                "PaymentRequestId": "$PaymentRequestId"
            }
        """.trimIndent()
    }
}