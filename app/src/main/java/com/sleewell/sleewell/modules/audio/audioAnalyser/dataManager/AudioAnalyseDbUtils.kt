package com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager

import android.content.Context
import com.sleewell.sleewell.database.analyse.night.entities.Analyse
import com.sleewell.sleewell.database.analyse.night.entities.Night
import com.sleewell.sleewell.database.analyse.night.NightDatabase
import com.sleewell.sleewell.database.analyse.night.entities.NightUpdate
import com.sleewell.sleewell.modules.audio.audioAnalyser.listeners.IAudioAnalyseRecordListener
import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue
import kotlinx.coroutines.*
import java.time.Instant

class AudioAnalyseDbUtils(context: Context, val listener: IAudioAnalyseRecordListener) : IAnalyseDataManager{
    val CLASS_TAG = "AUDIO_ANALYSE_DB_UTILS"

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val db = NightDatabase.getDatabase(context)
    private val nightDao = db.nightDao()
    private val analyseDao = db.analyseDao()

    private var nightId : Long? = null

    /**
     * List all the analyses register on the device
     *
     * @return Array of all the available analyse by their timestamp
     * @author Hugo Berthomé
     */
    override fun getAvailableAnalyse() {
        scope.launch {
            val nights = nightDao.getAll()
            listener.onListAvailableAnalyses(nights)
        }
    }

    /**
     * Read an analyse
     *
     * @param id identifying the analyse
     * @author Hugo Berthomé
     */
    override fun readAnalyse(id: Long) {
        scope.launch {
            val res = analyseDao.getAnalysesFromNightId(id)
            listener.onReadAnalyseRecord(res.toTypedArray(), id)
        }
    }

    /**
     * Delete an analyse
     *
     * @param timestamp identifying the analyse
     * @author Hugo Berthomé
     */
    override fun deleteAnalyse(id: Long) {
        scope.launch {
            val night = nightDao.getNight(id)

            if (night != null) {
                analyseDao.deleteAnalyseFromNightId(night.uId)
                nightDao.deleteNight(night)
            }
        }
    }

    /**
     * Init the registration on a new analyse
     *
     * @author Hugo Berthomé
     */
    override fun initNewAnalyse(): Boolean {
        scope.launch {
            nightId = nightDao.insertNight(Night(getCurrentTimestamp(), 0))
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
        scope.launch {
            if (nightId != null)
                analyseDao.insertAnalyse(Analyse(nightId!!, value.db, value.ts))
        }
    }

    /**
     * Return if an analyse is currently being saved
     *
     * @return Boolean
     * @author Hugo Berthomé
     */
    override fun isSaving(): Boolean {
        return nightId != null
    }

    /**
     * Stop the recording of the new analyse
     *
     * @author Hugo Berthomé
     */
    override fun endNewAnalyse() {
        scope.launch {
            if (nightId != null) {
                nightDao.updateNightEnd(NightUpdate(nightId!!, getCurrentTimestamp()))

                nightId = null
            }
        }
    }

    /**
     * Return the current timestamp in seconds
     *
     * @return Long timestamp
     */
    private fun getCurrentTimestamp(): Long {
        return Instant.now().epochSecond
    }
}