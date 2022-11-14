package com.example.howlstagramin_f16.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.howlstagramin_f16.databinding.FragmentSearchBinding

class SearchFragment: Fragment() {
    private lateinit var mBinding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)

        return mBinding.root
    }
}