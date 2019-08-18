package net.furusin.www.watchfacesample

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

private val TAG = MainActivity::class.java.simpleName
private const val IMAGE_SIZE_MAX = 512

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.select_image_button).setOnClickListener {
            getImage()
        }
    }

    private val READ_REQUEST_CODE = 1
    /**
     * 端末に保存されている画像を取得する
     */
    private fun getImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return
            }

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap.scale().cropToSquare())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 画像をリサイズする
     * @param size リサイズ後の画像の短辺のサイズ
     */
    private fun Bitmap.scale(size: Int = IMAGE_SIZE_MAX): Bitmap {
        var scaledBitmap = this
        val bitmapWidth = width
        val bitmapHeight = height

        if (width > IMAGE_SIZE_MAX && height > IMAGE_SIZE_MAX) {
            val scaleWidth = IMAGE_SIZE_MAX.toFloat() / bitmapWidth
            val scaleHeight = IMAGE_SIZE_MAX.toFloat() / bitmapHeight
            val scaleFactor = Math.min(scaleWidth, scaleHeight)

            val scale = Matrix()
            scale.postScale(scaleFactor, scaleFactor)
            scaledBitmap = Bitmap.createBitmap(this, 0, 0, bitmapWidth, bitmapHeight, scale, false)
        }
        return scaledBitmap
    }

    /**
     * 画像を中央から正方形に切り取る
     */
    private fun Bitmap.cropToSquare(): Bitmap {
        var croppedBitmap = this
        var size: Int
        if (width > height) {
            size = height
            croppedBitmap = Bitmap.createBitmap(croppedBitmap, width / 2 - size / 2, 0, size, size, null, true)
        } else {
            size = width
            croppedBitmap = Bitmap.createBitmap(croppedBitmap, 0, height / 2 - size / 2, size, size, null, true)

        }
        return croppedBitmap
    }
}
