package com.synapse.joyers.utils

import android.content.Context
import com.hbb20.CountryCodePicker


fun showCCPDialog(context: Context, onCodeSelected: (String, String) -> Unit) {
    val ccp = CountryCodePicker(context)

    // Optional: configure default country if needed
    ccp.ccpDialogShowFlag = false
    ccp.ccpDialogShowNameCode = false
    ccp.ccpDialogShowTitle = true
    ccp.showFullName(true)

    // Listener to capture the selected country code
    ccp.setOnCountryChangeListener {
        val code = ccp.selectedCountryCodeWithPlus
        val name = ccp.selectedCountryName
        onCodeSelected(code, name)
    }

    // Trigger the CCP dialog
    ccp.launchCountrySelectionDialog()
}
