package dk.bollsjen.wantedcats

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.OnFocusChangeListener
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
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()


    var dateClicks = 0
    var placeClicks = 0
    var nameClicks = 0
    var rewardClicks = 0

    init {
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        if(Firebase.auth.currentUser != null) {
            var login: MenuItem = menu.findItem(R.id.action_login)
            login.setVisible(false)

            var logout: MenuItem = menu.findItem(R.id.action_logout)
            logout.setVisible(true)
        }else{
            var login: MenuItem = menu.findItem(R.id.action_login)
            login.setVisible(true)

            var logout: MenuItem = menu.findItem(R.id.action_logout)
            logout.setVisible(false)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_login){
            //goToLogin()
            val action = FirstFragmentDirections.actionFirstFragmentToLogin()
            findNavController().navigate((action))
        }else if(item.itemId == R.id.action_logout){
            val action = FirstFragmentDirections.actionFirstFragmentToLogout()
            findNavController().navigate((action))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun goToLogin(){
        val action = FirstFragmentDirections.actionFirstFragmentToLogin()
        findNavController().navigate((action))
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
        }
        binding.fab.setOnClickListener {
            val action2 = FirstFragmentDirections.actionFirstFragmentToLogin()

            if (Firebase.auth.currentUser != null) {
                val action1 = FirstFragmentDirections.actionFirstFragmentToCreateCat(Firebase.auth.currentUser!!.email.toString())
                findNavController().navigate(action1)
            } else {
                findNavController().navigate(action2)
            }
        }
        catsViewModel.reload()

        //
        // Chips order by dimser
        //
        catsViewModel.sortPlace = 0
        catsViewModel.sortDate = 0
        catsViewModel.sortReward = 0
        catsViewModel.sortName = 0

        binding.filterRewardForm.visibility = View.GONE
        binding.filterPlaceForm.visibility = View.GONE
        binding.filterView.visibility = View.GONE

        var lowerLimit: Int = Int.MAX_VALUE
        var upperLimit: Int = 0

        var spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, arrayOf("All places..."))
        binding.filterPlaceSpinner.adapter = spinnerAdapter

        binding.filterCatsByPlace.setOnClickListener {
            if(binding.filterCatsByPlace.isChecked){
                binding.filterPlaceForm.visibility = View.VISIBLE
                spinnerAdapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, catsViewModel.getPlaces())
                binding.filterPlaceSpinner.adapter = spinnerAdapter
            }else{
                binding.filterPlaceForm.visibility = View.GONE
            }
        }

        binding.filterCatsByRewards.setOnClickListener {
            if(binding.filterCatsByRewards.isChecked){
                binding.filterRewardForm.visibility = View.VISIBLE
            }else{
                binding.filterRewardForm.visibility = View.GONE
            }
        }

        binding.filterCatsRewardsResetBtn.setOnClickListener{
            binding.filterRewardLowerLimit.setText(catsViewModel.getRewardLowerLimit().toString())
            binding.filterRewardUpperLimit.setText(catsViewModel.getRewardUpperLimit().toString())
            filterList()
        }

        binding.filterRewardFormSubmit.setOnClickListener {
            filterList()
        }

        binding.showFilterChip.setOnClickListener {
            if(binding.showFilterChip.isChecked){
                binding.filterView.visibility = View.VISIBLE
                binding.filterRewardLowerLimit.setText(catsViewModel.getRewardLowerLimit().toString())
                binding.filterRewardUpperLimit.setText(catsViewModel.getRewardUpperLimit().toString())
            }else{
                binding.filterView.visibility = View.GONE
                binding.filterPlaceForm.visibility = View.GONE
                binding.filterRewardForm.visibility = View.GONE

                binding.filterCatsByPlace.isChecked = false
                binding.filterCatsByRewards.isChecked = false
                binding.filterCatsByDates.isChecked = false
            }
        }

        binding.filterPlaceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(binding.filterPlaceSpinner.selectedItem.toString() != "All places..."){
                    filterList()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?){
            }
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

    binding.orderbyReset.setOnClickListener {
        binding.orderbyMyCatsChip.isChecked = false;
        catsViewModel.reload()
        Log.d("FISKERLARS1", "Reload")
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
                    if(newText != "")
                        catsViewModel.reload()//catsViewModel.getPlace(newText)
                    Log.d("FISKERLARS1", "Searchbar text change")
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    catsViewModel.getPlace(query)
                    Log.d("FISKERLARS1", "Searchbar text submit")
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
                binding.fab.visibility = View.VISIBLE
                Snackbar.make(view, "Signedin", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else{
                binding.orderbyMyCatsChip.visibility = View.GONE
                binding.orderbyNothingChip.visibility = View.GONE
                binding.fab.visibility = View.GONE
            }


        catsViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.errorText.text = errorMessage
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        }



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

    fun filterList(){
        var lower: Int = 0
        var upper: Int = 0

        var list: List<Cat> = emptyList()

        var placesDims: List<Cat> = emptyList()
        var rewardsDims: List<Cat> = emptyList()

        if(binding.filterRewardLowerLimit.text.toString() == "")
            lower = 0
        else
            lower = binding.filterRewardLowerLimit.text.toString().toInt()

        if(binding.filterRewardUpperLimit.text.toString() == "")
            upper = Int.MAX_VALUE
        else
            upper = binding.filterRewardUpperLimit.text.toString().toInt()

        rewardsDims = catsViewModel.filterByRewards(lower, upper)
        if(binding.filterPlaceSpinner.selectedItem.toString() != "All places..."){
            placesDims = catsViewModel.getPlace(binding.filterPlaceSpinner.selectedItem.toString())
        }else{
            placesDims = catsViewModel.getPlace("")
        }

        for(placeItem in placesDims){
            for(rewardItem in rewardsDims){
                if(placeItem == rewardItem){
                    list += placeItem
                }
            }
        }

        catsViewModel.repository.sortByList(list)
    }

}
