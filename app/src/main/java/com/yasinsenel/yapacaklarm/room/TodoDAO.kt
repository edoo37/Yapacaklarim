package com.yasinsenel.yapacaklarm.room

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.yasinsenel.yapacaklarm.model.TodoData
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(todoData: TodoData)

    @Delete
    suspend fun deleteItem(todoData: TodoData)

    @Query("SELECT * FROM tododata WHERE userId = :userId")
    fun gettAllList(userId : String) : MutableList<TodoData>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(todoData: TodoData)

}