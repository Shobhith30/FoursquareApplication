package com.example.foursquareapplication.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.databinding.ItemFilterFeatureBinding

class FeatureAdapter(private val featureList: ArrayList<String>) :
    RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    inner class FeatureViewHolder(val featureBinding: ItemFilterFeatureBinding) :
        RecyclerView.ViewHolder(featureBinding.root) {
        init {
            featureBinding.root.setOnClickListener {
                if (featureBinding.addFeatureButton.isChecked) {

                    featureBinding.addFeatureButton.isChecked = false
                    featureBinding.featureName.setTextColor(Color.parseColor("#8D8D8D"))

                } else {
                    featureBinding.addFeatureButton.isChecked = true
                    featureBinding.featureName.setTextColor(Color.BLACK)

                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val featureBinding = ItemFilterFeatureBinding.inflate(inflater, parent, false)
        return FeatureViewHolder(featureBinding)

    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.featureBinding.featureName.text = featureList[position]


    }

    override fun getItemCount(): Int {
        return featureList.size
    }

}
