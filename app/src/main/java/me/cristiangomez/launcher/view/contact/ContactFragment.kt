package me.cristiangomez.launcher.view.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.android.synthetic.main.contact_fragment.*
import me.cristiangomez.launcher.R
import me.cristiangomez.launcher.util.TextInputLayoutErrorClearer
import me.cristiangomez.launcher.util.validator.*

class ContactFragment : Fragment() {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.contact_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        // TODO: Use the ViewModel
        callUsAction.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel://1-800-542-1832")))
        }
        progressBar.hide()
        submitAction.setOnClickListener {
            if (isFormValid()) {
                progressBar.show()
                viewModel.submitForm(getFieldText(companyNameTextInputLayout).trim(),
                        getFieldText(contactNameTextInputLayout).trim(),
                        getFieldText(phoneTextInputLayout).trim(),
                        getFieldText(emailTextInputLayout).trim(),
                        getFieldText(commentTextInputLayout).trim(),
                        getSelectedServices(),
                        {
                            progressBar.hide()
                            Snackbar.make(view!!, R.string.contact_success, Snackbar.LENGTH_LONG)
                                    .show()
                            clearForm()
                        },
                        {
                            progressBar.hide()
                            Snackbar.make(view!!, R.string.contact_error, Snackbar.LENGTH_LONG)
                                    .show()
                        })
            }
        }
        companyNameTextInputLayout.editText?.addTextChangedListener(TextInputLayoutErrorClearer(
                companyNameTextInputLayout
        ))
        contactNameTextInputLayout.editText?.addTextChangedListener(TextInputLayoutErrorClearer(
                contactNameTextInputLayout
        ))
        phoneTextInputLayout.editText?.addTextChangedListener(TextInputLayoutErrorClearer(
                phoneTextInputLayout
        ))
        emailTextInputLayout.editText?.addTextChangedListener(TextInputLayoutErrorClearer(
                emailTextInputLayout
        ))
        commentTextInputLayout.editText?.addTextChangedListener(TextInputLayoutErrorClearer(
                commentTextInputLayout
        ))
        phoneTextInputLayout.editText?.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    private fun clearForm() {
        val fields = mapOf(
                Pair(companyNameTextInputLayout, listOf(RequiredValidator(), LengthValidator(2))),
                Pair(contactNameTextInputLayout, listOf(RequiredValidator(), LengthValidator(2))),
                Pair(phoneTextInputLayout, listOf(RequiredValidator(), PhoneNumberValidator())),
                Pair(emailTextInputLayout, listOf(RequiredValidator(), EmailValidator())),
                Pair(commentTextInputLayout, listOf(RequiredValidator(), LengthValidator(2)))
        )
        for (field in fields) {
            field.key.editText?.text = null
        }
        serviceResidentialCheckBox.isChecked = false
        serviceCommercialCheckBox.isChecked = false
        serviceInstitutionalCheckBox.isChecked = false
        serviceEZineCheckBox.isChecked = false
    }

    private fun isFormValid(): Boolean {
        val fields = mapOf(
                Pair(companyNameTextInputLayout, listOf(RequiredValidator(), LengthValidator(2))),
                Pair(contactNameTextInputLayout, listOf(RequiredValidator(), LengthValidator(2))),
                Pair(phoneTextInputLayout, listOf(RequiredValidator(), PhoneNumberValidator())),
                Pair(emailTextInputLayout, listOf(RequiredValidator(), EmailValidator())),
                Pair(commentTextInputLayout, listOf(RequiredValidator(), LengthValidator(2)))
        )
        return TextInputValidator(fields, requireContext()).validate()
    }

    private fun getFieldText(inputLayout: TextInputLayout): String {
        return inputLayout.editText!!.text.toString()
    }

    private fun getSelectedServices(): List<String> {
        val services = mutableListOf<String>()
        if (serviceCommercialCheckBox.isChecked) {
            services.add("Commercial+")
        }
        if (serviceEZineCheckBox.isChecked) {
            services.add("E-zine")
        }
        if (serviceInstitutionalCheckBox.isChecked) {
            services.add("Institutional+")
        }
        if (serviceResidentialCheckBox.isChecked) {
            services.add("Residential+")
        }
        return services
    }

}
