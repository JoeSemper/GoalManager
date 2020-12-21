package com.joesemper.goalmanager.data.db

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.joesemper.goalmanager.errors.NoAuthException
import com.joesemper.goalmanager.model.Goal
import com.joesemper.goalmanager.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val GOALS_COLLECTION = "goals"
private const val USERS_COLLECTION = "users"

const val TAG = "FireStoreDatabase"

class FireStoreDatabaseProvider : DataProvider {

    private val db = FirebaseFirestore.getInstance()
    private val result = MutableStateFlow<List<Goal>?>(null)

    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    private var subscribedOnDb = false

    override fun getCurrentUser(): User? {
        return currentUser?.run { User(displayName, email) }
    }

    override fun observeGoals(): Flow<List<Goal>> {
        if (!subscribedOnDb) subscribeForDbChanging()
        return result.filterNotNull()
    }

    override suspend fun deleteGoal(goalId: String) {
        suspendCancellableCoroutine<Unit> { continuation ->
            getUserGoalsCollection()
                .document(goalId)
                .delete()
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(Unit))
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun addOrReplaceGoal(newGoal: Goal) {
        suspendCoroutine<Unit> { continuation ->
            handleGaolsReference(
                {
                    getUserGoalsCollection()
                        .document(newGoal.id.toString())
                        .set(newGoal)
                        .addOnSuccessListener {
                            Log.d(TAG, "Note $newGoal is saved")
                            continuation.resumeWith(Result.success(Unit))
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Error saving note $newGoal, message: ${it.message}")
                            continuation.resumeWithException(it)
                        }

                }, {
                    Log.e(TAG, "Error getting reference note $newGoal, message: ${it.message}")
                    continuation.resumeWithException(it)
                }
            )
        }
    }

    private fun subscribeForDbChanging() {
        handleGaolsReference(
            {
                getUserGoalsCollection().addSnapshotListener { snapshot, e ->
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
            }, {
                Log.e(TAG, "Error getting reference while subscribed for goals")
            }
        )
    }

    private fun getUserGoalsCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(GOALS_COLLECTION)
    } ?: throw NoAuthException()

    private inline fun handleGaolsReference(
        referenceHandler: (CollectionReference) -> Unit,
        exceptionHandler: (Throwable) -> Unit = {}
    ) {
        kotlin.runCatching {
            getUserGoalsCollection()
        }
            .fold(referenceHandler, exceptionHandler)
    }
}