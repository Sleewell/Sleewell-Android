package com.sleewell.sleewell.new_mvp.menu.home.model

import android.content.Context
import android.nfc.NfcAdapter
import com.sleewell.sleewell.new_mvp.menu.home.HomeContract

/**
 * Model for the home fragment, it will get all the information that are necessaries
 *
 * @constructor created a Home model based on the HomeContract
 * @param context context of the activity / view
 * @author Hugo Berthomé
 */
class HomeModel(context : Context) : HomeContract.Model {

    private var nfcData: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    /**
     * Get the state of the nfc module
     *
     * @return NfcState enum
     * @author Hugo Berthomé
     */
    override fun nfcState(): NfcState {
        if (nfcData == null)
            return NfcState.NotAvailable
        if (!nfcData!!.isEnabled)
            return NfcState.Disable
        return NfcState.Enable
    }
}