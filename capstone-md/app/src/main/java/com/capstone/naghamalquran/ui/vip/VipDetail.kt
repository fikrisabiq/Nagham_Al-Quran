package com.capstone.naghamalquran.ui.vip

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.capstone.naghamalquran.R

class VipDetail : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var currentNaghamType: String // Menyimpan key_nagham saat ini
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagham_detail)



//            // ubah titel dan tmbh tombol back
//            val actionbar = supportActionBar
//            actionbar!!.title
//            actionbar.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.hide()

        //parce Char
        val dataNagham = intent.getParcelableExtra<VipDataList>("key_nagham")!!


        val tvDetailName = findViewById<TextView>(R.id.tv_tipe_nagham)
        val tvDetailDesc = findViewById<TextView>(R.id.tv_arab)
        val ivDetailPhoto = findViewById<ImageView>(R.id.img_item_photo)

        // Mendapatkan key_nagham dari intent
        currentNaghamType = dataNagham.nagham_type_vip
        mediaPlayer = MediaPlayer.create(this, getAudioResource(dataNagham.nagham_type_vip))


//            // Inisialisasi MediaPlayer berdasarkan key_nagham
//            mediaPlayer = MediaPlayer.create(this, getAudioResource(currentNaghamType))

//            val shareButton: Button = findViewById(R.id.share_button)

        tvDetailName.text = dataNagham.nagham_type_vip
        tvDetailDesc.text = dataNagham.long_desc_vip
        ivDetailPhoto.setImageResource(dataNagham.nagham_pict_vip)

//            // tombol share
//            shareButton.setOnClickListener {
//                val share = Intent()
//                share.action = Intent.ACTION_SEND
//                share.putExtra(
//                    Intent.EXTRA_TEXT,
//                    "This is about \n${tvDetailName.text.toString()} : \n${tvDetailDesc.text.toString()}" //supaya konversi
//                )
//                share.type = "text/plain"
//                startActivity(Intent.createChooser(share, "Share to:"))
//            }
        // Mendapatkan referensi ke tombol "Hear Me"
        val hearMeButton = findViewById<Button>(R.id.play_button)

        // Inisialisasi MediaPlayer berdasarkan key_nagham
        mediaPlayer = MediaPlayer.create(this, getAudioResource(currentNaghamType))

// Menambahkan pendengar OnCompletionListener untuk memulai ulang audio saat selesai
        mediaPlayer?.setOnCompletionListener {
            hearMeButton.text = "Play" // Ganti dengan teks yang sesuai
            mediaPlayer?.seekTo(0) // Mengatur pemutaran kembali ke awal
        }

// Menambahkan onClickListener untuk tombol "Hear Me"
        hearMeButton.setOnClickListener {
            // Memeriksa apakah MediaPlayer sedang berjalan
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                hearMeButton.text = "Dengarkan" // Ganti dengan teks yang sesuai
            } else {
                // Jika tidak sedang berjalan, mulai memutar
                mediaPlayer?.start()
                hearMeButton.text = "Pause" // Ganti dengan teks yang sesuai
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Pastikan untuk melepaskan sumber daya MediaPlayer saat Activity dihancurkan
        mediaPlayer?.release()
    }


    // tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getAudioResource(keyNagham: String): Int {
        return when (keyNagham) {
            "Full Syair" -> R.raw.tausyih_shaba_ashli_v2_a
            "Bayati Ashli Qorror" -> R.raw.tausyih_shaba_ashli_v2_a
//            "Bayati Ashli Nawa" -> R.raw.nama_file_bayati_ashli_nawa
//            "Bayati Syuri" -> R.raw.nama_file_bayati_syuri
//            "Bayati Suri Jawabuljawab" -> R.raw.nama_file_bayati_suri_jawabuljawab
//            "Bayati Husaini" -> R.raw.nama_file_bayati_husaini
            else -> R.raw.error // Ganti dengan file audio default atau resource lainnya
        }
    }


}