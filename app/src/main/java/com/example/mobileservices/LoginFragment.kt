package com.example.mobileservices

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobileservices.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.gotoRegisterButton.setOnClickListener {
            (activity as MainActivity).loadFragment(
                RegistrationFragment()
            )
        }

        binding.submitButton.setOnClickListener {
            if (areFieldsValid()) {
                binding.progressbar.visibility = View.VISIBLE
                (activity as MainActivity).buildRetrofit()
                    .login(
                        UserModel(
                            mobile_number = binding.mobileNumberEditText.text.toString(),
                            password = binding.passwordEditText.text.toString()
                        )
                    ).enqueue(object : Callback<ResponseModel> {
                        override fun onResponse(
                            call: Call<ResponseModel>,
                            response: Response<ResponseModel>
                        ) {
                            Log.d("responsee1", response.toString())
                            if (response.isSuccessful) {
                                Log.d("responsee", response.body()?.data!!.id.toString())
                                if ((response.body() as ResponseModel).status == "1") {
                                    (activity as MainActivity).apply {
                                        createSession(response.body()?.data!!)
                                        loadFragment(ServiceFragment())
                                    }

                                } else if ((response.body() as ResponseModel).status == "0") {
                                    binding.progressbar.visibility = View.GONE
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressbar.visibility = View.GONE
                                }

                            } else {
                                binding.progressbar.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "There is some problem in logging you",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                            Log.d("responsefail", call.request().toString())
                            Log.d("responsefail", t.toString())
                        }
                    })
            }

        }
    }

    private fun areFieldsValid(): Boolean {
        return (when {
            binding.mobileNumberEditText.text.toString().length != 10 -> {
                binding.mobileNumberEditText.error = "Please enter a valid phone number"
                return false
            }
            binding.mobileNumberEditText.text?.isEmpty()!! || binding.passwordEditText.text?.isEmpty()!! -> {
                Toast.makeText(
                    context,
                    "Please enter all the details",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            else -> return true


        })
    }
}