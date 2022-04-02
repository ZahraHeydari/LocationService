package com.android.tinysquare.presentation.detail

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import coil.request.CachePolicy
import com.android.tinysquare.databinding.FragmentDetailBinding
import com.android.tinysquare.domain.model.ApiError
import com.android.tinysquare.domain.model.Venue
import com.android.tinysquare.presentation.util.toast
import org.koin.android.ext.android.inject

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.
    private val detailViewModel: DetailViewModel by inject()
    private var venue: Venue? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        arguments?.apply {
            if (containsKey(Venue::class.java.name))
                venue = getParcelable(Venue::class.java.name)
        }

        with(detailViewModel) {
            venue?.id?.let { getVenueDetail(it) }

            venueDetail.observe(viewLifecycleOwner, Observer {

                binding.detailNameTextView.text = it.name
                binding.detailCategoryTextView.text = it.getCategoryLabel()

                binding.detailImageView.load(it.getPhoto()) {
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                binding.detailAddressTextView.text = it.getAddressLabel()
                binding.detailRatingTextView.text = it.getVenueRating().toString()
                binding.detailRatingTextView.setBackgroundColor(Color.parseColor("#${it?.getVenueRatingColor()}"))
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                binding.detailProgressBar.visibility = if (it) View.VISIBLE else View.GONE
            })

            networkError.observe(viewLifecycleOwner, Observer { error ->
                onErrorHappened(error)
            })
        }

        return binding.root
    }


    private fun onErrorHappened(apiError: ApiError) {
        context?.toast(apiError.getErrorMessage())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //To avoid memory leak
    }

    companion object {
        val FRAGMENT_NAME : String = DetailFragment::class.java.simpleName
    }
}