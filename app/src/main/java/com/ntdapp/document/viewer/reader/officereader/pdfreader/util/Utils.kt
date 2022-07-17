package com.ntdapp.document.viewer.reader.officereader.pdfreader.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.ntdapp.document.viewer.reader.officereader.pdfreader.SplashScreenActivity
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.commons.helpers.isRPlus
import com.simplemobiletools.commons.models.FileDirItem
import com.stericson.RootShell.execution.Command
import com.stericson.RootTools.RootTools
import java.io.*


object Utils {
    fun getFileName(uri: Uri, context: Context): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            try {
                context.contentResolver.query(uri, null, null, null, null).use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val indexDisplayName: Int =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (indexDisplayName != -1) {
                            result = cursor.getString(indexDisplayName)
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }

    fun fileShareIntent(chooserTitle: String?, fileUri: Uri?): Intent? {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.type = "application/pdf"
        return Intent.createChooser(shareIntent, chooserTitle)
    }

    fun takeScreenshot(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun saveBitmap(bitmap: Bitmap, context: Context): String {
        val filePath: File =
            context.getExternalFilesDir(null)!!.absoluteFile
        val dir = File(filePath.absolutePath.toString() + "/AllDocument/")
        dir.mkdir()
        val nameFile = "screenshot.jpg"
        val file = File(dir, nameFile)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return file.absolutePath
        }
    }

    fun saveBitmapByName(bitmap: Bitmap, context: Context, name: String,view: View,tv: TextView): String {
        val filePath: File =
            context.getExternalFilesDir(null)!!.absoluteFile
        val dir = File(filePath.absolutePath.toString() + "/AllDocument/")
        dir.mkdir()
        val nameFile = "$name.jpg"
        val file = File(dir, nameFile)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
//            var toast = Toast.makeText(
//                context,
//                "The screen shot stored in path that is " + file.absolutePath,
//                Toast.LENGTH_LONG
//            )
//            toast.setGravity(Gravity.CENTER, 0, 0)
//            toast.show()

            tv.text = "The screen shot stored in path that is " + file.absolutePath
            val toast = Toast(context)
            toast.setGravity(Gravity.BOTTOM, 0, 400)
            toast.duration = Toast.LENGTH_SHORT
            toast.setView(view)
            toast.show()

        } catch (e: FileNotFoundException) {
            Toast.makeText(context, e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } catch (e: IOException) {
            Toast.makeText(context, e.printStackTrace().toString(), Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } finally {
            return file.absolutePath
        }
    }

    fun saveBitmapTemp(bitmap: Bitmap, context: Context): String {
        val filePath: File =
            context.getExternalFilesDir(null)!!.absoluteFile
        val dir = File(filePath.absolutePath.toString() + "/AllDocumentTemp/")
        dir.mkdir()
        val nameFile = "screenshot.jpg"
        val file = File(dir, nameFile)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return file.absolutePath
        }
    }

    @Throws(FileNotFoundException::class)
    fun writeBitmapToUri(
        context: Context,
        bitmap: Bitmap,
        uri: Uri?,
        compressFormat: CompressFormat?,
        i: Int
    ) {
        var outputStream: OutputStream? = null
        try {
            outputStream = context.contentResolver.openOutputStream(uri!!)
            bitmap.compress(compressFormat, i, outputStream)
        } finally {
            closeSafe(outputStream)
        }
    }

    private fun closeSafe(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (unused: IOException) {
            }
        }
    }

    private const val APP_DOC = "AppDoc"
    private const val KEY_PATH = "KeyPath"
    private const val KEY_PATH_RECENT = "KeyPathRecent"

