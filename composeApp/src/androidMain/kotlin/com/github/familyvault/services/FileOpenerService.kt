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
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileOpenerService(private val context: Context) : IFileOpenerService {
    private val TAG = "FileOpenerService"
    
    override fun openFileWithExternalViewer(fileBytes: ByteArray, mimeType: String, fileName: String): Boolean {
        return try {
            // Create a temporary file
            val tempFile = createTempFile(fileBytes, fileName)
            Log.d(TAG, "Created temp file for $fileName (${fileBytes.size} bytes), mime: $mimeType")
            
            // Get content URI using FileProvider
            val authority = "${context.packageName}.fileprovider"
            val contentUri = FileProvider.getUriForFile(context, authority, tempFile)
            Log.d(TAG, "ContentUri: $contentUri")
            
            // Create a VIEW intent specifically targeting the Android Files app
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(contentUri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                
                // On newer Android versions, try to use the built-in viewer
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Try to use the system PDF viewer first
                    val systemViewerPackage = "com.google.android.apps.docs"
                    try {
                        // See if Google Drive (PDF viewer) is available
                        context.packageManager.getPackageInfo(systemViewerPackage, 0)
                        setPackage(systemViewerPackage)
                        Log.d(TAG, "Using Google Drive as PDF viewer")
                    } catch (e: Exception) {
                        Log.d(TAG, "Google Drive not available, using system picker")
                    }
                }
            }
            
            // Grant permissions to all potential viewers
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
            
            // Log viewers for debugging
            Log.d(TAG, "Available PDF viewers: ${resInfoList.map { it.activityInfo.packageName }}")
            
            if (resInfoList.isNotEmpty()) {
                // If we have viewers, show chooser to let user pick
                val chooserIntent = Intent.createChooser(intent, "Open PDF with")
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(chooserIntent)
                Log.d(TAG, "Showing PDF viewer chooser for $fileName")
                true
            } else {
                // No PDF viewer found
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
            // Make sure the filename has the correct extension
            val finalFileName = if (isPdfFile(fileBytes) && !fileName.endsWith(".pdf", ignoreCase = true)) {
                "$fileName.pdf"
            } else {
                fileName
            }
            
            // Create unique filename with timestamp
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val uniqueFileName = "${timeStamp}_$finalFileName"
            
            // Get the Downloads directory
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, uniqueFileName)
            
            // Write the file
            FileOutputStream(file).use { it.write(fileBytes) }
            
            // Make the file visible in Downloads app and file explorers
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                arrayOf("application/pdf"),
                null
            )
            
            // Show success message
            Toast.makeText(
                context,
                "PDF downloaded to Downloads/$uniqueFileName",
                Toast.LENGTH_LONG
            ).show()
            
            // Open the file explorer to the Downloads folder
            try {
                // First try: open Downloads directory
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse("content://downloads/all_downloads"), "resource/folder")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                Log.d(TAG, "Opened Downloads folder")
            } catch (e: Exception) {
                Log.e(TAG, "Could not open Downloads folder: ${e.message}", e)
                
                // Second try: open the specific file
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
    
    override fun isPdfFile(fileBytes: ByteArray): Boolean {
        // Check PDF file signature (%PDF-)
        val isPdf = fileBytes.size >= 4 && 
               fileBytes[0].toInt() == 0x25 && // %
               fileBytes[1].toInt() == 0x50 && // P
               fileBytes[2].toInt() == 0x44 && // D
               fileBytes[3].toInt() == 0x46    // F
               
        Log.d(TAG, "PDF detection result: $isPdf (${fileBytes.size} bytes, first bytes: ${fileBytes.take(4).joinToString("") { String.format("%02X", it) }})")
        return isPdf
    }
    
    private fun createTempFile(fileBytes: ByteArray, fileName: String): File {
        // Make sure the filename ends with .pdf for PDF files
        val finalFileName = if (isPdfFile(fileBytes) && !fileName.endsWith(".pdf", ignoreCase = true)) {
            "$fileName.pdf"
        } else {
            fileName
        }
        
        // Create a directory that's accessible to other apps
        val cacheDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) 
                     ?: context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                     ?: context.cacheDir
        
        // Create a unique file name
        val timeStamp = System.currentTimeMillis()
        val file = File(cacheDir, "${timeStamp}_$finalFileName")
        
        // Write the bytes to the file
        FileOutputStream(file).use { it.write(fileBytes) }
        
        Log.d(TAG, "Created temp file at: ${file.absolutePath}")
        return file
    }
}
