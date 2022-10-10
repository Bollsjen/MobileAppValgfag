package dk.bollsjen.wantedcats

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.databinding.FragmentCreateCatBinding
import dk.bollsjen.wantedcats.models.Cat
import dk.bollsjen.wantedcats.models.CatsViewModel
import dk.bollsjen.wantedcats.models.MyAdapter
import dk.bollsjen.wantedcats.repositories.Singleton
import java.text.SimpleDateFormat

class CreateCat : Fragment() {
    private var _binding: FragmentCreateCatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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
}