package com.android.tinysquare.data.source.local.base.dao

import androidx.room.*
import com.android.tinysquare.domain.model.Locale
import kotlinx.coroutines.delay


/**
 * Provides access to [Locale] underlying database
 * */
@Dao
interface LocaleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Locale>?) : List<Long>?

    @Query("SELECT * FROM Locale")
    suspend fun loadAll(): List<Locale>?

    @Query("DELETE FROM Locale")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAllThenInsertNewLocales(list: List<Locale>?) : List<Long>? {
        deleteAll()
        delay(10)
        return list?.let { return insertAll(it) }
    }

}