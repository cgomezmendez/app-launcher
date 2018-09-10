package me.cristiangomez.launcher.util.validator

import android.content.Context
import me.cristiangomez.launcher.R

class RequiredValidator : Validator<String> {

    override fun shouldStop(): Boolean {
        return true
    }

    override fun validate(input: String, context: Context): Boolean {
        errorMessage = context.getString(R.string.error_required)
        return input.isNotBlank()
    }

    override var errorMessage: String? = null
}