    fun isFileFavorite(path: String, context: Context): Boolean {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH, null)
        if (listPaths == null) {
            return false
        }
        return listPaths.contains(path)
    }

    fun setFileFavorite(path: String, context: Context, isFavorite: Boolean) {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH, null)
        var setPaths: HashSet<String> = HashSet()
        if (listPaths != null) {
            setPaths.addAll(listPaths)
        }
        if (isFavorite) {
            setPaths.add(path)
        } else {
            setPaths.remove(path)
        }
        Log.d("99999", "Love" + setPaths.size)
        val editor = sharePef.edit()
        editor.putStringSet(KEY_PATH, setPaths)
        editor.apply()
    }

    fun removeFileFavorite(path: String, context: Context) {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH, null)
        if (listPaths != null && listPaths.contains(path)) {
            var setPaths: HashSet<String> = HashSet()
            if (listPaths != null) {
                setPaths.addAll(listPaths)
            }
            setPaths.remove(path)
            val editor = sharePef.edit()
            editor.putStringSet(KEY_PATH, setPaths)
            editor.apply()
        }
    }

    fun renameFileFavorite(pathOld: String, pathNew: String, context: Context) {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH, null)
        if (listPaths != null && listPaths.contains(pathOld)) {
            var setPaths: HashSet<String> = HashSet()
            if (listPaths != null) {
                setPaths.addAll(listPaths)
            }
            setPaths.remove(pathOld)
            setPaths.add(pathNew)
            val editor = sharePef.edit()
            editor.putStringSet(KEY_PATH, setPaths)
            editor.apply()
        }
    }

    fun setFileRecent(path: String, context: Context) {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH_RECENT, null)
        var setPaths: HashSet<String> = HashSet()
        setPaths.add(path)
        if (listPaths != null) {
            listPaths.remove(path)
            setPaths.addAll(listPaths)
        }
        val editor = sharePef.edit()
        editor.putStringSet(KEY_PATH_RECENT, setPaths)
        editor.putLong(path, -System.currentTimeMillis())
        editor.apply()
    }

    fun getAllFileRecents(context: Context): ArrayList<File> {
        val files: ArrayList<File> = ArrayList()
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH_RECENT, null)
        if (listPaths != null) {
            listPaths.forEach {
                val file = File(it)
                files.add(file)
            }
        }
        return files
    }

    fun getAccessTimeFile(path: String, context: Context): Long {
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        return sharePef.getLong(path, 0L)
    }

    fun getAllFileFavorites(context: Context): ArrayList<File> {
        val files: ArrayList<File> = ArrayList()
        val sharePef = context.getSharedPreferences(APP_DOC, Activity.MODE_PRIVATE)
        val listPaths = sharePef.getStringSet(KEY_PATH, null)
        if (listPaths != null) {
            listPaths.forEach {
                val file = File(it)
                files.add(file)
            }
        }
        return files
    }

    fun isShowAds(): Boolean {
        val timeCurrent = System.currentTimeMillis()
        val deltaTime: Long = (timeCurrent - SplashScreenActivity.TimeEndAds) / 1000
        return deltaTime >= Constants.TIME_SHOW_ADS
    }

    private fun runCommand(command: Command) {
        try {
            RootTools.getShell(true).add(command)
            Log.d("99999999", "Delete Success")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Activity.deleteFile(
        fileDirItem: FileDirItem,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        ensureBackgroundThread {
            deleteFileBg(fileDirItem, allowDeleteFolder, callback)
        }
    }

    fun deleteRecursively2(file: File): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles() ?: return file.delete()
            for (child in files) {
                deleteRecursively2(child)
            }
        }

        return file.delete()
    }


    fun Activity.deleteFileBg(
        fileDirItem: FileDirItem,
        allowDeleteFolder: Boolean = false,
        callback: ((wasSuccess: Boolean) -> Unit)? = null
    ) {
        val path = fileDirItem.path
        if (isRestrictedSAFOnlyRoot(path)) {
            deleteAndroidSAFDirectory(path, allowDeleteFolder, callback)
        } else {
            val file = File(path)
            if (!isRPlus() && file.absolutePath.startsWith(internalStoragePath) && !file.canWrite()) {
                callback?.invoke(false)
                return
            }

            var fileDeleted =
                !isPathOnOTG(path) && ((!file.exists() && file.length() == 0L) || file.delete())
            if (fileDeleted) {
                deleteFromMediaStore(path) { needsRescan ->
                    if (needsRescan) {
                        rescanAndDeletePath(path) {
                            runOnUiThread {
                                callback?.invoke(true)
                            }
                        }
                    } else {
                        runOnUiThread {
                            callback?.invoke(true)
                        }
                    }
                }
            } else {
                if (getIsPathDirectory(file.absolutePath) && allowDeleteFolder) {
                    fileDeleted = deleteRecursively2(file)
                }

                if (!fileDeleted) {
                    if (needsStupidWritePermissions(path)) {

                    } else if (isRPlus()) {
                        val fileUris = getFileUrisFromFileDirItems(arrayListOf(fileDirItem)).second
                        deleteSDK30Uris(this, fileUris) { success ->
                            runOnUiThread {
                                callback?.invoke(success)
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteSDK30Uris(activity: Activity, uris: List<Uri>, callback: (success: Boolean) -> Unit) {
        if (isRPlus()) {
//            BaseSimpleActivity.funAfterDelete30File = callback
//            try {
//                val deleteRequest = MediaStore.createDeleteRequest(activity.contentResolver, uris).intentSender
//                startIntentSenderForResult(deleteRequest, DELETE_FILE_SDK_30_HANDLER, null, 0, 0, 0)
//            } catch (e: Exception) {
//               e.printStackTrace()
//            }
        } else {
            callback(false)
        }
    }

    fun updateSDK30Uris(uris: List<Uri>, callback: (success: Boolean) -> Unit) {
        callback(true)
    }


    fun Activity.renameFile(
        oldPath: String,
        newPath: String,
        isRenamingMultipleFiles: Boolean,
        callback: ((success: Boolean, useAndroid30Way: Boolean) -> Unit)? = null
    ) {
        val oldFile = File(oldPath)
        val newFile = File(newPath)
        val tempFile = try {
            createTempFile(oldFile) ?: return
        } catch (exception: Exception) {
            if (isRPlus() && exception is java.nio.file.FileSystemException) {
                // if we are renaming multiple files at once, we should give the Android 30+ permission dialog all uris together, not one by one
                if (isRenamingMultipleFiles) {
                    callback?.invoke(false, true)
                } else {
                    val fileUris =
                        getFileUrisFromFileDirItems(arrayListOf(File(oldPath).toFileDirItem(this))).second
                    updateSDK30Uris(fileUris) { success ->
                        if (success) {
                            val values = ContentValues().apply {
                                put(
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    newPath.getFilenameFromPath()
                                )
                            }

                            try {
                                contentResolver.update(fileUris.first(), values, null, null)
                                callback?.invoke(true, false)
                            } catch (e: Exception) {
                                showErrorToast(e)
                                callback?.invoke(false, false)
                            }
                        } else {
                            callback?.invoke(false, false)
                        }
                    }
                }
            } else {
                showErrorToast(exception)
                callback?.invoke(false, false)
            }
            return
        }

        val oldToTempSucceeds = oldFile.renameTo(tempFile)
        val tempToNewSucceeds = tempFile.renameTo(newFile)
        if (oldToTempSucceeds && tempToNewSucceeds) {
            if (newFile.isDirectory) {
                updateInMediaStore(oldPath, newPath)
                rescanPath(newPath) {
                    runOnUiThread {
                        callback?.invoke(true, false)
                    }
                    scanPathRecursively(newPath)
                }
            } else {
                if (!baseConfig.keepLastModified) {
                    newFile.setLastModified(System.currentTimeMillis())
                }
                updateInMediaStore(oldPath, newPath)
                scanPathsRecursively(arrayListOf(newPath)) {
                    runOnUiThread {
                        callback?.invoke(true, false)
                    }
                }
            }
        } else {
            tempFile.delete()
            runOnUiThread {
                callback?.invoke(false, false)
            }
        }
    }

    fun getEndFile(name: String): String {
        when {
            name.endsWith(".pdf") -> {
                return ".pdf"
            }
            name.endsWith(".doc") -> {
                return ".doc"
            }
            name.endsWith(".docx") -> {
                return ".docx"
            }
            name.endsWith(".xlsx") -> {
                return ".xlsx"
            }
            name.endsWith(".xls") -> {
                return ".xls"
            }
            name.endsWith(".pptx") -> {
                return ".pptx"
            }
            name.endsWith(".txt") -> {
                return ".txt"
            }
            name.endsWith(".ppt") -> {
                return ".ppt"
            }
            name.endsWith(".png") -> {
                return ".png"
            }
            name.endsWith(".jpg") -> {
                return ".jpg"
            }
            else -> return ""
        }
    }


}