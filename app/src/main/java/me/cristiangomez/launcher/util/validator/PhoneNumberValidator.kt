package me.cristiangomez.launcher.util.validator

import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import me.cristiangomez.launcher.R

class PhoneNumberValidator : Validator<String> {
    override var errorMessage: String? = null
    var phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()

    override fun validate(input: String, context: Context): Boolean {
        errorMessage = context.getString(R.string.error_phone_invalid)
        return try {
            phoneNumberUtil.isValidNumber(phoneNumberUtil.parse(input, "ZZ"))
        } catch (ex: Exception) {
            false
        }
    }

    override fun shouldStop(): Boolean {
        return true
    }
}