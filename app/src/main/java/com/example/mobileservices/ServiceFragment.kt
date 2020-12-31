package com.example.mobileservices

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.mobileservices.databinding.FragmentServiceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ServiceFragment : Fragment() {

    lateinit var binding: FragmentServiceBinding

    val list = ArrayList<String>()

    private val IMAGE_PICK_CONSTANT = 2

    private val CAMERA_CAPTURE = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.fragment_service, container, false)
        binding = FragmentServiceBinding.bind(v)

        retrieveSpinnerAdapterData()


        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2)

        binding.cameraImageView.setOnClickListener {
            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                flags =
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION

                resolveActivity(activity?.packageManager!!)?.apply {
                    putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        createFileAndGetUri()?.apply {
                            list.add(this.toString())
//                            Toast.makeText(context, "uri is $this", Toast.LENGTH_SHORT).show()
                        }
                    )

                }


            }
            startActivityForResult(intent, CAMERA_CAPTURE)
        }

        binding.gallaryImageView.setOnClickListener {

            var intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    flags =
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }

            }
            startActivityForResult(intent, IMAGE_PICK_CONSTANT)
        }

        binding.submitButton.setOnClickListener {

            if (isRequiredFieldsSelected()) {
                if (list.size == 3)

                    (activity as MainActivity).loadFragment(DeviceDetailsFragment())
                else
                    Toast.makeText(context, "Please select exactly 3 images", Toast.LENGTH_SHORT)
                        .show()
            } else
                Toast.makeText(context, "Please select all the details", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun isRequiredFieldsSelected() =
        (binding.brandSpinner.selectedItemPosition != binding.brandSpinner.adapter.count - 1
                && binding.problemSpinner.selectedItemPosition != binding.problemSpinner.adapter.count - 1
                && binding.modelSpinner.selectedItemPosition != binding.modelSpinner.adapter.count - 1)


    private fun createFileAndGetUri(): Uri? {
        try {
            var file = File.createTempFile(
                "repair",
                Date().time.toString() + ".jpg",
                context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )

            return FileProvider.getUriForFile(
                context!!,
                activity!!.packageName + ".fileprovider",

                file
            )
        } catch (e: Exception) {
            Toast.makeText(context, "exception in create file camera", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return null
    }

    private fun retrieveSpinnerAdapterData() {

        retrieveBrandAdapterData()

//        retieveModelAdapterData()

//        retrieveProblemAdapterData()

    }

//    private fun retrieveProblemAdapterData() {
//        (activity as MainActivity).buildRetrofit().getProblems()
//            .enqueue(object : Callback<List<ProblemsModelClass>> {
//                override fun onResponse(
//                    call: Call<List<ProblemsModelClass>>,
//                    response: Response<List<ProblemsModelClass>>
//                ) {
//                    Log.d("faileddd", response.body().toString())
//                    if (response.isSuccessful) {
//
//
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ProblemsModelClass>>, t: Throwable) {
//                    Log.d("modelff", t.message.toString())
//                    Log.d("brandssff", call.request().body().toString())
//
//                }
//            })
//    }

    private fun setUpProblemSpinnerSelectedListener(response: List<ProblemsModelClass>) {
        binding.problemSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position < response?.size!!)
                        (activity as MainActivity).problemsModelClass.problem =
                            response?.get(position)?.problem!!

                    Log.d("checkinn3", (activity as MainActivity).problemsModelClass.problem)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }


    private fun setUpModelSpinnerSelectedListener(response: List<ModelModelClass>) {
        binding.modelSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position < response?.size!!)
                        (activity as MainActivity).problemsModelClass.modal =
                            response?.get(position)?.Modal_number!!
                    Log.d("checkinn2", (activity as MainActivity).problemsModelClass.modal)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }


    private fun setUpBrandSpinnerSelectedListener(response: List<BrandsModelClass>) {
        binding.brandSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position < response?.size!!) {
                        (activity as MainActivity).problemsModelClass.apply {
                            brand_id = response?.get(position)?.brand_id!!
                            brand_name = response?.get(position).brand_name
                        }

                    }
                    Log.d("checkinn1", (activity as MainActivity).problemsModelClass.brand_id)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

//    private fun retieveModelAdapterData() {
//        (activity as MainActivity).buildRetrofit().getModels()
//            .enqueue(object : Callback<List<ModelModelClass>> {
//                override fun onResponse(
//                    call: Call<List<ModelModelClass>>,
//                    response: Response<List<ModelModelClass>>
//                ) {
//                    Log.d("faileddd", response.toString())
//                    if (response.isSuccessful) {
//
//                    } else {
//                        Log.d("modelfffa", response.message())
//                    }
//                }
//
//                override fun onFailure(call: Call<List<ModelModelClass>>, t: Throwable) {
//
//                    Log.d("modelff", t.message.toString())
//                    Log.d("brandssff", call.request().body().toString())
//
//                }
//            })
//    }

    private fun retrieveBrandAdapterData() {
        (activity as MainActivity).buildRetrofit().getBrands(
            activity?.getPreferences(Activity.MODE_PRIVATE)
                ?.getString(Constants.USER_ID_CONSTANT, "")!!
        )
            .enqueue(object : Callback<RepairDetailsModel> {
                override fun onResponse(
                    call: Call<RepairDetailsModel>,
                    response: Response<RepairDetailsModel>
                ) {
                    Log.d("brandssss", response.raw().toString())
                    if (response.isSuccessful) {

                        val list1 = ArrayList<String>()
                        for (i in response.body()!!.data1)
                            list1.add(i.brand_name)
                        list1.add("Select Brand")
                        val adapter = ArrayAdapter<String>(
                            requireContext(),
                            R.layout.spinner_drop_down_views,
                            list1
                        )
                        binding.brandSpinner.setPopupBackgroundResource(R.color.white)
                        binding.brandSpinner.adapter = adapter

                        binding.brandSpinner.setSelection(list1.size - 1, true)

                        setUpBrandSpinnerSelectedListener(response?.body()?.data1!!)


                        val list2 = ArrayList<String>()
                        for (i in response.body()!!.data2)
                            list2.add(i.Modal_number)
                        list2.add("Select Model Number")
                        val adapter2 = ArrayAdapter<String>(
                            requireContext(),
                            R.layout.spinner_drop_down_views,
                            list2
                        )
                        binding.modelSpinner.setPopupBackgroundResource(R.color.white)
                        binding.modelSpinner.adapter = adapter2

                        binding.modelSpinner.setSelection(list2.size - 1, true)
                        setUpModelSpinnerSelectedListener(response.body()?.data2!!)



                        Log.d("modelsucc", response.body().toString())
                        val list3 = ArrayList<String>()
                        for (i in response.body()!!.data3)
                            list3.add(i.problem)
                        list3.add("Select issue")
                        val adapter3 = ArrayAdapter<String>(
                            requireContext(),
                            R.layout.spinner_drop_down_views,
                            list3
                        )
                        binding.problemSpinner.setPopupBackgroundResource(R.color.white)

                        binding.problemSpinner.adapter = adapter3
                        binding.problemSpinner.setSelection(list3.size - 1, true)


                        setUpProblemSpinnerSelectedListener(response.body()?.data3!!)

                    }
                }

                override fun onFailure(call: Call<RepairDetailsModel>, t: Throwable) {
                    Log.d("brandssff", t.message.toString())
                    Log.d("brandssff", call.request().toString())

                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        Log.d("okk", "yess");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_CONSTANT) {

                if (data?.clipData != null)
                    for (i in 0 until data?.clipData?.itemCount!!) {

                        list.add(data?.clipData?.getItemAt(i)?.uri?.toString()!!)
                        if (list.size == 3)
                            break;

                    }
                else if (data?.data != null)
                    list.add(data?.data!!.toString())


            }

        }

        if (requestCode == CAMERA_CAPTURE) {
            if (data?.extras != null && activity != null) {

                //get the uri of the image to insert

//                var uri = activity!!.contentResolver.insert(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    ContentValues().apply {
//                        put(
//                            MediaStore.MediaColumns.DISPLAY_NAME,
//                            System.currentTimeMillis().toString().toString()
//                        )
//
//                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
//                            put(
//                                MediaStore.MediaColumns.RELATIVE_PATH,
//                                Environment.DIRECTORY_PICTURES
//                            )
//                            put(MediaStore.MediaColumns.IS_PENDING, 1)
//                        }
//
//
//                    })

                //populate the uri file with bitmap data

//                uri?.let {
//                    activity?.contentResolver?.openOutputStream(it)
//                        ?.apply {
//                            (data?.extras?.get("data") as Bitmap).compress(
//                                Bitmap.CompressFormat.JPEG,
//                                100,
//                                this
//                            )
//                        }
//                }
//                Log.d("uriiii", uri.toString());

            }


        }

        (activity as MainActivity).modelClass = ModelClass(imageUri = list)
        super.onActivityResult(requestCode, resultCode, data)
    }
}