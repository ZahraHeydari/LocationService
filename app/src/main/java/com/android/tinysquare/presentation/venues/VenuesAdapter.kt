package com.android.tinysquare.presentation.venues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.android.tinysquare.databinding.HolderVenueItemBinding
import com.android.tinysquare.domain.model.Locale
import com.android.tinysquare.domain.model.Venue
import kotlin.properties.Delegates

class VenuesAdapter(val onVenuesItemOnClickListener: OnVenuesItemOnClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var venueList: List<Locale> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holderVenueBinding = HolderVenueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VenueViewHolder(holderVenueBinding)
    }

    override fun getItemCount(): Int = venueList.size

    private fun getItem(position: Int) = venueList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VenueViewHolder).onBind(getItem(position).venue)
    }

    private inner class VenueViewHolder(private val holderVenueBinding: HolderVenueItemBinding) :
        RecyclerView.ViewHolder(holderVenueBinding.root) {

        fun onBind(venue: Venue?) {
            if (venue == null) return
            with(holderVenueBinding) {
                venueNameTextView.text = venue.name
                venueCategoryTextView.text = venue.getCategoryLabel()
                venueDistantTextView.text = venue.getDistanceFormatted()

                venueImageView.load(venue.getIcon()) {
                    diskCachePolicy(CachePolicy.ENABLED)
                }
            }

            itemView.setOnClickListener {
                onVenuesItemOnClickListener.onItemClick(venue)
            }
        }
    }

    /**
    * To make an interaction between [VenuesFragment]
    * and [VenuesAdapter]
    * */
    interface OnVenuesItemOnClickListener {
        fun onItemClick(venue: Venue)
    }
}