package com.dannir.reto1.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.dannir.reto1.R
import com.dannir.reto1.databinding.ActivityMainBinding
import com.dannir.reto1.fragments.HomeFragment
import com.dannir.reto1.fragments.ProfileFragment
import com.dannir.reto1.fragments.PublishFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  lateinit var  newHomeFragment: HomeFragment
    private  lateinit var  newPublishFragment: PublishFragment
    private  lateinit var  newProfileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavView.itemIconTintList = null

        newHomeFragment = HomeFragment.newInstance()
        newPublishFragment = PublishFragment.newInstance()
        newProfileFragment = ProfileFragment.newInstance()

        showFragment(newHomeFragment)

        binding.bottomNavView.setOnItemSelectedListener {

            when (it.itemId){
                R.id.item_home -> {
                    showFragment(newHomeFragment)
                    Log.e(">>>", it.itemId.toString())
                }

                R.id.item_publish -> {
                    showFragment(newPublishFragment)
                }

                R.id.item_profile -> {
                    showFragment(newProfileFragment)
                }
            }
            true
        }

    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainerCL.id, fragment)
        transaction.commit()
    }
}