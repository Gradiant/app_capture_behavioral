package com.example.capturebehavioural.framework
import android.net.Uri
import com.example.data.OnFirebase
import com.example.domain.Data
import com.example.domain.Consents
import com.example.domain.Response
import com.example.domain.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import kotlin.collections.HashMap

@ExperimentalCoroutinesApi
class FirebaseDatabase: OnFirebase {

    private val database = Firebase.database(("https://capturebehavioural-c2904-default-rtdb.europe-west1.firebasedatabase.app"))
    val myRef = database.getReference("users")

    override fun saveAudio(data: Data): Flow<Response<Any>> = callbackFlow {
        val storage = Firebase.storage
        val storageRef = storage.reference.child( "capturebehavioural/${data.sesion}/prueba.csv")

        val uploadTask = storageRef.putFile(Uri.fromFile(File(data.dataPath)))
        uploadTask.addOnSuccessListener {
            trySendBlocking(Response.Success("ok"))
        }
        .addOnFailureListener {
            trySendBlocking(Response.Error("ok"))
        }
        awaitClose()
    }

    override fun saveUserConsent(user: User): Flow<Response<Any>> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listOfUsers = arrayListOf<User>()
                    for (addressSnapshot in snapshot.children) {
                        addressSnapshot.children.forEach {
                            listOfUsers.add(User(
                                (it.value as HashMap<*, *>)["nombre"].toString(),
                                (it.value as HashMap<*, *>)["apellidos"].toString(),
                                (it.value as HashMap<*, *>)["direccion"].toString(),
                                (it.value as HashMap<*, *>)["telefono"].toString(),
                                (it.value as HashMap<*, *>)["email"].toString(),
                                (it.value as HashMap<*, *>)["fecha"].toString(),
                                (it.value as HashMap<*, *>)["numero_identificacion"].toString(),
                                Consents(consentimiento_captura = ((it.value as HashMap<*, *>)["consents"] as HashMap<*,*>)["consentimiento_captura"] as Boolean,
                                    consentimiento_voluntario = ((it.value as HashMap<*, *>)["consents"] as HashMap<*,*>)["consentimiento_captura"] as Boolean,
                                    consentimiento_publicacion = ((it.value as HashMap<*, *>)["consents"] as HashMap<*,*>)["consentimiento_captura"] as Boolean,
                                    consentimiento_administracion = ((it.value as HashMap<*, *>)["consents"] as HashMap<*,*>)["consentimiento_captura"] as Boolean,
                                    consentimiento_acceso_correcion_eliminacion = ((it.value as HashMap<*, *>)["consents"] as HashMap<*,*>)["consentimiento_captura"] as Boolean
                                ))
                            )
                        }
                    }
                    if(!listOfUsers.contains(user)) {
                        listOfUsers.add(user)
                    }

                    myRef.setValue(listOfUsers).addOnSuccessListener {
                        trySendBlocking(Response.Success("ok"))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Response.Error(error.message))
            }
        }

        database.reference.addListenerForSingleValueEvent(callback)

        awaitClose {
            myRef.removeEventListener(callback)
        }
    }

    override fun loadEmailsRegistered(): Flow<Response<Any>> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listOfEmail = arrayListOf<String>()
                    for (addressSnapshot in snapshot.children) {
                        addressSnapshot.children.forEach {
                            listOfEmail.add((it.value as HashMap<*, *>)["email"].toString())
                        }
                    }
                    trySendBlocking(Response.Success(listOfEmail))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Response.Error(error.message))
            }
        }

        database.reference.addListenerForSingleValueEvent(callback)

        awaitClose {
            myRef.removeEventListener(callback)
        }
    }
}