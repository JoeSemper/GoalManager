package com.joesemper.goalmanager.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.joesemper.goalmanager.errors.NoAuthException
import com.joesemper.goalmanager.model.Goal

private const val GOALS_COLLECTION = "goals"
//private const val USERS_COLLECTION = "users"

const val TAG = "FireStoreDatabase"

class FireStoreDatabaseProvider : DataProvider {

    private val db = FirebaseFirestore.getInstance()
    private val result = MutableLiveData<List<Goal>>()
    private val goalsReference = db.collection(GOALS_COLLECTION)

//    private val currentUser: FirebaseUser?
//        get() = FirebaseAuth.getInstance().currentUser

    private var subscribedOnDb = false

    override fun observeGoals(): LiveData<List<Goal>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result
    }

    override fun addOrReplaceGoal(newGoal: Goal): LiveData<Result<Goal>> {
        val result = MutableLiveData<Result<Goal>>()

        goalsReference
            .document(newGoal.id.toString())
            .set(newGoal)
            .addOnSuccessListener {
                Log.d(TAG, "Note $newGoal is saved")
                result.value = Result.success(newGoal)
            }
            .addOnFailureListener {
                Log.d(TAG, "Error saving note $newGoal, message: ${it.message}")
                result.value = Result.failure(it)
            }

        return result
    }

//    override fun addOrReplaceGoal(newGoal: Goal) {
//        handleGaolsReference(
//            {
//                getUserGaolsCollection()
//                    .document(newGoal.id.toString())
//                    .set(newGoal)
//                    .addOnSuccessListener {
//                        Log.d(TAG, "Note $newGoal is saved")
//                    }
//                    .addOnFailureListener{
//                        Log.e(TAG, "Error saving goal $newGoal")
//                    }
//            } , {
//                Log.e(TAG, "Error getting reference goal $newGoal")
//            }
//        )
//    }

    private fun subscribeForDbChanging() {
        goalsReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e(TAG, "Observe goal exception:$e")
            } else if (snapshot != null) {
                val notes = mutableListOf<Goal>()

                for (doc: QueryDocumentSnapshot in snapshot) {
                    notes.add(doc.toObject(Goal::class.java))
                }

                result.value = notes
            }
        }

        subscribedOnDb = true
    }

//    private fun subscribeForDbChanging() {
//        handleGaolsReference(
//            {
//                getUserGaolsCollection().addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.e(TAG, "Observe goal exception $e")
//                    } else if (snapshot != null) {
//                        val goals = mutableListOf<Goal>()
//
//                        for (doc: QueryDocumentSnapshot in snapshot) {
//                            goals.add(doc.toObject(Goal::class.java))
//                        }
//
//                        result.value = goals
//                    }
//                }
//
//                subscribedOnDb = true
//            }, {
//                Log.e(TAG, "Error getting reference while subscribed on goals")
//            }
//        )
//    }

//    private fun getUserGaolsCollection() = currentUser?.let {
//        db.collection(USERS_COLLECTION).document(it.uid).collection(GOALS_COLLECTION)
//    } ?: throw NoAuthException()

//    private inline fun handleGaolsReference(
//        referenceHandler: (CollectionReference) -> Unit,
//        exceptionHandler: (Throwable) -> Unit = {}
//    ) {
//        kotlin.runCatching {
//            getUserGaolsCollection()
//        }
//            .fold(referenceHandler, exceptionHandler)
//    }

}