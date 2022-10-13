package dk.bollsjen.wantedcats

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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


class CreateCat : Fragment() {
    private var _binding: FragmentCreateCatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val storage = Firebase.storage
    private var storageRef = storage.reference
    var imagesRef: StorageReference? = storageRef.child("images")
    var spaceRef = storageRef.child("images/space.jpg")

    var selectedImageUri: Uri? = null


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

            /*val theRef = storageRef.child()
            val uploadTask
            val stream = FileInputStream(File(selectedImageUri.toString()))

            uploadTask = mountainsRef.putStream(stream)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
            }*/

            val cat: Cat = Cat(0,name, desc,place,reward,userId,currentDate,"")

            catsViewModel.add(cat)
            findNavController().navigate(R.id.action_createCat_to_FirstFragment)
        }

        binding.createCatBackToCatList.setOnClickListener{
            findNavController().navigate(R.id.action_createCat_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun chooseImage(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                selectedImageUri = data?.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    //binding.createCatImage.setImageURI(selectedImageUri)
                }
            }
        }
    }
}