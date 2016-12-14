package com.github.tehras.loanapplication.data.cache

import rx.Completable
import rx.Observable

/**
 * This Local Cache will fetch the data object if available
 */
interface LocalCache {

    /**
     * Get Local Cache Data
     */
    fun <T> get(key: String, clazz: Class<T>): Observable<T>

    /**
     * Replace and Save to localCache cache
     */
    fun <T> save(key: String, o: T): Observable<T>

    /**
     * Delete just a single element, for example when an error occurs
     */
    fun delete(key: String): Observable<Void>

    /**
     * Checks if Data exists already
     * @return true, the cache exists, otherwise false
     */
    fun isCached(key: String): Completable

    /**
     * Checks if the cache is expired.
     * @return true, the cache is expired, otherwise false.
     */
    fun isExpired(): Boolean

    /**
     * Evict all elements of the cache.
     */
    fun evictAll()
}