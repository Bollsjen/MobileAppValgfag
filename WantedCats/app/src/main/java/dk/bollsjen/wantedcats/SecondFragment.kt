package dk.bollsjen.wantedcats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import dk.bollsjen.wantedcats.databinding.FragmentSecondBinding
import dk.bollsjen.wantedcats.models.CatsViewModel
import java.text.SimpleDateFormat

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val catsViewModel: CatsViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)
        val position = secondFragmentArgs.position
        val cat = catsViewModel[position]

        if(auth.currentUser != null && cat != null){
            if(auth.currentUser!!.email == cat.userId){
                binding.editCatBtn.visibility = View.VISIBLE
            }else{
                binding.editCatBtn.visibility = View.GONE
            }
        }else{
            binding.editCatBtn.visibility = View.GONE
        }

        if (cat == null) {
            binding.showCatName.setText("No cat was found")
            return
        }
        binding.showCatName.setText(cat.name)
        binding.showCatDescription.setText(cat.description)
        binding.showCatPlace.setText(cat.place)
        binding.showCatReward.setText(cat.reward.toString())
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(cat.date * 1000)
        binding.showCatDate.setText(currentDate.toString())
        binding.showCatDate.setRawInputType(0)

        binding.backToCatList.setOnClickListener {
            // findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            // https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
            findNavController().popBackStack()
        }

        binding.editCatBtn.setOnClickListener{
            val cation = SecondFragmentDirections.actionSecondFragmentToEditCat(position)
            findNavController().navigate(cation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}