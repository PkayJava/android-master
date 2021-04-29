package ${pkg}.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ${pkg}.entity.HelloEntity

@Dao
interface HelloDao {

    @Insert
    suspend fun insert(entity: HelloEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun batchInsert(entities: List<HelloEntity>): LongArray

    @Query("SELECT * FROM hello WHERE id = :id")
    suspend fun findById(id: Int): HelloEntity?

    @Query(
        """
        SELECT * FROM hello
        ORDER BY id DESC LIMIT :limit OFFSET :offset
        """
    )
    suspend fun findAll(
        offset: Int,
        limit: Int,
    ): List<HelloEntity>

    @Query(
        """
        SELECT * FROM hello
        WHERE id > :lastId
        ORDER BY id DESC LIMIT :limit
        """
    )
    suspend fun findByLastId(
        lastId: Int,
        limit: Int,
    ): List<HelloEntity>

}
