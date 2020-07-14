package com.example.semicolon.semi_settings

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.semicolon.R
import com.example.semicolon.databinding.SettingsRecyclerViewAdapterBinding

import com.example.semicolon.semi_settings.SettingsFragment.OnListFragmentInteractionListener
import com.example.semicolon.semi_settings.Setting.SettingItem

import kotlinx.android.synthetic.main.settings_recycler_view_adapter.view.*

/**
 * [RecyclerView.Adapter] that can display a [SettingItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class SettingsRecyclerViewAdapter(
    private val mValues: List<SettingItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item: SettingItem = v.tag as SettingItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SettingsRecyclerViewAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.settings_recycler_view_adapter, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: SettingItem = mValues[position]
        holder.mIdView.text = item.name

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.setting_title

        override fun toString(): String {
            return super.toString() + " '" + mIdView.text + "'"
        }
    }
}