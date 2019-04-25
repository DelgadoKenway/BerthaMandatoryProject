package com.anto.berthamandatory

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.anto.berthamandatory.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun LogIn(view: View) {
        val loginText = findViewById<EditText>(R.id.LoginText)
        val passwordText = findViewById<EditText>(R.id.PasswordText)
        val check = GetUserByMail(loginText.text.toString(), this)
        if (check == null) {
            Toast.makeText(this, "No such user.", Toast.LENGTH_LONG).show()
            return
        }
        if (check.password == passwordText.text.toString()) {
            //navigation
            Log.d("USER", "proceeding to navigation")
            val intent = Intent(this, UserScreen::class.java)
            intent.putExtra(UserScreen.Logged_In_User, loginText.text.toString())
            startActivity(intent)
        } else {
            Toast.makeText(this, "Wrong pass", Toast.LENGTH_LONG).show()
            return
        }
    }

    fun RegisterClick(view: View) {
        val loginText = findViewById<EditText>(R.id.LoginText)
        val passwordText = findViewById<EditText>(R.id.PasswordText)
        var check: User? = User()
        val newUser = User()
        if (loginText.text.toString() != null) {
            check = GetUserByMail(loginText.text.toString(), this)
        }
        if (check != null) {
            Toast.makeText(this, "User already exists", Toast.LENGTH_LONG).show()
            return
        } else {
            newUser.mail = loginText.text.toString()
            newUser.password = passwordText.text.toString()
            newUser.id = GetUsers(this).size + 101
            Register(newUser)
            Log.d("USER", "new user: " + newUser.mail + "   " + newUser.id)
        }

    }

    fun Register(user: User) {

        val users = GetUsers(this)
        users.add(user)
        SaveUsers(users)
        Toast.makeText(this, "User created", Toast.LENGTH_LONG).show()
    }

    fun SaveUsers(users: ArrayList<User>) {
        val sharedPreferences = getSharedPreferences("bertha_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(users)
        editor.putString("b_users", json)
        editor.apply()
    }

    companion object {
        fun GetUserByMail(mail: String, context: Context): User? {
            val users = GetUsers(context)
            Log.d("USER", "array size:" + users.size)
            if (users != null && users.size > 0) {

                for (u in users) {
                    //Log.d("USER", "loop");
                    if (u != null) {
                        if (u.mail == mail) {
                            return u
                        }
                    }
                    //Log.d("USER", "null or empty array?");
                }

            }
            return null
        }

        fun GetUsers(context: Context): ArrayList<User> {
            val sharedPreferences = context.getSharedPreferences("bertha_pref", Context.MODE_PRIVATE)
            //SharedPreferences.Editor editor = sharedPreferences.edit();
            val type = object : TypeToken<ArrayList<User>>() {

            }.type
            val gson = Gson()
            val json = sharedPreferences.getString("b_users", null)
            var users: ArrayList<User>?
            users = gson.fromJson<ArrayList<User>>(json, type)
            if (users == null) users = ArrayList()
            return users
        }
    }
}
