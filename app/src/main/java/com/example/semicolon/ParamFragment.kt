package com.example.semicolon

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.lang.Exception

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

/**
 * A simple [Fragment] subclass.
 * Use the [ParamFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ParamFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
            param4 = it.getInt(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_param, container, false)
        val fullName = param2
        val detailsText = param3
        val nameText = view.findViewById<TextView>(R.id.name_text)
        val image = view.findViewById<ImageView>(R.id.place_for_image)
        val detText = view.findViewById<TextView>(R.id.details_text)
        try {
            image.setImageDrawable(getDrawable(context as Context, param4 as Int))
        } catch (e : Exception) {}
        nameText.text = fullName
        detText.text = detailsText
        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @param param3 Parameter 3.
         * @return A new instance of fragment ParamFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: Int) =
            ParamFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                    putInt(ARG_PARAM4, param4)
                }
            }
    }
}