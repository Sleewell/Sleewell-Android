package com.sleewell.sleewell.modules.time

import java.time.Instant

class TimeUtils {
    companion object {
        /**
         * Return the current timestamp in seconds
         *
         * @return Long timestamp
         */
        fun getCurrentTimestamp() : Long {
            return Instant.now().epochSecond
        }
    }
}