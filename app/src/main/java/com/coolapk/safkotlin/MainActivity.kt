package com.coolapk.safkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun open(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val directoryUri = data?.data ?: return
                // Open with `DocumentFile.fromTreeUri`...
                val file = DocumentFile.fromTreeUri(this, directoryUri)

                val findFile = file?.findFile("money.txt")
                if (findFile == null) {
                    val createFile = file?.createFile("text/plain", "money.txt")
                    alterDocument(createFile?.uri!!)
                } else {
                    alterDocument(findFile.uri)
                }


            } else {
                // The user cancelled the request.
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun alterDocument(uri: Uri) {
        try {
            val pfd = this.contentResolver.openFileDescriptor(uri, "w") ?: return
            val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
            fileOutputStream.write(
                ("Overwritten by MyCloud at " +
                        System.currentTimeMillis() + "\n").toByteArray()
            )
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close()
            pfd.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val OPEN_DIRECTORY_REQUEST_CODE = 101
    }
}
