package com.github.familyvault.services

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.github.familyvault.utils.FileTypeUtils
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileOpenerService(private val context: Context) : IFileOpenerService {
    private val TAG = "FileOpenerService"
    
    override fun openFileWithExternalViewer(
        fileBytes: ByteArray,
        mimeType: String,
        fileName: String
    ): Boolean {
        return try {
            val tempFile = createTempFile(fileBytes, fileName)

            val authority = "${context.packageName}.fileprovider"
            val contentUri = FileProvider.getUriForFile(context, authority, tempFile)
            Log.d(TAG, "ContentUri: $contentUri")
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val systemViewerPackage = "com.google.android.apps.docs"
                    try {
                        context.packageManager.getPackageInfo(systemViewerPackage, 0)
                        setPackage(systemViewerPackage)
                        Log.d(TAG, "Using Google Drive as PDF viewer")
                    } catch (e: Exception) {
                        Log.d(TAG, "Google Drive not available, using system picker")
                    }
                }
            }
            
            val resInfoList = context.packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                Log.d(TAG, "Granting permission to: $packageName")
                context.grantUriPermission(
                    packageName, 
                    contentUri, 
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            if (resInfoList.isNotEmpty()) {
                val chooserIntent = Intent.createChooser(intent, "Open PDF with")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
                Log.d(TAG, "Showing PDF viewer chooser for $fileName")
                true
            } else {
                Log.e(TAG, "No app found to handle PDF files")
                Toast.makeText(
                    context, 
                    "No PDF viewer found. Please install a PDF reader app.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error opening file: ${e.message}", e)
            e.printStackTrace()
            Toast.makeText(
                context,
                "Error opening file: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }
    
    override fun downloadFile(fileBytes: ByteArray, fileName: String): String? {
        return try {
            val finalFileName = if (FileTypeUtils.isPdfFile(fileBytes) &&
                !fileName.endsWith(".pdf", ignoreCase = true)) {
                "$fileName.pdf"
            } else {
                fileName
            }
            
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())
            val uniqueFileName = "${timeStamp}_$finalFileName"
            
            val downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )
            val file = File(downloadsDir, uniqueFileName)
            
            FileOutputStream(file).use { it.write(fileBytes) }
            
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf("application/pdf"),
                null
            )
            
            Toast.makeText(
                context,
                "PDF downloaded to Downloads/$uniqueFileName",
                Toast.LENGTH_LONG
            ).show()
            
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(
                    "content://downloads/all_downloads"),
                    "resource/folder")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d(TAG, "Opened Downloads folder")
            } catch (e: Exception) {
                Log.e(TAG, "Could not open Downloads folder: ${e.message}", e)
                
                try {
                    val fileUri = FileProvider.getUriForFile(
                        context, 
                        "${context.packageName}.fileprovider",
                        file
                    )
                    val openIntent = Intent(Intent.ACTION_VIEW)
                    openIntent.setDataAndType(fileUri, "application/pdf")
                    openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(openIntent)
                    Log.d(TAG, "Opened specific file: $fileUri")
                } catch (e2: Exception) {
                    Log.e(TAG, "Could not open file directly: ${e2.message}", e2)
                }
            }
            
            Log.d(TAG, "File downloaded to: ${file.absolutePath}")
            return file.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading file: ${e.message}", e)
            Toast.makeText(
                context, 
                "Failed to download file: ${e.message}", 
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }
    
    private fun createTempFile(fileBytes: ByteArray, fileName: String): File {
        val finalFileName = if (FileTypeUtils.isPdfFile(fileBytes)
            && !fileName.endsWith(".pdf", ignoreCase = true)) {
            "$fileName.pdf"
        } else {
            fileName
        }
        
        val cacheDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) 
                     ?: context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                     ?: context.cacheDir
        
        val timeStamp = System.currentTimeMillis()
        val file = File(cacheDir, "${timeStamp}_$finalFileName")
        
        FileOutputStream(file).use { it.write(fileBytes) }
        
        Log.d(TAG, "Created temp file at: ${file.absolutePath}")
        return file
    }
}
