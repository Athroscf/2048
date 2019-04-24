package com.example.fiall.a2048

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.fiall.a2048.dataBase.BD2048
import com.example.fiall.a2048.dataBase.tablesDB.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object {
        var actualUser: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val game = BD2048.getInstance(this)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val user = User(etUsername.text.toString())
            val userLog = User(etUsername.text.toString(), etPassword.text.toString())

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
            }
            else if (user.toString().toInt() == 0){

                val alert = AlertDialog.Builder(this)

                alert.setTitle(getString(R.string.en_alertLoginTitle))
                alert.setMessage(getString(R.string.en_alertLoginMessage))

                alert.setPositiveButton(R.string.en_Yes) { _, _ ->
                    game?.getGameDao()?.saveUser(userLog)
                    Toast.makeText(this, "Registered completed.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    actualUser = username
                }
                alert.setNegativeButton(R.string.en_No) { _, _ ->
                    Toast.makeText(this, "Please enter a user to play.", Toast.LENGTH_SHORT).show()
                }

                val dialog: AlertDialog = alert.create()

                dialog.show()

            } else {

                Toast.makeText(this, "Loged In Successfully!", Toast.LENGTH_SHORT).show()

                actualUser = username
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
