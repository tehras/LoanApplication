package com.github.tehras.loanapplication.data.cache

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocalDataManagerImpl @Inject constructor(val sharedPreferences: SharedPreferences) : LocalDataManager {

    companion object {
        val DATA_CACHE_KEY = "data_cache_private"
    }

    @Throws(NoLocalDataException::class)
    override fun getData(key: String): String {
        val data = sharedPreferences.getString(key, "") ?: throw NoLocalDataException()
        if (data.isNullOrEmpty())
            throw NoLocalDataException()

        return data
    }

    override fun saveData(key: String, response: String) {
        sharedPreferences.edit().putString(key, response).putLong(key + "_timestamp", System.currentTimeMillis()).apply()
    }

    override fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }

}