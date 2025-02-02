package com.minar.birday.utilities

import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.minar.birday.R
import com.minar.birday.model.EventCode
import com.minar.birday.model.EventResult
import java.io.ByteArrayOutputStream


// Given a bitmap, convert it to a byte array
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val imgConverted: ByteArray = byteArrayOf()
    return try {
        bitmap.toByteArray()
    } catch (e: Exception) {
        imgConverted
    }
}

// Given a byte array containing an image, return the corresponding bitmap
fun byteArrayToBitmap(byteImg: ByteArray): Bitmap {
    // If the string is empty, just return an empty white bitmap
    if (byteImg.isEmpty()) return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    return BitmapFactory.decodeByteArray(byteImg, 0, byteImg.size)
}

// Get the smallest dimension in a non-square image to crop and resize it
fun getBitmapSquareSize(bitmap: Bitmap): Int {
    return bitmap.width.coerceAtMost(bitmap.height)
}

// Transform a square bitmap in a circular bitmap, useful for notification
fun getCircularBitmap(bitmap: Bitmap): Bitmap {
    val output = Bitmap.createBitmap(
        bitmap.width,
        bitmap.height,
        Bitmap.Config.ARGB_8888,
    )
    val canvas = Canvas(output)
    val color: Int = Color.GRAY
    val paint = Paint()
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    val rectF = RectF(rect)
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    canvas.drawOval(rectF, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
}

// Return the event image converted in bitmap or the placeholder
fun setEventImageOrPlaceholder(event: EventResult, eventImage: ImageView) {
    if (event.image != null && event.image.isNotEmpty()) {
        // The click is not implemented atm
        eventImage.setImageBitmap(byteArrayToBitmap(event.image))
    } else eventImage.setImageDrawable(
        ContextCompat.getDrawable(
            eventImage.context,
            // Set the image depending on the event type
            when (event.type) {
                EventCode.BIRTHDAY.name -> R.drawable.placeholder_birthday_image
                EventCode.ANNIVERSARY.name -> R.drawable.placeholder_anniversary_image
                EventCode.DEATH.name -> R.drawable.placeholder_death_image
                EventCode.NAME_DAY.name -> R.drawable.placeholder_name_day_image
                else -> R.drawable.placeholder_other_image
            }
        )
    )
}

// Loop an animated vector drawable indefinitely
fun ImageView.applyLoopingAnimatedVectorDrawable(@DrawableRes animatedVector: Int) {
    val animated = AnimatedVectorDrawableCompat.create(context, animatedVector)
    animated?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            this@applyLoopingAnimatedVectorDrawable.post { animated.start() }
        }
    })
    this.setImageDrawable(animated)
    animated?.start()
}

// Extension function to convert bitmap to byte array
fun Bitmap.toByteArray(): ByteArray {
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.JPEG, 100, this)
        return toByteArray()
    }
}
