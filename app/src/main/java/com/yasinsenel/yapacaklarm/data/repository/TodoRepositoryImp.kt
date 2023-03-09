package com.yasinsenel.yapacaklarm.data.repository

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.yasinsenel.yapacaklarm.R
import com.yasinsenel.yapacaklarm.data.model.User
import com.yasinsenel.yapacaklarm.data.model.UserDocument
import com.yasinsenel.yapacaklarm.model.TodoData
import com.yasinsenel.yapacaklarm.model.UnsplashModel
import com.yasinsenel.yapacaklarm.domain.repository.TodoRepository
import com.yasinsenel.yapacaklarm.room.TodoDAO
import com.yasinsenel.yapacaklarm.service.ImagesAPI
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TodoRepositoryImp @Inject constructor(private val api : ImagesAPI, private val todoDAO: TodoDAO
                                            , private val firebaseFirestore: FirebaseFirestore, private val auth: FirebaseAuth,
                                            private val firebaseStorage : StorageReference, private val application: Application

) : TodoRepository {
    override suspend fun getImageResponse(categoryName: String): UnsplashModel {
        return api.getData(categoryName)
    }

    override suspend fun gettAllData(userId : String): MutableList<TodoData> {
        val list = todoDAO.gettAllList(userId)
        return list
    }


    override suspend fun insertItem(todoData: TodoData) {
        todoDAO.insertItem(todoData)
    }

    override suspend fun deleteItem(todoData: TodoData) {
        todoDAO.deleteItem(todoData)
    }

    override suspend fun updateItem(todoData: TodoData) {
        todoDAO.updateItem(todoData)
    }

    override suspend fun getDataFromFirestore(todoData: TodoData) : MutableList<TodoData> {
        var getList : MutableList<TodoData>? = mutableListOf()
        auth.uid?.let { uid->
            firebaseFirestore.collection("users").document(uid).get()
                .addOnSuccessListener {
                    if(it.exists()){
                        if(it.get("todoList") !=null){
                            val users: List<TodoData>? = it.toObject(UserDocument::class.java)?.todoList
                            getList = users?.toMutableList()
                            //setAdapter(getList)
                        }
                    }

                }.await()
        }

        return getList!!
    }

    override suspend fun getUserDataFromFirestore(view: View) : User {
        var getUser : User? = null
        val def = CompletableDeferred<User>()
        auth.uid?.let { uid->
            firebaseFirestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val email=it.get("email")
                    val username=it.get("name")
                    val user = User(username.toString(),email.toString())
                    def.complete(user)
                }.await()
        }

        return def.await()
    }


    override suspend fun saveDatatoFirestorage(todoData: TodoData) {
        auth.uid?.let {uid->
                if(todoData.todoImage!=null){
                    val createStorageRef = "images/${uid}/${todoData.todoName}/${todoData.todoImage?.split("/")?.last()}"
                    firebaseStorage.child(createStorageRef).putFile(todoData.todoImage!!.toUri()).await()
                }
        }

    }
    override suspend fun saveTodoDatatoFirestore(todoData: TodoData){
        auth.uid.let {uid->
            firebaseFirestore.collection("users").document(uid!!).update("todoList", FieldValue.arrayUnion(todoData))
        }.await()

    }

    override suspend fun getDataFromFireStorage(userId : String): Uri? {
        val def = CompletableDeferred<Uri>()
        firebaseStorage.child("profile-images/${userId}")
            .downloadUrl
            .addOnSuccessListener {
                def.complete(it)
            }
            .addOnFailureListener {

            }.await()
        return def.await()
    }

    override suspend fun removeTodoDatatoFirestore(todoData: TodoData){
        auth.uid.let {uid->
            firebaseFirestore.collection("users").document(uid!!).update("todoList", FieldValue.arrayRemove(todoData))
        }.await()

    }

    override suspend fun saveUserDatatoFirestore(userId:String,name: String, email: String, password : String,activity : FragmentActivity){
        auth.uid.let {uid->
            val user = hashMapOf(
                "userId" to userId,
                "name" to name,
                "email" to email,
                "password" to password
            )
            firebaseFirestore.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    val tabs = activity.findViewById<ViewPager2>(R.id.viewPager)
                    tabs?.currentItem = 0
                }
                .addOnFailureListener {
                    Toast.makeText(application,it.toString(), Toast.LENGTH_SHORT).show()
                }.await()
        }
    }
}