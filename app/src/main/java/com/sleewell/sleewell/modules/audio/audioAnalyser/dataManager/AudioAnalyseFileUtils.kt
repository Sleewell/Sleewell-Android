package com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

/**
 * Manager of the audio analyse in the files
 * Can save or read analyse but also delete old analyse
 *
 * @author Hugo Berthomé
 */
class AudioAnalyseFileUtils(context: Context, val listener: IAudioAnalyseRecordListener) :
    IAnalyseDataManager {
    private val classtag = "AUDIO_ANALYSE_FILE_UTIL"

    private val gson = Gson()
    private var startFile = true

    private val outputDirectory = context.cacheDir?.absolutePath + "/analyse"
    private var outputFile: File? = null
    private var outputStream: OutputStream? = null

    // coroutine
    private var scopeIO = CoroutineScope(Job() + Dispatchers.IO)
    private val queueData: Queue<AnalyseValue> = LinkedList<AnalyseValue>()
    private var endSaving = false
    private var threadRunning = false

    /**
     * List all the analyses register on the device
     *
     * @return Array of all the available analyse by their timestamp
     * @author Hugo Berthomé
     */
    override fun getAvailableAnalyse() {
        val listFiles = readDirectory()
        val listAvailableAnalyse = mutableListOf<Night>()

        listFiles.forEach { file ->
            try {
                val currentFileDate = file.name.replace(".json", "")
                listAvailableAnalyse.add(Night(dateStringToTimestamp(currentFileDate), null, null))
            } catch (error: DateTimeParseException) {
                Log.e(
                    classtag,
                    "Invalid file name for analyse, cannot be converted to epoch seconds : " + file.name.replace(
                        ".json",
                        ""
                    )
                )
            }
        }
        listener.onListAvailableAnalyses(listAvailableAnalyse)
    }

    /**
     * Read an analyse
     *
     * @param id identifying the analyse
     * @author Hugo Berthomé
     */
    override fun readAnalyse(id: Long) {
        scopeIO.run {
            val emptyArray = arrayOf<AnalyseValue>()
            val fileName = timestampToDateString(id)
            val analyse = File("$outputDirectory/$fileName.json")

            if (!analyse.exists()) {
                listener.onAnalyseRecordError("File " + analyse.name + " doesn't exist")
                return@run
            }
            try {
                val res = gson.fromJson(analyse.reader(), emptyArray.javaClass)
                if (res == null)
                    listener.onReadAnalyseRecord(emptyArray, id)
                else
                    listener.onReadAnalyseRecord(res, id)
            } catch (eSyntax: JsonSyntaxException) {
                Log.e(classtag, "Invalid json syntax in analyse file " + analyse.name)
                listener.onAnalyseRecordError("Invalid json syntax in analyse file " + analyse.name)
            } catch (eIO: JsonIOException) {
                Log.e(classtag, "Failed to read file  " + analyse.name)
                listener.onAnalyseRecordError("Failed to read file  " + analyse.name)
            }
        }
    }

    /**
     * Read an analyse
     *
     * @param date to identify the analyse
     * @author Hugo Berthomé
     */
    override fun readAnalyse(date: Date) {
        readAnalyse(date.time)
    }

    /**
     * Delete an analyse
     *
     * @param id identifying the analyse
     * @author Hugo Berthomé
     */
    override fun deleteAnalyse(id: Long) {
        val fileName = timestampToDateString(id)
        val analyse = File("$outputDirectory/$fileName.json")

        if (analyse.exists())
            analyse.delete()
    }

    /**
     * Init the registration on a new analyse
     *
     * @author Hugo Berthomé
     */
    override fun initNewAnalyse(): Boolean {
        val outputFileName = getCurrentDateHour()
        outputFile = File("$outputDirectory/$outputFileName.json")
        try {
            if (!createDir())
                return false

            outputStream = FileOutputStream(outputFile)
            outputStream?.write("[".toByteArray())
            startFile = true
        } catch (e: FileNotFoundException) {
            outputFile = null
            outputStream = null
            Log.e(classtag, "File " + outputFile?.name + " couldn't be created")
            return false
        }
        return true
    }

    /**
     * Add a value to the analyse
     *
     * @param value to add
     * @author Hugo Berthomé
     */
    override fun addToAnalyse(value: AnalyseValue) {
        if (!isSaving())
            listener.onAnalyseRecordError("Record not initialized")

        queueData.add(value)

        if (!threadRunning) {
            scopeIO.run {
                threadRunning = true

                while (queueData.size != 0) {
                    val valueFromQueue = queueData.poll()

                    if (valueFromQueue != null) {
                        val jsonString = gson.toJson(valueFromQueue)
                        try {
                            if (!startFile)
                                outputStream?.write(",".toByteArray())
                            outputStream?.write(jsonString.toByteArray())
                            startFile = false
                        } catch (e: IOException) {
                            Log.e(classtag, "Unable to write inside the file")
                            listener.onAnalyseRecordError("Unable to write inside the file")
                            threadRunning = false
                            return@run
                        }
                    }
                }
                if (endSaving) {
                    endSaving()
                }
                threadRunning = false
            }
        }
    }

    /**
     * Return if an analyse is currently being saved
     *
     * @return Boolean
     * @author Hugo Berthomé
     */
    override fun isSaving(): Boolean {
        return outputStream != null || outputFile != null
    }

    /**
     * Stop the recording of the new analyse
     *
     * @author Hugo Berthomé
     */
    override fun endNewAnalyse() {
        if (!isSaving())
            return

        endSaving = true
        if (!threadRunning) {
            scopeIO.run {
                endSaving()
            }
        }
    }

    private fun endSaving() {
        outputStream?.write("]".toByteArray())
        startFile = true
        outputStream = null
        outputFile = null
        endSaving = false
        threadRunning = false
        listener.onAnalyseRecordEnd()
    }

    /**
     * Read the analyse directory and return all the files existing
     *
     * @return Array<File> array with all the file existing
     * @author Hugo Berthomé
     */
    private fun readDirectory(): Array<File> {
        val dir = File(outputDirectory)

        if (!dir.exists()) {
            return arrayOf()
        }
        val files = dir.listFiles()
        if (files != null) {
            files.sortBy { it.name }
            return files
        }
        return arrayOf()
    }

    /**
     * Create the save directory in the cache
     *
     * @return true if succeed false otherwise
     */
    private fun createDir(): Boolean {
        val dir = File(outputDirectory)

        if (!dir.exists()) {
            try {
                dir.mkdirs()
            } catch (e: SecurityException) {
                Log.e(classtag, "Directory $outputDirectory couldn't be created")
                return false
            }
        }
        return true
    }

    companion object {
        /*val DATE_FORMAT = "yyyy-MM-dd_HH:mm:ss"*/
        val DATE_FORMAT = "yyyy-MM-dd"

        /**
         * Convert a string date to timestamp
         *
         * @param date
         * @return timestamp
         */
        private fun dateStringToTimestamp(date: String): Long {
            return LocalDateTime.parse(
                date,
                DateTimeFormatter.ofPattern(DATE_FORMAT)
            ).toEpochSecond(OffsetDateTime.now().offset)
        }

        /**
         * Convert a timestamp to date string format
         *
         * @param timestamp
         * @return
         */
        fun timestampToDateString(timestamp: Long): String {
            return DateTimeFormatter
                .ofPattern(DATE_FORMAT)
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.ofEpochSecond(timestamp))
        }

        /**
         * Return the current date formatted
         *
         * @return the current date with the time
         */
        private fun getCurrentDateHour(): String {
            return DateTimeFormatter
                .ofPattern(DATE_FORMAT)
                .withZone(ZoneOffset.systemDefault())
                .format(Instant.now())
        }
    }
}