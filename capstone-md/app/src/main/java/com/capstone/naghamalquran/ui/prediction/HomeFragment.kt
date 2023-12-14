package com.capstone.naghamalquran.ui.prediction

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.naghamalquran.databinding.FragmentHomeBinding
import java.io.IOException
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isRecording = false
    private var audioFilePath: String? = null

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnRecord: ImageButton = binding.btnRecord
        val btnPlay: Button = binding.btnPlay

        // Setiap kali tombol rekam ditekan
        btnRecord.setOnClickListener {
            if (!isRecording) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        // Setiap kali tombol putar ditekan
        btnPlay.setOnClickListener {
            playAudio()
        }

        // Meminta izin rekam audio
        requestPermission()

        return root
    }

    private fun startRecording() {
        val fileName = "audio_${UUID.randomUUID()}.wav"
        audioFilePath = "${requireContext().externalCacheDir?.absolutePath}/$fileName"

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFilePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        isRecording = true

//        // Rotate animation for the record button
//        val rotateAnimation = RotateAnimation(
//            0f,
//            360f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        ).apply {
//            duration = 1000
//            repeatCount = Animation.INFINITE
//        }
//
//        binding.btnRecord.startAnimation(rotateAnimation)

        // Scale animation for the record button
        val scaleAnimation = ScaleAnimation(
            1f,
            1.2f,
            1f,
            1.2f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = 500 // Ubah durasi animasi sesuai kebutuhan
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }

        binding.btnRecord.startAnimation(scaleAnimation)

    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        isRecording = false

        // Stop rotate animation for the record button
        binding.btnRecord.clearAnimation()

    }

    private fun playAudio() {
        if (audioFilePath != null) {
            mediaPlayer = MediaPlayer().apply {
                setOnPreparedListener {
                    // Jalankan audio setelah persiapan selesai
                    start()
                }

                setOnErrorListener { mp, what, extra ->
                    // Tangani kesalahan pemutaran
                    stop()
                    release()
                    // Tampilkan pesan kesalahan kepada pengguna
                    // contoh:
                    Toast.makeText(requireContext(), "Error playing audio", Toast.LENGTH_SHORT).show()
                    true
                }

                setDataSource(audioFilePath)
                prepareAsync() // Persiapkan secara asynchronous
            }
        } else {
            // Tampilkan pesan kepada pengguna bahwa tidak ada rekaman yang tersedia
            // contoh:
        Toast.makeText(requireContext(), "No recorded audio available", Toast.LENGTH_SHORT).show()
        }
    }


    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Pastikan untuk melepaskan sumber daya yang digunakan
        mediaRecorder?.release()
        mediaPlayer?.release()
    }
}