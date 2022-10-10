package dk.bollsjen.wantedcats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bollsjen.wantedcats.databinding.FragmentLoginBinding
import dk.bollsjen.wantedcats.loginController.AuthenticationLogin
import dk.bollsjen.wantedcats.models.CatsViewModel
import dk.bollsjen.wantedcats.models.LoginInfo
import dk.bollsjen.wantedcats.repositories.Singleton


class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(auth.currentUser != null) {
            //binding.loginErrorText.text = Firebase.auth.currentUser?.email
            findNavController().navigate(R.id.action_login_to_createCat)
        }

        binding.loginBtn.setOnClickListener{
            val email: String = binding.loginUsernameField.text.toString()
            val password: String = binding.loginPasswordField.text.toString()

            /*val info: LoginInfo = Singleton.getUserByInfo(LoginInfo(0,email,password))

            if(info != LoginInfo(0,email,password) && info.id != 0) {
                Singleton.loginToken = info
                findNavController().navigate(LoginDirections.actionLoginToCreateCat(info.id))
            }
            else { binding.loginErrorText.setText("Wrong username or password") }*/
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_login_to_createCat)
                }
            }
        }

        binding.loginSignupBtn.setOnClickListener{
            findNavController().navigate(LoginDirections.actionLoginToSignup())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}