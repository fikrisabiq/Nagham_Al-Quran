package com.capstone.naghamalquran.ui.type

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.capstone.naghamalquran.R

class NaghamDetail : AppCompatActivity() {

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
            val dataNagham = intent.getParcelableExtra<NaghamList>("key_nagham")!!


            val tvDetailName = findViewById<TextView>(R.id.tv_tipe_nagham)
            val tvDetailDesc = findViewById<TextView>(R.id.tv_arab)
            val ivDetailPhoto = findViewById<ImageView>(R.id.img_item_photo)

            // Mendapatkan key_nagham dari intent
            currentNaghamType = dataNagham.nagham_type
            mediaPlayer = MediaPlayer.create(this, getAudioResource(dataNagham.nagham_type))


//            // Inisialisasi MediaPlayer berdasarkan key_nagham
//            mediaPlayer = MediaPlayer.create(this, getAudioResource(currentNaghamType))

//            val shareButton: Button = findViewById(R.id.share_button)

            tvDetailName.text = dataNagham.nagham_type
            tvDetailDesc.text = dataNagham.long_desc
            ivDetailPhoto.setImageResource(dataNagham.nagham_pict)

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
                hearMeButton.text = getString(R.string.btn_play) // Ganti dengan teks yang sesuai
                mediaPlayer?.seekTo(0) // Mengatur pemutaran kembali ke awal
            }

// Menambahkan onClickListener untuk tombol "Hear Me"
            hearMeButton.setOnClickListener {
                // Memeriksa apakah MediaPlayer sedang berjalan
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                    hearMeButton.text = getString(R.string.btn_resume) // Ganti dengan teks yang sesuai
                } else {
                    // Jika tidak sedang berjalan, mulai memutar
                    mediaPlayer?.start()
                    hearMeButton.text = getString(R.string.btn_pause) // Ganti dengan teks yang sesuai
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
            "Tausyih Bayati" -> R.raw.tausyih_bayati
            "Bayati Ashli Qorror" -> R.raw.tausyih_bayati_ashli_qorror
            "Bayati Ashli Nawa" -> R.raw.tausyih_bayati_ashli_nawa
            "Bayati Syuri" -> R.raw.tausyih_bayati_syuri
            "Bayati Husaini" -> R.raw.tausyih_bayati_husaini
            "Bayati Ashli Jawab" -> R.raw.tausyih_bayati_ashli_jawab

            "Tausyih Shaba" -> R.raw.tausyih_shaba
            "Shaba Ashli" -> R.raw.tausyih_shaba_ashli
            "Jawab Shaba" -> R.raw.tausyih_jawab_shaba
            "Jawab Shaba Maalajam" -> R.raw.tausyih_jawab_shaba_maalajam
            "Shaba Ashli 2" -> R.raw.tausyih_shaba_ashli_2
            "Jawab Shaba Maalbastanjar" -> R.raw.tausyih_jawab_shaba_maalbastanjar

            "Tausyih Hijaz" -> R.raw.tausyih_hijaz
            "Hijaz Ashli" -> R.raw.tausyih_hijaz_ashli
            "Hijaz Kar" -> R.raw.tausyih_hijaz_kar
            "Hijaz Karkur" -> R.raw.tausyih_hijaz_karkur
            "Hijaz Kur" -> R.raw.tausyih_hijaz_kur

            "Tausyih Rast" -> R.raw.tausyih_rast
            "Rast Ashli" -> R.raw.tausyih_rast_ashli
            "Rast Alanawa" -> R.raw.tausyih_rast_alanawa

            "Tausyih Sika" -> R.raw.tausyih_sika
            "Sika Ashli" -> R.raw.tausyih_sika_ashli
            "Sika Turki" -> R.raw.tausyih_sika_turki
            "Sika Mishri" -> R.raw.tausyih_sika_mishri

            "Tausyih Jiharkah" -> R.raw.tausyih_jiharkah
            "Jiharkah Ashli" -> R.raw.tausyih_jiharkah_ashli
            "Jiharkah Jawab" -> R.raw.tausyih_jiharkah_jawab
            "Jiharkah Ashli 2" -> R.raw.tausyih_jiharkah_ashli_2
            "Jiharkah Jawab 2" -> R.raw.tausyih_jiharkah_jawab_2

            "Tausyih Nahawand" -> R.raw.tausyih_nahawand
            "Nahawand Ashli" -> R.raw.tausyih_nahawand_ashli
            "Nahawand Jawab" -> R.raw.tausyih_nahawand_jawab

            else -> R.raw.error // Ganti dengan file audio default atau resource lainnya
        }
    }


}