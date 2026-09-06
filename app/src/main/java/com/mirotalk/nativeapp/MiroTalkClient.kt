package com.mirotalk.nativeapp

import android.content.Context
import org.json.JSONObject
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.PeerConnectionFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import io.socket.client.IO
import io.socket.client.Socket

data class Participant(val id: String, val name: String)

class MiroTalkClient(
    context: Context,
    private val serverUrl: String,
    private val room: String,
    private val name: String
) {
    val participants = mutableListOf(Participant("local", name))
    private val socket: Socket = IO.socket(serverUrl)
    private val factory: PeerConnectionFactory
    private var capturer: CameraVideoCapturer? = null
    private var audioTrack: AudioTrack? = null
    private var videoTrack: VideoTrack? = null
    private var audioSource: AudioSource? = null
    private var videoSource: VideoSource? = null

    init {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions()
        )
        factory = PeerConnectionFactory.builder().createPeerConnectionFactory()
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("join", JSONObject().apply {
                put("channel", room)
                put("peer_name", name)
                put("peer_uuid", "android-$name")
                put("peer_audio", true)
                put("peer_video", true)
                put("peer_audio_status", true)
                put("peer_video_status", true)
            })
        }
        socket.connect()
    }

    fun startLocalMedia() {
        audioSource = factory.createAudioSource(org.webrtc.MediaConstraints())
        audioTrack = factory.createAudioTrack("audio-local", audioSource)
        val enumerator = Camera2Enumerator(null)
        val cameraName = enumerator.deviceNames.firstOrNull { enumerator.isFrontFacing(it) }
        if (cameraName != null) {
            capturer = enumerator.createCapturer(cameraName, null)
            videoSource = factory.createVideoSource(capturer!!.isScreencast)
            capturer!!.initialize(null, null, videoSource!!.capturerObserver)
            capturer!!.startCapture(1280, 720, 30)
            videoTrack = factory.createVideoTrack("video-local", videoSource)
        }
    }

    fun setMicrophoneEnabled(enabled: Boolean) { audioTrack?.setEnabled(enabled) }
    fun setCameraEnabled(enabled: Boolean) { videoTrack?.setEnabled(enabled) }

    fun close() {
        capturer?.stopCapture()
        capturer?.dispose()
        audioTrack?.dispose()
        videoTrack?.dispose()
        audioSource?.dispose()
        videoSource?.dispose()
        socket.disconnect()
        socket.off()
        factory.dispose()
    }
}