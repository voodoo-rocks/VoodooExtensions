package rocks.voodoo.extensions

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.*


/**
Implemented for String and Int values as params. Other types ignored
 */
inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        val value = it.second
        when (value) {
            is String -> intent.putExtra(it.first, value)
            is Int -> intent.putExtra(it.first, value)
        // etc.
        }
    }
    startActivity(intent)
}

fun Context.navigateToDialer(tel: String?) {
    tel?.let {
        val number = Uri.parse("tel:$it")
        val intent = Intent(Intent.ACTION_DIAL, number)
        intent.resolveActivity(packageManager)?.run { startActivity(intent) }
    }
}

fun Context.navigateToSendEmailApp(email: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    intent.resolveActivity(packageManager)?.run { startActivity(intent) }
}

fun Context.navigateToOpenUrlApp(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    intent.resolveActivity(packageManager)?.run { startActivity(intent) }
}

fun Context.showTimeDialog(date: Date, listener: TimePickerDialog.OnTimeSetListener) {
    val calendar = Calendar.getInstance()
    calendar.time = date
    TimePickerDialog(this, listener, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), false)
            .show()
}

fun Context.getSystemAppSettingsIntent(): Intent {
    return Intent("android.settings.APP_NOTIFICATION_SETTINGS")
            // for android 5-7
            .putExtra("app_package", packageName)
            .putExtra("app_uid", applicationInfo.uid)
            // for android 8
            .putExtra("android.provider.extra.APP_PACKAGE", packageName)
}