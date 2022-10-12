package dk.bollsjen.wantedcats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.databinding.FragmentFirstBinding
import dk.bollsjen.wantedcats.databinding.FragmentLogoutBinding

class Logout : Fragment() {

    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Firebase.auth.currentUser != null) {
            Firebase.auth.signOut()
            val action = LogoutDirections.actionLogoutToFirstFragment()
            findNavController().navigate(action)
        }else{
            val action = LogoutDirections.actionLogoutToFirstFragment()
            findNavController().navigate(action)
        }

    }
}