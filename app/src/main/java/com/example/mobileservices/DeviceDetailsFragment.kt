package com.example.mobileservices

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileservices.databinding.FragmentDeviceDetailsBinding
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class DeviceDetailsFragment : Fragment(R.layout.fragment_device_details) {
    lateinit var imagelist: List<String>
    lateinit var binding: FragmentDeviceDetailsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDeviceDetailsBinding.bind(view)

        binding.submitButton.setOnClickListener {
            binding.include.progressbar.visibility = View.VISIBLE
            var contentResolver = activity!!.contentResolver
//            var imageUrl = Uri.parse(imagelist[0])


//            var file = File(
//                context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                Date().time.toString() + "." + MimeTypeMap.getSingleton()
//                    .getExtensionFromMimeType(contentResolver.getType(imageUrl))
//            )


//            file.createNewFile()
//            var outputStream = FileOutputStream(file)
//            var byte = ByteArray(DEFAULT_BUFFER_SIZE)
//            var inputStream = contentResolver.openInputStream(imageUrl)!!
//            var i = 0
//            while ((inputStream.read(byte)).also { i = it } != -1) {
//                outputStream.write(byte, 0, i)
//            }

            var arrays: Array<MultipartBody.Part> = Array(imagelist.size) { i: Int ->

                var inputStream = contentResolver.openInputStream(Uri.parse(imagelist[i]))!!
                var name = "imageFile" + (i + 1)
                var filename = Date().time.toString() + "." + MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(Uri.parse(imagelist[i])))

                MultipartBody.Part.createFormData(
                    name, filename, RequestBody.create(
                        MediaType.parse("image/*"), inputStream.readBytes()
                    )
                )


            }




            (activity as MainActivity).buildRetrofit()
                .bookRepair(
                    (activity as MainActivity).problemsModelClass.apply {
                        activity!!.getPreferences(
                            MODE_PRIVATE
                        ).apply {

                            userid = getString(Constants.USER_ID_CONSTANT, "")!!
                            user_name = getString(Constants.USER_NAME_CONSTANT, "")!!
                        }

                    },

                    arrays
                ).apply {


                    enqueue(object :
                        Callback<RepairBookingModelClass> {
                        override fun onResponse(
                            call: Call<RepairBookingModelClass>,
                            response: Response<RepairBookingModelClass>
                        ) {

                            binding.include.progressbar.visibility = View.GONE
                            if (response.isSuccessful) {

                                showDialog(view)
                            } else {

                            }

                        }

                        override fun onFailure(call: Call<RepairBookingModelClass>, t: Throwable) {
                            binding.include.progressbar.visibility = View.GONE
                            Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })

                }
        }

        setUpViewData()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpViewData() {
        var model = (activity as MainActivity).problemsModelClass
        binding.include.apply {
            customerNameTextView.text =
                activity?.getPreferences(MODE_PRIVATE)
                    ?.getString(Constants.USER_NAME_CONSTANT, "No UserName")

            mobileNumberTextView.text = activity?.getPreferences(MODE_PRIVATE)
                ?.getString(Constants.MOBILE_NUMBER_CONSTANT, "No Mobile Number")

            modelTextView.text = model.modal

            problemTextView.text = model.problem

            brandTextView.text = model.brand_id
        }

    }

    private fun showDialog(view: View) {
        var alertDialog = AlertDialog.Builder(context).create()
        alertDialog.window?.decorView?.rootView?.setBackgroundColor(resources.getColor(android.R.color.transparent))
        alertDialog.apply {
            var dialogView =
                LayoutInflater.from(view.context).inflate(R.layout.dialog_layout, null, false)
            setView(dialogView)
            dialogView.findViewById<MaterialButton>(R.id.button2).setOnClickListener {
                dismiss()
                (activity as MainActivity).loadFragment(ReviewFragment())
            }

        }.show()
        Log.d("heighttt", alertDialog.window?.decorView?.rootView?.height.toString())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        imagelist = (activity as MainActivity).modelClass.imageUri


        var recycler = binding.include.imageRecycler
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        var adapter = ImageAdapter()
        recycler.adapter = adapter
        adapter.submitList(imagelist!!)

        super.onActivityCreated(savedInstanceState)
    }

}