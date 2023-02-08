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

    @Query("SELECT * FROM tododata")
    suspend fun gettAllList() : MutableList<TodoData>

    @Update
    suspend fun updateItem(todoData: TodoData)

}