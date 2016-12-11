package com.github.tehras.loanapplication.data.cache

/**
 * This class will get and put the data
 */
interface LocalDataManager {

    @Throws(NoLocalDataException::class)
    fun getData(key: String): String

    fun saveData(key: String, response: String)

    fun clearAllData()

}