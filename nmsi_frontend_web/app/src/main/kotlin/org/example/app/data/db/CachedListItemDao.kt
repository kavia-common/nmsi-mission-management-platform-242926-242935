package org.example.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CachedListItemDao {

    @Query(
        """
        SELECT * FROM cached_list_items
        WHERE userId = :userId AND role = :role AND kind = :kind
        ORDER BY sortOrder ASC
        """,
    )
    suspend fun listForScope(userId: String, role: String, kind: String): List<CachedListItemEntity>

    @Query(
        """
        DELETE FROM cached_list_items
        WHERE userId = :userId AND role = :role AND kind = :kind
        """,
    )
    suspend fun deleteForScope(userId: String, role: String, kind: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CachedListItemEntity>)
}
