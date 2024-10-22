package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.ui.theme.MyApplicationTheme
import vn.momo.momo_partner.AppMoMoLib

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEBUG)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {innerPadding ->
                    BankButton(this,modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BankButton(context: MainActivity,modifier: Modifier = Modifier) {
    Row{
        Button(
            onClick = { requestPayment(context) },
            modifier = modifier
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite"
                )
                Text(text = "Momo")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { openApp(context) },
            modifier = modifier
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite"
                )
                Text(text = "Momo intent")
            }
        }
    }
}

private fun openApp(context: MainActivity){
    val packageManager = context.packageManager
    val packageName = context.getString(R.string.momo)
    val intent = packageManager.getLaunchIntentForPackage(packageName)
    if (intent != null) {
        startActivity(context, intent, null)
    } else {
        // Redirect to Play Store if the app is not installed
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mservice.momotransfer&hl=vi"))
        startActivity(context, playStoreIntent, null)
    }
}

private fun requestPayment(context : MainActivity) {
    val amount = 10000
    val merchantName = "Google"
    val merchantCode = "SCB01"
    AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
    AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
    val eventValue: MutableMap<String, Any> = HashMap()
    eventValue["merchantname"] = merchantName
    eventValue["merchantcode"] = merchantCode
    eventValue["amount"] = amount
    eventValue["orderId"] = "orderId123456789"
    eventValue["orderLabel"] = "Mã đơn hàng"
    try {
        AppMoMoLib.getInstance().requestMoMoCallBack(context, eventValue)
        Log.d("MoMoPayment", "requestPayment: ${AppMoMoLib.getInstance().REQUEST_CODE_MOMO}")
    }catch (e : Exception){
        Log.d("MoMoPayment", "requestPayment: $e")
    }
}
