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
            "Ar-Rahman Bayati (1-13)" -> R.raw.ar_rahman_bayati
            "Ar-Rahman Bayati Ashli Qorror" -> R.raw.ar_rahman_bayati_ashli_qorror
            "Ar-Rahman Bayati Ashli Nawa" -> R.raw.ar_rahman_bayati_ashli_nawa
            "Ar-Rahman Bayati Syuri" -> R.raw.ar_rahman_bayati_syuri
            "Ar-Rahman Bayati Husaini" -> R.raw.ar_rahman_bayati_husaini
            "Ar-Rahman Bayati Ashli Jawab" -> R.raw.ar_rahman_bayati_ashli_jawab
            "Ar-Rahman Bayati Syuri Jawabuljawab" -> R.raw.ar_rahman_bayati_syuri_jawabuljawab

            "Ar-Rahman Shaba (14-15)" -> R.raw.ar_rahman_shaba
            "Ar-Rahman Shaba Ashli" -> R.raw.ar_rahman_shaba_ashli
            "Ar-Rahman Jawab Shaba" -> R.raw.ar_rahman_jawab_shaba
            "Ar-Rahman Jawab Shaba Maalajam" -> R.raw.ar_rahman_jawab_shaba_maalajam
            "Ar-Rahman Jawab Shaba Maalbastanjar" -> R.raw.ar_rahman_jawab_shaba_maalbastanjar

            "Ar-Rahman Hijaz (16-18)" -> R.raw.ar_rahman_hijaz
            "Ar-Rahman Hijaz Ashli" -> R.raw.ar_rahman_hijaz_ashli
            "Ar-Rahman Hijaz Kar" -> R.raw.ar_rahman_hijaz_kar
            "Ar-Rahman Hijaz Karkur" -> R.raw.ar_rahman_hijaz_karkur

            "Ar-Rahman Rast (19-23)" -> R.raw.ar_rahman_rast
            "Ar-Rahman Rast Zunjiran" -> R.raw.ar_rahman_rast_zunjiran
            "Ar-Rahman Rast Alanawa" -> R.raw.ar_rahman_rast_alanawa

            "Ar-Rahman Sika (24-27)" -> R.raw.ar_rahman_sika
            "Ar-Rahman Sika Ashli" -> R.raw.ar_rahman_sika_ashli
            "Ar-Rahman Sika Mishri" -> R.raw.ar_rahman_sika_mishri
            "Ar-Rahman Sika Turki" -> R.raw.ar_rahman_sika_turki

            "Ar-Rahman Jiharkah (28-30)" -> R.raw.ar_rahman_jiharkah
            "Ar-Rahman Jiharkah Ashli" -> R.raw.ar_rahman_jiharkah_ashli
            "Ar-Rahman Jiharkah Jawab" -> R.raw.ar_rahman_jiharkah_jawab

            "Ar-Rahman Nahawand (31-33)" -> R.raw.ar_rahman_nahawand
            "Ar-Rahman Nahawand Ashli" -> R.raw.ar_rahman_nahawand_ashli
            "Ar-Rahman Nahawand Jawab" -> R.raw.ar_rahman_nahawand_jawab

            else -> R.raw.error // Ganti dengan file audio default atau resource lainnya
        }
    }



}