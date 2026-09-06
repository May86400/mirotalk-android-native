package com.mirotalk.nativeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ParticipantsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participants)
        title = "People"
        val names = intent.getStringArrayListExtra(EXTRA_NAMES).orEmpty()
        findViewById<ListView>(R.id.participantsList).adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, names.ifEmpty { arrayListOf("No other participants") }
        )
    }

    companion object {
        private const val EXTRA_NAMES = "participant_names"
        fun intent(context: Context, participants: List<Participant>): Intent =
            Intent(context, ParticipantsActivity::class.java).putStringArrayListExtra(
                EXTRA_NAMES, ArrayList(participants.map { it.name })
            )
    }
}