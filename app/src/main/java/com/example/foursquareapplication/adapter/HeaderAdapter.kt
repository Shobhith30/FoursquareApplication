package com.example.foursquareapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquareapplication.databinding.PagingHeaderFooterBinding
import retrofit2.http.Header

class HeaderAdapter(val retry : ()->Unit) :LoadStateAdapter<HeaderAdapter.HeaderViewHolder>() {
    override fun onBindViewHolder(holder: HeaderAdapter.HeaderViewHolder, loadState: LoadState) {
        holder.headerBinding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState !is LoadState.Loading
            errorMessage.isVisible = loadState !is LoadState.Loading
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): HeaderAdapter.HeaderViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val headerBinding = PagingHeaderFooterBinding.inflate(inflater,parent,false)
        return HeaderViewHolder(headerBinding)

    }

    inner class HeaderViewHolder(val headerBinding :PagingHeaderFooterBinding) : RecyclerView.ViewHolder(headerBinding.root){
        init {
            headerBinding.retry.setOnClickListener {
                retry.invoke()
            }
        }
    }

}