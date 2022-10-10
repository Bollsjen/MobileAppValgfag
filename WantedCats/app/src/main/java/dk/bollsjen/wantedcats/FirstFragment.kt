package dk.bollsjen.wantedcats

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.databinding.FragmentFirstBinding
import dk.bollsjen.wantedcats.models.CatsViewModel
import dk.bollsjen.wantedcats.models.MyAdapter
import dk.bollsjen.wantedcats.repositories.*
import dk.bollsjen.wantedcats.models.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()


    var dateClicks = 0
    var placeClicks = 0
    var nameClicks = 0
    var rewardClicks = 0

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catsViewModel.catsLiveData.observe(viewLifecycleOwner) { cats ->
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

            binding.fab.setOnClickListener {
                val action1 =
                    FirstFragmentDirections.actionFirstFragmentToCreateCat(Singleton.loginToken.id)
                val action2 = FirstFragmentDirections.actionFirstFragmentToLogin()

                if (Singleton.loginToken.id != 0) {
                    findNavController().navigate(action1)
                } else {
                    findNavController().navigate(action2)
                }
            }
        }

        //
        // Chips order by dimser
        //
        catsViewModel.sortPlace = 0
        catsViewModel.sortDate = 0
        catsViewModel.sortReward = 0
        catsViewModel.sortName = 0
        if(!binding.showFilterChip.isChecked) {
            binding.rewardFilter.visibility = View.GONE
        }

        var lowerLimit: Int = catsViewModel.getRewardLowerLimit()
        var upperLimit: Int = catsViewModel.getRewardUpperLimit()


        binding.showFilterChip.setOnClickListener {
            if(binding.showFilterChip.isChecked){
                binding.rewardFilter.visibility = View.VISIBLE
                binding.rewardTitleText.visibility = View.VISIBLE

                binding.rewardTitleText.text = "By reward"
                binding.rewardCheckmark.visibility = View.GONE
                binding.rewardUpperLimitField.visibility = View.GONE
                binding.rewardLowerLimitField.visibility = View.GONE
            }else{
                binding.rewardFilter.visibility = View.GONE
            }
        }

        binding.rewardFilter.setOnClickListener {
            if(binding.rewardTitleText.visibility == View.VISIBLE){
                binding.rewardTitleText.visibility = View.GONE
                binding.rewardCheckmark.visibility = View.VISIBLE
                binding.rewardUpperLimitField.visibility = View.VISIBLE
                binding.rewardLowerLimitField.visibility = View.VISIBLE
            }else{
                binding.rewardTitleText.text = "By reward"
                binding.rewardTitleText.visibility = View.VISIBLE
                binding.rewardCheckmark.visibility = View.GONE
                binding.rewardUpperLimitField.visibility = View.GONE
                binding.rewardLowerLimitField.visibility = View.GONE
            }
        }


        binding.showFilterChip.setOnClickListener {
            if(binding.showFilterChip.isChecked){
                binding.rewardFilter.visibility = View.VISIBLE
                binding.rewardTitleText.visibility = View.VISIBLE

                binding.rewardTitleText.text = "By reward"
                binding.rewardCheckmark.visibility = View.GONE
                binding.rewardUpperLimitField.visibility = View.GONE
                binding.rewardLowerLimitField.visibility = View.GONE
            }else{
                binding.rewardFilter.visibility = View.GONE
            }
        }

        binding.rewardFilter.setOnClickListener {
            if(binding.rewardTitleText.visibility == View.VISIBLE){
                binding.rewardTitleText.visibility = View.GONE
                binding.rewardCheckmark.visibility = View.VISIBLE
                binding.rewardUpperLimitField.visibility = View.VISIBLE
                binding.rewardLowerLimitField.visibility = View.VISIBLE
            }else{
                binding.rewardTitleText.text = "By reward"
                binding.rewardTitleText.visibility = View.VISIBLE
                binding.rewardCheckmark.visibility = View.GONE
                binding.rewardUpperLimitField.visibility = View.GONE
                binding.rewardLowerLimitField.visibility = View.GONE
            }
        }

    binding.rewardLowerLimitField.doAfterTextChanged {

        if(binding.rewardLowerLimitField.text.toString() != "") {
            lowerLimit = binding.rewardLowerLimitField.text.toString().toInt()
        }

        if(binding.rewardUpperLimitField.text.toString() != "") {
            upperLimit = binding.rewardUpperLimitField.text.toString().toInt()
        }
        catsViewModel.filterByRewards( lowerLimit, upperLimit)
    }

    binding.orderbyMyCatsChip.setOnClickListener {
        catsViewModel.myCats(Firebase.auth.currentUser?.email)
                //binding.textViewMessage.text = "Email: " + Firebase.auth.currentUser!!.email
    }

    binding.orderbyPlaceChip.setOnClickListener {
        placeClicks++
        if(placeClicks > 2){
            binding.orderbyPlaceChip.isChecked = false
            placeClicks = 0
        }else{
            binding.orderbyDateChip.isChecked = false
            binding.orderbyRewardsChip.isChecked = false
            binding.orderbyPlaceChip.isChecked = true
            binding.orderbyNameChip.isChecked = false

            dateClicks = 0
            rewardClicks = 0
            nameClicks = 0
        }

        catsViewModel.sortPlace = placeClicks
        catsViewModel.sortList()
    }

    binding.orderbyReset.setOnClickListener {
        catsViewModel.resetSorting(catsViewModel.repository.catsOriginalData.value!!)
        binding.orderbyDateChip.isChecked = false
        binding.orderbyRewardsChip.isChecked = false
        binding.orderbyPlaceChip.isChecked = false
        binding.orderbyNameChip.isChecked = false

        dateClicks = 0
        rewardClicks = 0
        nameClicks = 0
        placeClicks = 0
    }

    binding.orderbyNothingChip.setOnClickListener {
        binding.orderbyMyCatsChip.isChecked = false;
        catsViewModel.reload()
    }

    binding.orderbyDateChip.setOnClickListener {
        dateClicks++
        if(dateClicks > 2){
            binding.orderbyDateChip.isChecked = false
            dateClicks = 0
        }else{
            binding.orderbyDateChip.isChecked = true
            binding.orderbyRewardsChip.isChecked = false
            binding.orderbyPlaceChip.isChecked = false
            binding.orderbyNameChip.isChecked = false

                    placeClicks = 0
                    rewardClicks = 0
                    nameClicks = 0
                }

                catsViewModel.sortDate = dateClicks
                catsViewModel.sortList()
            }

            binding.orderbyRewardsChip.setOnClickListener {
                rewardClicks++
                if(rewardClicks > 2){
                    binding.orderbyRewardsChip.isChecked = false
                    rewardClicks = 0
                }else{
                    binding.orderbyDateChip.isChecked = false
                    binding.orderbyRewardsChip.isChecked = true
                    binding.orderbyPlaceChip.isChecked = false
                    binding.orderbyNameChip.isChecked = false

                    placeClicks = 0
                    dateClicks = 0
                    nameClicks = 0
                }

                catsViewModel.sortReward = rewardClicks
                catsViewModel.sortList()
            }

            binding.orderbyNameChip.setOnClickListener {
                nameClicks++
                if(nameClicks > 2){
                    binding.orderbyNameChip.isChecked = false
                    nameClicks = 0
                }else{
                    binding.orderbyDateChip.isChecked = false
                    binding.orderbyRewardsChip.isChecked = false
                    binding.orderbyPlaceChip.isChecked = false
                    binding.orderbyNameChip.isChecked = true

                    placeClicks = 0
                    dateClicks = 0
                    rewardClicks = 0
                }
                catsViewModel.sortName = nameClicks
                catsViewModel.sortList()
            }

            binding.searchBar.setOnQueryTextListener(object: OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    catsViewModel.getPlace(newText)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    catsViewModel.getPlace(query)
                    return false
                }
            })

            /*if(Singleton.loginToken.email == "" || Singleton.loginToken.email == null){
                binding.orderbyMyCatsChip.visibility = View.GONE
                binding.orderbyNothingChip.visibility = View.GONE
            }else{
                binding.orderbyMyCatsChip.visibility = View.VISIBLE
                binding.orderbyNothingChip.visibility = View.VISIBLE
            }*/

            if(Firebase.auth.currentUser != null){
                binding.orderbyMyCatsChip.visibility = View.VISIBLE
                binding.orderbyNothingChip.visibility = View.VISIBLE
            }else{
                binding.orderbyMyCatsChip.visibility = View.GONE
                binding.orderbyNothingChip.visibility = View.GONE
            }


        catsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textViewMessage.text = errorMessage
        }

        catsViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            catsViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }

        /* binding.buttonFirst.setOnClickListener {
             findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
         }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setChipBackgroundColor(
        checkedColor: Int = Color.RED,
        unCheckedColor: Int = Color.BLUE
    ) : ColorStateList{
        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked))

        val colors = intArrayOf(
            checkedColor,
            unCheckedColor
        )

        return ColorStateList(states,colors)
    }
}
