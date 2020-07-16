package com.example.semicolon.semi_settings

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.semicolon.databinding.SettingsRecyclerViewAdapterBinding
import com.example.semicolon.semi_settings.Setting.SettingItem
import com.example.semicolon.semi_settings.view_models.SettingsFragmentViewModel

/**
 * [RecyclerView.Adapter] that can display a [SettingItem].
 */
class SettingsRecyclerViewAdapter(private val mValues: List<SettingItem>,
                                  private val viewModel: SettingsFragmentViewModel
) : RecyclerView.Adapter<SettingsRecyclerViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: SettingItem = mValues[position]

        holder.bind(item, viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: SettingsRecyclerViewAdapterBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SettingItem, viewModel: SettingsFragmentViewModel) {
            binding.settingTitle.text = item.name

            binding.linearLayout.setOnClickListener {
                viewModel.onUserClicked(item.pos)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SettingsRecyclerViewAdapterBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = mValues.size
}