package com.capstone.naghamalquran.ui.vip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.naghamalquran.R
import com.capstone.naghamalquran.databinding.FragmentDetailBinding
import com.capstone.naghamalquran.databinding.FragmentVipBinding
import com.capstone.naghamalquran.ui.type.NaghamAdapter
import com.capstone.naghamalquran.ui.type.NaghamList

class VipFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvListNagham

        // Inisialisasi list NaghamList
        val naghamListArrayVip = mutableListOf<VipDataList>()
        val naghamTypeArrayVip = resources.getStringArray(R.array.Nagham_Type_vip)
        val naghamLongDescArrayVip = resources.getStringArray(R.array.Long_Descrtion_vip)
        val naghamShortDescArrayVip = resources.getStringArray(R.array.Short_Description_vip)
        val naghamPictArrayVip = resources.obtainTypedArray(R.array.nagham_pict_vip)

        // Isi list dengan data NaghamList
        for (i in naghamTypeArrayVip.indices) {
            val naghamList = VipDataList(
                naghamTypeArrayVip[i],
                naghamShortDescArrayVip[i],
                naghamPictArrayVip.getResourceId(i, 0),  // Menggunakan getResourceId untuk mendapatkan ID gambar
                naghamLongDescArrayVip[i]
            )
            naghamListArrayVip.add(naghamList)
        }

        // Recycle the TypedArray to avoid memory leaks
        naghamPictArrayVip.recycle()

        // Set adapter dengan list NaghamList
        val adapter = VipAdapter(naghamListArrayVip)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}