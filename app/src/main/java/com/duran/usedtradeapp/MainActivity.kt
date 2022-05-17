package com.duran.usedtradeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chattingFragment = ChattingFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationView)

        replaceFragment(homeFragment) // 최초 홈 프래그먼트

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    replaceFragment(homeFragment)
                }
                R.id.chatting -> {
                    replaceFragment(chattingFragment)
                }

                R.id.info -> {
                    replaceFragment(myPageFragment)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction() // 트렌젝션 : 작업을 시작한다고 알린다.
            .apply {
                replace(R.id.fmContainer, fragment)
                commit() // 트랜젝션 끝
            }
    }
}