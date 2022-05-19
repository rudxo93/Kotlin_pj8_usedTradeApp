package com.duran.usedtradeapp.myPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.duran.usedtradeapp.R
import com.duran.usedtradeapp.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private lateinit var binding: FragmentMyPageBinding
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMyPageBinding = FragmentMyPageBinding.bind(view)
        binding = fragmentMyPageBinding

        binding.signInOutBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            if (auth.currentUser == null) {
                // 로그인
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            successSignIn()
                        } else {
                            Toast.makeText(
                                context,
                                "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // 로그아웃
                auth.signOut()
                binding.emailEt.text.clear()
                binding.emailEt.isEnabled = true
                binding.passwordEt.text.clear()
                binding.passwordEt.isEnabled = true

                binding.signInOutBtn.text = "로그인"
                binding.signInOutBtn.isEnabled = false
                binding.signUpBtn.isEnabled = false
            }
        }

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "회원가입에 실패했습니다. 이미 가입된 이메일일 수 있습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
        }

        binding.emailEt.addTextChangedListener {
            val enable = binding.emailEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()
            binding.signUpBtn.isEnabled = enable
            binding.signInOutBtn.isEnabled = enable
        }

        binding.passwordEt.addTextChangedListener {
            val enable = binding.emailEt.text.isNotEmpty() && binding.passwordEt.text.isNotEmpty()
            binding.signUpBtn.isEnabled = enable
            binding.signInOutBtn.isEnabled = enable
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            binding.emailEt.text.clear()
            binding.emailEt.isEnabled = true
            binding.passwordEt.text.clear()
            binding.passwordEt.isEnabled = true

            binding.signInOutBtn.text = "로그인"
            binding.signInOutBtn.isEnabled = false
            binding.signUpBtn.isEnabled = false
        } else {
            binding.emailEt.setText(auth.currentUser!!.email)
            binding.emailEt.isEnabled = false
            binding.passwordEt.setText("**********")
            binding.passwordEt.isEnabled = false

            binding.signInOutBtn.text = "로그아웃"
            binding.signInOutBtn.isEnabled = true
            binding.signUpBtn.isEnabled = false
        }

    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        binding.emailEt.isEnabled = false
        binding.passwordEt.isEnabled = false
        binding.signUpBtn.isEnabled = false
        binding.signInOutBtn.text = "로그아웃"
    }

}