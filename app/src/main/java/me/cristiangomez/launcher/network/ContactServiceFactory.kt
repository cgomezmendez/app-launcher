package me.cristiangomez.launcher.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object ContactServiceFactory {
    fun getService(): ContactService {
        return Retrofit.Builder()
                .baseUrl("http://trustsecurityusa.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(ContactService::class.java)
    }
}