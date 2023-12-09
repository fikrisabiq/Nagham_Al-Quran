package com.capstone.naghamalquran.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.naghamalquran.R
import com.capstone.naghamalquran.databinding.FragmentDetailBinding

class NaghamFragment : Fragment() {

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
        val naghamListArray = mutableListOf<NaghamList>()
        val naghamTypeArray = resources.getStringArray(R.array.Nagham_Type)
        val naghamLongDescArray = resources.getStringArray(R.array.Long_Descrtion)
        val naghamShortDescArray = resources.getStringArray(R.array.Short_Description)
        val naghamPictArray = resources.obtainTypedArray(R.array.nagham_pict)

        // Isi list dengan data NaghamList
        for (i in naghamTypeArray.indices) {
            val naghamList = NaghamList(
                naghamTypeArray[i],
                naghamShortDescArray[i],
                naghamPictArray.getResourceId(i, 0),  // Menggunakan getResourceId untuk mendapatkan ID gambar
                naghamLongDescArray[i]
            )
            naghamListArray.add(naghamList)
        }

        // Recycle the TypedArray to avoid memory leaks
        naghamPictArray.recycle()

        // Set adapter dengan list NaghamList
        val adapter = NaghamAdapter(naghamListArray)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
