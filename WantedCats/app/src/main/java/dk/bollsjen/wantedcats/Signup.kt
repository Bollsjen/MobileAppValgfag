package dk.bollsjen.wantedcats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dk.bollsjen.wantedcats.databinding.FragmentSignupBinding
import dk.bollsjen.wantedcats.loginController.AuthenticationLogin
import dk.bollsjen.wantedcats.models.CatsViewModel
import org.jetbrains.annotations.NotNull
import dk.bollsjen.wantedcats.models.*

class Signup : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val catsViewModel: CatsViewModel by activityViewModels()
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtn.setOnClickListener{
            val email: String = binding.signupEmailField.text.toString()
            val password: String = binding.signupPasswordField.text.toString()

            mAuth?.createUserWithEmailAndPassword(email,password)?.addOnCompleteListener(
                OnCompleteListener {
                    @Override
                    fun onComplete(@NotNull task: Task<AuthResult>){
                        if(task.isSuccessful){
                            val user: User = User(email,password)

                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                FirebaseDatabase.getInstance().getReference("Users").child(it1.uid).setValue(user).addOnCompleteListener(
                                    OnCompleteListener {
                                        @Override
                                        fun onComplete(@NotNull task: Task<Void>){
                                            if(task.isSuccessful){
                                                val action =
                                                    SignupDirections.actionSignupToLogin()
                                                findNavController().navigate(action)
                                            }else {
                                                Log.d("FISKERLARS", "Det gik galt")
                                            }
                                        }
                                    })
                            }

                        }
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}