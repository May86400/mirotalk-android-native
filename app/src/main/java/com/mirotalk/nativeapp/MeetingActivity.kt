package com.mirotalk.nativeapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.PeerConnectionFactory
import org.webrtc.SurfaceViewRenderer

class MeetingActivity : AppCompatActivity() {
    private lateinit var micButton: ToggleButton
    private lateinit var cameraButton: ToggleButton
    private lateinit var callClient: MiroTalkClient

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true || permissions[Manifest.permission.RECORD_AUDIO] == true) {
            callClient.startLocalMedia()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        val room = intent.getStringExtra(EXTRA_ROOM) ?: "default-room"
        val name = intent.getStringExtra(EXTRA_NAME) ?: "Guest"
        callClient = MiroTalkClient(this, BuildConfig.MIROTALK_URL, room, name)

        micButton = findViewById(R.id.micButton)
        cameraButton = findViewById(R.id.cameraButton)
        micButton.isChecked = true
        cameraButton.isChecked = true
        micButton.setOnCheckedChangeListener { _, checked -> callClient.setMicrophoneEnabled(checked) }
        cameraButton.setOnCheckedChangeListener { _, checked -> callClient.setCameraEnabled(checked) }
        findViewById<Button>(R.id.peopleButton).setOnClickListener {
            startActivity(ParticipantsActivity.intent(this, callClient.participants))
        }
        findViewById<Button>(R.id.hangupButton).setOnClickListener { finish() }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionRequest.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO))
        } else {
            callClient.startLocalMedia()
        }
    }

    override fun onDestroy() {
        callClient.close()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_ROOM = "room"
        const val EXTRA_NAME = "name"
    }
}