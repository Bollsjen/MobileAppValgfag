package dk.bollsjen.wantedcats

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dk.bollsjen.wantedcats.databinding.FragmentFirstBinding
import dk.bollsjen.wantedcats.models.CatsViewModel
import dk.bollsjen.wantedcats.models.MyAdapter
import dk.bollsjen.wantedcats.repositories.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catsViewModel.booksLiveData.observe(viewLifecycleOwner) { cats ->
            //Log.d("APPLE", "observer $books")
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = if (cats == null) View.GONE else View.VISIBLE
            if (cats != null) {
                val adapter = MyAdapter(cats) { position ->
                    val action =
                        FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
                    findNavController().navigate(action /*R.id.action_FirstFragment_to_SecondFragment*/)
                }
                // binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                var columns = 1
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    columns = 2
                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 1
                }
                binding.recyclerView.layoutManager = GridLayoutManager(this.context, columns)

                binding.recyclerView.adapter = adapter
            }

            binding.fab.setOnClickListener{
                val action1 = FirstFragmentDirections.actionFirstFragmentToCreateCat(Singleton.loginToken.id)
                val action2 = FirstFragmentDirections.actionFirstFragmentToLogin()

                if(Singleton.loginToken.id != 0){
                    findNavController().navigate(action1)
                }else{
                    findNavController().navigate(action2)
                }
            }

            //
            // Chips order by dimser
            //
            binding.orderbyMyCatsChip.setOnClickListener {
                catsViewModel.myCats(Singleton.loginToken.id)
            }

            binding.orderbyPlaceChip.setOnClickListener {
                catsViewModel.getPlace()
            }

            binding.orderbyNothingChip.setOnClickListener {
                catsViewModel.reload()
            }

            binding.searchBar.setOnQueryTextListener(object: OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    catsViewModel.getPlace(query)
                    return false
                }
            })
        }

        catsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textViewMessage.text = errorMessage
        }

        catsViewModel.reload()

        /*binding.swiperefresh.setOnRefreshListener {
            booksViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }*/

        /* binding.buttonFirst.setOnClickListener {
             findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
         }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}