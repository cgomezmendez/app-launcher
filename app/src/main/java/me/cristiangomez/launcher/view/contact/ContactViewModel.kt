package me.cristiangomez.launcher.view.contact

import androidx.lifecycle.ViewModel;
import me.cristiangomez.launcher.network.ContactServiceFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class ContactViewModel : ViewModel() {
    val contactService = ContactServiceFactory.getService()
    var isSubmitInProgress = false
    // TODO: Implement the ViewModel

    fun submitForm(company: String,
                   contact: String,
                   phone: String,
                   email: String,
                   comment: String,
                   services: List<String>,
                   onSubmitSuccess: (() -> Unit)?,
                   onSubmitError: (() -> Unit)?) {
        if (!isSubmitInProgress) {
            isSubmitInProgress = true
            contactService.submitForm(company, contact, phone,
                    email, comment, services).enqueue(object: Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    isSubmitInProgress = false
                    onSubmitError?.invoke()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.body()?.string() == "Invalid code. Please hit the back button and try again") {
                        onSubmitError?.invoke()
                    } else {
                        onSubmitSuccess?.invoke()
                    }
                    isSubmitInProgress = false
                }
            })
        }
    }
}
