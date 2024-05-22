package org.hyperskill.secretdiary
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private val listOfNotes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("PREF_DIARY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val tv = findViewById<TextView>(R.id.tvDiary)
        val et = findViewById<EditText>(R.id.etNewWriting)
        val buttonSave = findViewById<Button>(R.id.btnSave)
        val buttonUndo = findViewById<Button>(R.id.btnUndo)

        val savedString = sharedPreferences.getString("KEY_DIARY_TEXT", "").toString()
        if (savedString.isNotEmpty()) {
            val savedList = savedString.split("\n\n").toList()
            listOfNotes.addAll(savedList)
            tv.text = listOfNotes.joinToString("\n\n")
        }

        buttonSave.setOnClickListener {
            val newWriting = et.text
            if (newWriting.isEmpty() || newWriting.isBlank()) {
                Toast.makeText(applicationContext, "Empty or blank input cannot be saved", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val currentInstant = Clock.System.now()
                val tz = TimeZone.currentSystemDefault()
                val dateTime = currentInstant.toLocalDateTime(tz).toString()
                    .replace('T', ' ').substringBefore('.')

                val newString = "$dateTime\n$newWriting"
                listOfNotes.add(0,newString)

                tv.text = listOfNotes.joinToString("\n\n")
                val textToSave = tv.text.toString()
                editor.putString("KEY_DIARY_TEXT", textToSave).apply()
                et.text.clear()
            }
        }

        buttonUndo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Remove last note")
                .setMessage("Do you really want to remove the last writing? " +
                        "This operation cannot be undone!")
                .setNegativeButton("No",null)
                .setPositiveButton("Yes") {_,_ ->
                    if (listOfNotes.isNotEmpty()) {
                        listOfNotes.removeAt(0)
                        tv.text = listOfNotes.joinToString("\n\n")
                        val textToSave = tv.text.toString()
                        editor.putString("KEY_DIARY_TEXT", textToSave).apply()
                    }
                }
                .show()
        }

        /*
            Tests for android can not guarantee the correctness of solutions that make use of
            mutation on "static" variables to keep state. You should avoid using those.
            Consider "static" as being anything on kotlin that is transpiled to java
            into a static variable. That includes global variables and variables inside
            singletons declared with keyword object, including companion object.
            This limitation is related to the use of JUnit on tests. JUnit re-instantiate all
            instance variable for each test method, but it does not re-instantiate static variables.
            The use of static variable to hold state can lead to state from one test to spill over
            to another test and cause unexpected results.
            Using mutation on static variables to keep state
            is considered a bad practice anyway and no measure
            attempting to give support to that pattern will be made.
         */
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}