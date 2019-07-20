package com.fourteenrows.p2pcs.model.database.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class Authentication(private val database: FirebaseFirestore, private val authorizer: FirebaseAuth) {

    fun addEmailData(email: String) = database
        .collection("Emails")
        .document("all_mails")
        .update("mail_array", FieldValue.arrayUnion(email))

    fun authenticateUser(email: String, pwd: String) = authorizer
        .signInWithEmailAndPassword(email, pwd)

    fun insertUser(email: String, pwd: String) = authorizer
        .createUserWithEmailAndPassword(email, pwd)

    fun isEmailVerified() = authorizer
        .currentUser!!.isEmailVerified

    fun isEmailRegistered(email: String) = database
        .collection("Emails")
        .whereArrayContains("mail_array", email)
        .get()

    fun sendResetEmail(email: String) {
        authorizer
            .sendPasswordResetEmail(email)
    }

    fun sendResetEmailKnown(): Task<Void> {
        val email = authorizer.currentUser!!.email!!
        return authorizer.sendPasswordResetEmail(email)
    }

    fun sendVerificationEmail() {
        authorizer.currentUser!!.sendEmailVerification()
    }

    fun signOut() {
        authorizer.signOut()
    }

}