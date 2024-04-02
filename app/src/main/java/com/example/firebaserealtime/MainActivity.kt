package com.example.firebaserealtime

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaserealtime.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database

class MainActivity : AppCompatActivity(), UserClickInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var userAdapter: UserAdapter
    var list = arrayListOf<User>()
    lateinit var layoutManager: LinearLayoutManager
    var firebaseRealtime = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = LinearLayoutManager(this)
        binding.lvlistview.layoutManager = layoutManager
        userAdapter = UserAdapter(list, this)
        binding.lvlistview.adapter = userAdapter

        
        firebaseRealtime.reference.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var user = snapshot.getValue(User::class.java)
                user?.id = snapshot.key
                list.add(user?:User())
                userAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                System.out.println("snapshot ${snapshot.value}")
                list.removeIf { element-> element.id == snapshot.key }
                userAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        binding.fab.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.register_dialog)
            var etEmail = dialog.findViewById<EditText>(R.id.etEmail)
            var etPassword = dialog.findViewById<EditText>(R.id.etPassword)
            var btnRegister = dialog.findViewById<Button>(R.id.btnRegister)
            var btnupdate = dialog.findViewById<Button>(R.id.btnupdate)

            btnRegister.setOnClickListener {
                if (etEmail.text.toString().trim().isNullOrEmpty()) {
                    etEmail.error = "enter your email"
                } else if (etPassword.text.toString().trim().isNullOrEmpty()) {
                    etPassword.error = "enter your password"
                } else {
                    var user = User(etEmail.text.toString(), etPassword.text.toString())
                    firebaseRealtime.reference.push().setValue(user)
                    dialog.dismiss()
                }
            }
            btnupdate.setOnClickListener {
                if (etEmail.text.toString().trim().isNullOrEmpty()){
                    etEmail.error ="enter your email"
                }else if (etPassword.text.toString().trim().isNullOrEmpty()){
                    etPassword.error ="enter your password"
                }else{
                    var user =User(etEmail.text.toString(),etPassword.text.toString())
                    firebaseRealtime.reference.child("etEmail").setValue(user)
                    dialog.dismiss()
                }
            }


            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        }
    }

    override fun userClick(user: User) {
        firebaseRealtime.reference.child(user.id?:"").removeValue()
    }

}