package com.github.tehras.loanapplication.data.cache

import com.google.gson.Gson
import rx.Completable
import rx.Observable
import timber.log.Timber
import javax.inject.Inject


class LocalCacheImpl @Inject constructor(private val localDataManager: LocalDataManager) : LocalCache {

    /**
     * How long until data is considered "stale"
     * This is in milliseconds
     *
     * We'll say 14 days
     */
    private val EXPIRATION_TIME = (14 * 24 * 60 * 60 * 1000).toLong()

    override fun <T> get(key: String, clazz: Class<T>): Observable<T> {
        return Observable.create { subscriber ->
            try {
                val data: T? = Gson().fromJson(localDataManager.getData(key), clazz)
                if (data != null) {
                    subscriber.onNext(data)
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(NoLocalDataException())
                }
            } catch (e: NoLocalDataException) {
                Timber.d("No Local Data Found")
                subscriber.onError(e)
            }
        }
    }

    override fun <T> save(key: String, o: T): Observable<T> {
        return Observable.create { subscriber ->
            try {
                localDataManager.saveData(key, Gson().toJson(o))
                subscriber.onCompleted()
            } catch (e: NoLocalDataException) {
                Timber.d("No Local Data Found")
                subscriber.onError(e)
            }
        }
    }

   override fun delete(key: String): Observable<Void> {
        return Observable.create { subscriber ->
            try {
                localDataManager.deleteData(key)
                subscriber.onCompleted()
            } catch (e: NoLocalDataException) {
                Timber.d("Could not delete data")
                subscriber.onError(e)
            }
        }
    }

    override fun isCached(key: String): Completable {
        //TODO implement
        return Completable.complete()
    }

    override fun isExpired(): Boolean {
        //TODO
        return false
    }

    override fun evictAll() {
        localDataManager.clearAllData()
    }

}