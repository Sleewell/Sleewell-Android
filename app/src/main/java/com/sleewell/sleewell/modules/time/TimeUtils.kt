package com.sleewell.sleewell.modules.time

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.lang.Exception

class TimeUtils {
    companion object {
        /**
         * Return the current timestamp in seconds
         *
         * @return Long timestamp
         */
        fun getCurrentTimestamp(): Long {
            return Instant.now().epochSecond
        }

        /**
         * Return the hour of a timestamp between 0 and 24 hours
         *
         * @param ts timestamp
         * @return Long hours
         */
        @SuppressLint("SimpleDateFormat")
        fun getHourFromTimestampFromLocalTimezone(ts: Long): Long {
            try {
                val cal = Calendar.getInstance()
                val tz = cal.timeZone

                /* date formatter in local timezone */
                val sdf = SimpleDateFormat("HH")
                sdf.timeZone = tz

                val localTime = sdf.format(Date(ts * 1000))
                return localTime.toLong()
            } catch (e: Exception) {
            }
            return -1
        }

        /**
         * Converts timestamp to given format
         *
         * @param ts
         * @param format
         * @return String
         * @author Hugo Berthomé
         */
        @SuppressLint("SimpleDateFormat")
        fun convertTimestampToDate(ts: Long, format: String = "yyyyMMdd"): String {
            val cal = Calendar.getInstance()
            val tz = cal.timeZone

            /* date formatter in local timezone */
            val sdf = SimpleDateFormat(format)
            sdf.timeZone = tz

            return sdf.format(Date(ts * 1000))
        }

        /**
         * Get the night date from timestamp
         * If timestamp is in late night, it will give the previous Date of the day
         *
         * @param ts
         * @return String yyyyMMdd
         */
        @SuppressLint("SimpleDateFormat")
        fun getNightDateFromTimeStamp(ts: Long): String {
            val format = "yyyyMMdd"
            val cal = Calendar.getInstance()
            val tz = cal.timeZone

            cal.time = Date(ts * 1000)
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            if (hours in 0..8) {
                cal.add(Calendar.DAY_OF_YEAR, -1)
            }

            /* date formatter in local timezone */
            val sdf = SimpleDateFormat(format)
            sdf.timeZone = tz

            return sdf.format(cal.time)
        }
    }
}