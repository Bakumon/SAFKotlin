package com.coolapk.safkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save.setOnClickListener { save() }
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
                // 保存 uri 到本地
                saveUri(this, directoryUri)
                // 保留权限
                val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(directoryUri, takeFlags)
            } else {
                // The user cancelled the request.
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun save() {
        val uri: Uri = getUri(this) ?: return

        val file = DocumentFile.fromTreeUri(this, uri)

        if (file == null || !file.exists()) {
            Toast.makeText(this, "目录不存在", Toast.LENGTH_LONG).show()
            return
        }

        if (!file.canWrite()) {
            Toast.makeText(this, "目录不可写", Toast.LENGTH_LONG).show()
            return
        }

        val findFile = file.findFile("money.txt")

        if (findFile == null) {
            val createFile = file.createFile("text/plain", "money.txt")
            if (createFile == null) {
                Toast.makeText(this, "文件创建失败", Toast.LENGTH_LONG).show()
            } else {
                alterDocument(createFile.uri)
            }
        } else {
            alterDocument(findFile.uri)
        }
    }

    private fun alterDocument(uri: Uri) {
        try {
            val pfd = this.contentResolver.openFileDescriptor(uri, "w") ?: return
            val fileOutputStream = FileOutputStream(pfd.fileDescriptor)
            fileOutputStream.write(("Overwritten by MyCloud at " + System.currentTimeMillis() + "\n").toByteArray())
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
