package com.sleewell.sleewell.modules.audio.audioAnalyser.dataManager

import com.sleewell.sleewell.modules.audio.audioAnalyser.model.AnalyseValue

interface IAnalyseDataManager {

    // INFO read operations

    /**
     * List all the analyses register on the device
     *
     * @return Array of all the available analyse by their timestamp
     * @author Hugo Berthomé
     */
    fun getAvailableAnalyse()

    /**
     * Read an analyse
     *
     * @param id identifying the analyse
     * @author Hugo Berthomé
     */
    fun readAnalyse(id: Long)

    /**
     * Delete an analyse
     *
     * @param id identifying the analyse
     * @author Hugo Berthomé
     */
    fun deleteAnalyse(id: Long)

    // INFO write operations

    /**
     * Init the registration on a new analyse
     *
     * @author Hugo Berthomé
     */
    fun initNewAnalyse() : Boolean

    /**
     * Add a value to the analyse
     *
     * @param value to add
     * @author Hugo Berthomé
     */
    fun addToAnalyse(value : AnalyseValue)

    /**
     * Return if an analyse is currently being saved
     *
     * @return Boolean
     * @author Hugo Berthomé
     */
    fun isSaving() : Boolean

    /**
     * Stop the recording of the new analyse
     *
     * @author Hugo Berthomé
     */
    fun endNewAnalyse()
}