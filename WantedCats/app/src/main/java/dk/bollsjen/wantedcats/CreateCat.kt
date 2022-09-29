package dk.bollsjen.wantedcats

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dk.bollsjen.wantedcats.databinding.FragmentCreateCatBinding
import dk.bollsjen.wantedcats.models.Cat
import dk.bollsjen.wantedcats.models.CatsViewModel
import dk.bollsjen.wantedcats.models.MyAdapter
import java.text.SimpleDateFormat

class CreateCat : Fragment() {
    private var _binding: FragmentCreateCatBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createCatCreatBtn.setOnClickListener{
            val name: String = binding.createCatName.text.toString()
            val desc: String = binding.createCatDescription.text.toString()
            val place: String = binding.creatCatPlace.text.toString()
            val reward: Int = Integer.parseInt(binding.createCatReward.text.toString())
            val currentDate = System.currentTimeMillis() / 1000

            val cat: Cat = Cat(0,name, desc,place,reward,reward,currentDate,"")

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