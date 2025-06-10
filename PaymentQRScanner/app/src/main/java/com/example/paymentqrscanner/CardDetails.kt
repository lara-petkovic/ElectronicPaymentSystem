package com.example.paymentqrscanner

import com.google.gson.annotations.Expose

class CardDetails
{
    var Pan: String = ""
    val HolderName : String = "Dusan Sudjic"
    val ExpirationDate: String = "11/26"
    val SecurityCode: String = "456"
    var PaymentRequestId: String = ""
    constructor(acqAcc:Boolean){
        Pan = if(acqAcc) "2345678901234560" else "6666666666666664"
    }

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