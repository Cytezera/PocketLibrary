package com.example.pocketlibrary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.pocketlibrary.home.HomeBodyFragment
import com.example.pocketlibrary.internalDatabase.SyncManager
import androidx.lifecycle.lifecycleScope
import com.example.pocketlibrary.home.HomeContainerFragment
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        lifecycleScope.launch{
            SyncManager.syncWithFirebase(this@MainActivity)
        }
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_container, NavigationFragment())
                .replace(R.id.main_container, HomeContainerFragment())
                .commit()
        }
    }
}