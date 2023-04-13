package com.example.midmorningsqlliteapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName:EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber:EditText
    lateinit var btnSave:Button
    lateinit var btnViews:Button
    lateinit var btnDelete:Button
    lateinit var db: SQLiteDatabase
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtIdNumber)
        btnSave = findViewById(R.id.mBtnSave)
        btnViews = findViewById(R.id.mBtnView)
        btnDelete = findViewById(R.id.mBtnDelete)
        // create a database called eMobilis
        db = openOrCreateDatabase("eMobilis", Context.MODE_PRIVATE,null)
        // create a table called user in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        // set onClick listeners to button
        btnSave.setOnClickListener {
            // receive data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            // check if the user is submitting empty fields
            if (name.isEmpty() || email.isEmpty() || idNumber.isEmpty()){
                // display an error message using the defined message functions
                message("EMPTY FIELDS!!!", "please fill all inputs")
            }else{
                // proceed to save
                db.execSQL("INSERT users VALUES('"+name+"', '"+email+"','"+idNumber+"')")
                clear()
                message("SUCCESS", "User saved successfully")
            }
        }
        btnViews.setOnClickListener {
            // use cursor to select all the methods
            var cursor = db.rawQuery("SELECT * FROM users",null)
            // check if there is any record found
            if (cursor.count == 0){
                message("NO RECORDS!!!","Sorry no user were found!!")
            }else{
                //
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                message("USERS",buffer.toString())
            }

        }
        btnDelete.setOnClickListener {
            var idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELDS!!!", "please fill all inputs")
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho=''"+idNumber+"",null)
                if (cursor.count == 0){
                    message("NO RECORD FOUND!!!", "SORRY THERE IS NO USER WITH THE PROVIDED ID")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho=''"+idNumber+"")
                    clear()
                    message("SUCCESS!!!","USERS DELETED SUCCESSFULLY")
                }
            }
        }

    }
    fun message(title:String, message:String) {
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setPositiveButton("cancel", null)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.create().show()

    }
    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")
    }
}

