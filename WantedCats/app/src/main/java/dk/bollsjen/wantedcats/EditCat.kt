package dk.bollsjen.wantedcats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dk.bollsjen.wantedcats.databinding.FragmentEditCatBinding
import dk.bollsjen.wantedcats.models.CatsViewModel
import java.text.SimpleDateFormat

class EditCat : Fragment() {
    private var _binding: FragmentEditCatBinding? = null
    private val binding get() = _binding!!
    private val catsViewModel: CatsViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCatBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)
        val position = secondFragmentArgs.position
        val cat = catsViewModel[position]

        if(auth.currentUser == null){
            findNavController().popBackStack()
            findNavController().popBackStack()
            return
        }

        if (cat == null) {
            return
        }
        binding.editCatNameField.setText(cat.name)
        binding.editCatDescriptionField.setText(cat.description)
        binding.editCatPlaceField.setText(cat.place)
        binding.editCatRewardField.setText(cat.reward.toString())

        binding.editCatBackToListBtn.setOnClickListener {
            // https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        binding.editCatUpdateBtn.setOnClickListener{
            catsViewModel.update(cat)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        binding.editCatDeleteBtn.setOnClickListener {
            catsViewModel.delete(cat.id)
            findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}