package dk.bollsjen.wantedcats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)
        val position = secondFragmentArgs.position
        val cat = catsViewModel[position]
        if (cat == null) {
            binding.editCatName.setText("No cat was found")
            return
        }
        binding.editCatName.setText(cat.name)
        binding.editCatDescription.setText(cat.description)
        binding.editCatPlace.setText(cat.place)
        binding.editCatReward.setText(cat.reward.toString())
        val simpleDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = simpleDate.format(cat.date * 1000)
        binding.editCatDate.setText(currentDate.toString())
        binding.editCatDate.setRawInputType(0)

        binding.backToCatList.setOnClickListener {
            // findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            // https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
            findNavController().popBackStack()
        }

        binding.deleteCatBtn.setOnClickListener{
            catsViewModel.delete(cat.id)
            //findNavController().popBackStack()
        }

        /*binding.buttonDelete.setOnClickListener {
            booksViewModel.delete(book.id)
            findNavController().popBackStack()
        }*/

        /*binding.buttonUpdate.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            //val publisher = binding.editTextPublisher.text.toString().trim()
            //val author = binding.editTextAuthor.text.toString().trim()
            val price = binding.editTextPrice.text.toString().trim().toDouble()
            val updatedBook = Book(book.id, title,  price)
            Log.d("APPLE", "update $updatedBook")
            booksViewModel.update(updatedBook)
            findNavController().popBackStack()
        }*/


        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}