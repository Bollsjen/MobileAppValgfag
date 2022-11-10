package dk.bollsjen.wantedcats

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dk.bollsjen.wantedcats.databinding.FragmentCreateCatBinding
import dk.bollsjen.wantedcats.models.Cat
import dk.bollsjen.wantedcats.models.CatsViewModel
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.*


class CreateCat : Fragment() {
    private var _binding: FragmentCreateCatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val storage = Firebase.storage

    var selectedImageUri: Uri? = null
    val REQUEST_CODE = 200


    private val catsViewModel: CatsViewModel by activityViewModels()
    private val args: CreateCatArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCatBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createCatBtn.setOnClickListener{
            val name: String = binding.createCatName.text.toString()
            val desc: String = binding.createCatDescription.text.toString()
            val place: String = binding.createCatPlace.text.toString()
            val reward: Int = Integer.parseInt(binding.createCatReward.text.toString())
            val currentDate = System.currentTimeMillis() / 1000
            var userId: String? = auth.currentUser?.email

            if(userId == null){
                userId = ""
            }

            try{
                /*val storageRef = storage.reference
                val theRef = storageRef.child(UUID.randomUUID().toString()+"---wanted-cats---"+name)
                val imgFile = File(selectedImageUri.toString())
                val stream = FileInputStream(imgFile)*/
                /*val uploadTask = theRef.putStream(stream)

                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }*/

                val cat: Cat = Cat(0,name, desc,place,reward,userId,currentDate,"")

                catsViewModel.add(cat)
                findNavController().navigate(R.id.action_createCat_to_FirstFragment)
            }catch (e: Exception){
                binding.createCatErrorMessage.text = e.message
            }
        }

        binding.createCatBackToCatList.setOnClickListener{
            findNavController().navigate(R.id.action_createCat_to_FirstFragment)
        }

        binding.createCatUploadCat.visibility = View.GONE
        /*binding.createCatUploadCat.setOnClickListener{
            chooseImage()
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun chooseImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("IMAGEUPLOADDIMS", uri.toString())
                requireContext().contentResolver.openInputStream(uri).use { stream ->
                    //this is where the crash happens
                }
            }
        }else{
            binding.createCatErrorMessage.text = "ERROR";
        }
    }
}