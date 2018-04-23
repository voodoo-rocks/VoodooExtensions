package rocks.voodoo.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

fun Bitmap?.toBase64(): String? {
    if (this == null) return null
    val bufferSize = 1024
    val buffer = CharArray(bufferSize)
    val stringBuffer = StringBuilder()

    ByteArrayOutputStream().use { byteArrayOutputStream ->

        compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val bytes = Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

        InputStreamReader(ByteArrayInputStream(bytes)).use { inputStreamReader ->
            while (true) {
                val read = inputStreamReader.read(buffer, 0, bufferSize)
                if (read == -1) {
                    break
                }

                val line = String(buffer, 0, read)
                stringBuffer.append(line)
            }
        }
    }
    return stringBuffer.toString()
}

fun Bitmap.cropCenterBitmap(width: Int, height: Int): Bitmap {
    val originalWidth = this.width
    val originalHeight = this.height

    var requiredWidth: Int = originalWidth
    var requiredHeight: Int = originalHeight

    if (originalWidth > width) {
        requiredWidth = width
    }
    if (originalHeight > height) {
        requiredHeight = height
    }

    val x = (originalWidth / 2) - (requiredWidth / 2)
    val y = (originalHeight / 2) - (requiredHeight / 2)

    return cropBitmap(x, y, width, height)
}

fun Bitmap.cropBitmap(x: Int, y: Int, width: Int, height: Int): Bitmap {
    val originalWidth = this.width
    val originalHeight = this.height

    var requiredWidth: Int = originalWidth
    var requiredHeight: Int = originalHeight

    if (originalWidth > width) {
        requiredWidth = width
    }
    if (originalHeight > height) {
        requiredHeight = height
    }

    val pixels = IntArray(requiredWidth * requiredHeight)

    this.getPixels(pixels, 0, requiredWidth, x, y, requiredWidth, requiredHeight)

    val avatarBitmap: Bitmap = Bitmap.createBitmap(requiredWidth, requiredHeight, Bitmap.Config.ARGB_8888)

    avatarBitmap.setPixels(pixels, 0, requiredWidth, 0, 0, requiredWidth, requiredHeight)

    return avatarBitmap
}

fun Bitmap.scaleBitmap(width: Int, height: Int): Bitmap {
    val originalWidth = this.width
    val originalHeight = this.height
    val ratioBitmap = originalWidth.toFloat() / originalHeight.toFloat()
    val ratioMax = width.toFloat() / height.toFloat()

    var finalWidth = width
    var finalHeight = height
    if (ratioMax > ratioBitmap) {
        finalWidth = (height.toFloat() * ratioBitmap).toInt()
    } else {
        finalHeight = (width.toFloat() / ratioBitmap).toInt()
    }
    return Bitmap.createScaledBitmap(this, finalWidth, finalHeight, true)
}