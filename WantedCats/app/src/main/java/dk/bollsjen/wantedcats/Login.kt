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
import dk.bollsjen.wantedcats.databinding.FragmentLoginBinding
import dk.bollsjen.wantedcats.loginController.AuthenticationLogin
import dk.bollsjen.wantedcats.models.CatsViewModel


class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener{
            val username: String = binding.loginUsernameField.text.toString()
            val password: String = binding.loginPasswordField.text.toString()

            /*val info: LoginInfo = Singleton.getyserByInfo(LoginInfo(0,username,password))

            if(info != LoginInfo(0,username,password) && info.id != 0) {
                Singleton.loginToken = info
                findNavController().navigate(LoginDirections.actionLoginToCreateCat(info.id))
            }
            else { binding.loginErrorText.setText("Wrong username or password") }*/

            val controller: NavController = Navigation.findNavController(MainActivity.instance, R.id.FirstFragment)
            AuthenticationLogin.signIn(username,password, controller)
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