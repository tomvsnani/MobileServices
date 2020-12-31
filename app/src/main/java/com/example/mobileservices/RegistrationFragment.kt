package com.example.mobileservices

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.mobileservices.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding
    var userCreationLiveData: MutableLiveData<Boolean> = MutableLiveData()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_registration, container, false)
        binding = FragmentRegistrationBinding.bind(v)

        binding.submitButton.setOnClickListener {

            if (areFieldsValid()) {
                var model =
                    UserModel(
                        binding.firstnameEditText.text.toString(),
                        binding.lastnameEditText.text.toString(),
                        binding.emailIdEditText.text.toString(),
                        binding.mobileNumberEditText.text.toString(),
                        password = binding.passwordEditText.text.toString()
                    )
                startRegistration(model)


            }
        }

        return v
    }

    private fun startRegistration(model: UserModel) {
        binding.progressbar.visibility=View.VISIBLE
        (activity as MainActivity).buildRetrofit()
            .register(
//                model.first_name,
//                model.last_name,
//                model.mobile_number,
//                model.email,
//                model.password
            model
            )

            .enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    Log.d("success", response.toString())
                    binding.progressbar.visibility=View.GONE
                    if (response.isSuccessful) {
                        if ((response.body() as ResponseModel).status == "1")
                            (activity as MainActivity).loadFragment(ServiceFragment())
                        else if ((response.body() as ResponseModel).status == "0")
                            Toast.makeText(
                                context,
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()

                    }
                    else{
                        Toast.makeText(
                            context,
                            "There is some problem in registering you",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    binding.progressbar.visibility=View.GONE
                    Toast.makeText(
                        context,
                        "There is some problem in registering you",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("failedd", t.message.toString())
                }
            })


    }

    private fun areFieldsValid(): Boolean {
        if (binding.emailIdEditText.text.toString()
                .isEmpty() || binding.mobileNumberEditText.text.toString().isEmpty() ||
            binding.firstnameEditText.text.toString()
                .isEmpty() || binding.lastnameEditText.text.toString().isEmpty() ||
            binding.passwordEditText.text.toString()
                .isEmpty()

        ) {
            Toast.makeText(context, "Please enter all the details", Toast.LENGTH_SHORT).show()
            return false
        } else {
            when {
                !binding.emailIdEditText.text.toString()
                    .contains("com") || !binding.emailIdEditText.text.toString()
                    .contains("@") -> {

                    binding.emailIdEditText.error = "Please enter valid email"
                    return false
                }


                binding.mobileNumberEditText.text.toString().length != 10 -> {
                    binding.mobileNumberEditText.error =
                        "Please enter valid phone number"
                    return false
                }
                else -> return true
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}