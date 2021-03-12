package com.android.tinysquare.presentation.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.tinysquare.R
import com.android.tinysquare.databinding.ActivityMainBinding
import com.android.tinysquare.domain.model.Venue
import com.android.tinysquare.presentation.detail.DetailFragment
import com.android.tinysquare.presentation.venues.VenuesFragment
import com.android.tinysquare.presentation.util.newFragmentInstance


class MainActivity : AppCompatActivity(), OnMainActivityCallback{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(savedInstanceState == null) navigateToVenuesPage()
    }

    private fun navigateToVenuesPage() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_container,
                newFragmentInstance<VenuesFragment>(),
                VenuesFragment.FRAGMENT_NAME
            ).commit()
    }

    override fun navigateToDetail(venue: Venue) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.main_container,
                newFragmentInstance<DetailFragment>(Pair(Venue::class.java.name, venue)),
                DetailFragment.FRAGMENT_NAME
            )
            .addToBackStack(DetailFragment.FRAGMENT_NAME)
            .commit()
    }

}

