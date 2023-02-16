package com.yasinsenel.yapacaklarm.room

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.yasinsenel.yapacaklarm.model.TodoData

@Dao
interface TodoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(todoData: TodoData)

    @Delete
    suspend fun deleteItem(todoData: TodoData)

    @Query("SELECT * FROM tododata WHERE userId = :userId")
    suspend fun gettAllList(userId : String) : MutableList<TodoData>

    @Update
    suspend fun updateItem(todoData: TodoData)

}