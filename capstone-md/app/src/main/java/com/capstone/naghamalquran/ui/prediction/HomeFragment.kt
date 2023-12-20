package com.capstone.naghamalquran.ui.prediction

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.naghamalquran.R
import com.capstone.naghamalquran.databinding.FragmentHomeBinding
import com.capstone.naghamalquran.ui.prediction.api.response.ApiResponse2
import com.capstone.naghamalquran.ui.prediction.api.retrofit.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var isRecording = false
    private var audioFilePath: String? = null

    private var isRecordingToastShown = false

    private lateinit var btnPlay: Button
    private lateinit var btnSave: Button // Add button declaration
    private var currentRecordingName: String? = null // Variable to store the recording name

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1
        private const val SAVE_REQUEST_CODE = 123
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnRecord: ImageButton = binding.btnRecord
//        val btnPlay: Button = binding.btnPlay
        btnSave = binding.btnSave
        btnPlay = binding.btnPlay

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

        // Menambahkan listener untuk perubahan fragment
        parentFragmentManager.addOnBackStackChangedListener {
            // Cek apakah sedang merekam, jika ya, tampilkan pesan peringatan dan kembalikan fragment
            if (isRecording && !isRecordingToastShown) {
                Toast.makeText(requireContext(), "Tidak bisa berpindah saat sedang merekam!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
                stopRecording()
                isRecordingToastShown = true
            } else {
                // Reset isRecordingToastShown ketika status recording berubah
                isRecordingToastShown = false
            }
        }


        // Set onClickListener for the "Save" button
        btnSave.setOnClickListener {
            saveRecording()
        }

        // Set initial visibility of the buttons
        updateButtonVisibility()


        // Meminta izin rekam audio
        requestPermission()

        return root
    }

    // Add this function to update the visibility of the buttons
    private fun updateButtonVisibility() {
        btnSave.visibility = if (isRecording) View.GONE else View.VISIBLE
        btnPlay.visibility = if (isRecording) View.GONE else View.VISIBLE
    }

    private fun startRecording() {
        // Reset hasil pesan dari API
        binding.resultPrediksi.text = getString(R.string.txt_hearing)
        //        val fileName = "audio_${UUID.randomUUID()}.wav"
        val fileName = "audio_${System.currentTimeMillis()}.wav"
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
        // Update button visibility
        updateButtonVisibility()

    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        isRecording = false

        // Stop rotate animation for the record button
        binding.btnRecord.clearAnimation()

        // Update button visibility
        updateButtonVisibility()

        // Setelah merekam dihentikan, kirim file audio ke API
        if (audioFilePath != null) {
            val file = File(audioFilePath!!)
            val requestFile = file.asRequestBody("audio/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            // Menampilkan ProgressBar saat mengambil respons dari API
            binding.progressBar.visibility = View.VISIBLE

            // Anda mungkin perlu mengganti "notes" dengan parameter yang sesuai
            val notes = "Catatan Pengguna"
            val notesBody = RequestBody.create("text/plain".toMediaTypeOrNull(), notes)

            // Kirim request ke API
            val apiService = ApiConfig.getApiService()
            val call = apiService.addAudio(notesBody, body)

            call.enqueue(object : Callback<ApiResponse2> {
                override fun onResponse(call: Call<ApiResponse2>, response: Response<ApiResponse2>) {
                    if (response.isSuccessful) {
                        // Tangani respons sukses di sini
                        val result = response.body()
                        binding.resultPrediksi.text = result?.message
                    } else {
                        // Tangani respons tidak sukses di sini
                        Toast.makeText(requireContext(), "Gagal mengirim rekaman", Toast.LENGTH_SHORT).show()
                        binding.resultPrediksi.text = ""
                    }

                    // Sembunyikan ProgressBar setelah mendapatkan respons dari API
                    binding.progressBar.visibility = View.GONE
                }

                override fun onFailure(call: Call<ApiResponse2>, t: Throwable) {
                    // Tangani kegagalan koneksi atau permintaan di sini
                    Toast.makeText(requireContext(), "Koneksi Internet Anda terputus", Toast.LENGTH_SHORT).show()
                    binding.resultPrediksi.text = ""

                    // Sembunyikan ProgressBar saat terjadi kegagalan
                    binding.progressBar.visibility = View.GONE
                }
            })
        }

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

    private var fileIndex = 0
    private fun saveRecording() {
        if (audioFilePath != null) {

            val recordingName = currentRecordingName ?: "audio_${fileIndex++}_nagham"
            val fileName = "$recordingName.wav"

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "audio/wav"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }

            startActivityForResult(intent, SAVE_REQUEST_CODE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SAVE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                        FileInputStream(File(audioFilePath!!)).use { inputStream ->
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (inputStream.read(buffer).also { length = it } > 0) {
                                outputStream.write(buffer, 0, length)
                            }
                        }
                        Toast.makeText(requireContext(), "Recording saved!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to save recording", Toast.LENGTH_SHORT).show()
                }
            }
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

        // Hentikan perekaman jika sedang berlangsung
        if (isRecording) {
            stopRecording()
            Toast.makeText(requireContext(), "Perekaman dihentikan karena pindah fragment", Toast.LENGTH_SHORT).show()
        }

        _binding = null

        // Pastikan untuk melepaskan sumber daya yang digunakan
        mediaRecorder?.release()
        mediaPlayer?.release()
    }
}