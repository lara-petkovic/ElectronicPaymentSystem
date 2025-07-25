package com.example.paymentqrscanner

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.integration.android.IntentIntegrator

@Composable
fun QRScannerScreen() {
    var qrCodeResult by remember { mutableStateOf("Scan a QR code to see details.") }
    val context = LocalContext.current
    val activity = context as? Activity

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult = IntentIntegrator.parseActivityResult(
                result.resultCode, result.data
            )
            val qrCodeResult = intentResult?.contents ?: "No QR code detected."

            val intent = Intent(context, PaymentActivity::class.java).apply {
                putExtra("qrCodeResult", qrCodeResult)
            }
            context.startActivity(intent)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = qrCodeResult,
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(onClick = {
                if (activity != null) {
                    val intentIntegrator = IntentIntegrator(activity).apply {
                        setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        setPrompt("Scan a QR Code")
                        setBeepEnabled(true)
                        setOrientationLocked(true)
                    }
                    launcher.launch(intentIntegrator.createScanIntent())
                }
            }) {
                Text("Scan QR Code")
            }
        }
    }
}
