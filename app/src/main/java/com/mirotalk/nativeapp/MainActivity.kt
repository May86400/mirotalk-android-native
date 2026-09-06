package com.mirotalk.nativeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val roomInput = findViewById<EditText>(R.id.roomInput)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        findViewById<Button>(R.id.joinButton).setOnClickListener {
            val room = roomInput.text.toString().trim().ifEmpty { "default-room" }
            val name = nameInput.text.toString().trim().ifEmpty { "Guest" }
            startActivity(Intent(this, MeetingActivity::class.java).apply {
                putExtra(MeetingActivity.EXTRA_ROOM, room)
                putExtra(MeetingActivity.EXTRA_NAME, name)
            })
        }
    }
}