package dk.bollsjen.stringmanipulation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.bollsjen.stringmanipulation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        binding.lowerCaseBtn.setOnClickListener {
            var text : String = binding.inputText.text.toString()
            text = text.lowercase()
            binding.inputText.setText(text)
        }

        binding.upperCaseBtn.setOnClickListener {
            var text : String = binding.inputText.text.toString()
            text = text.uppercase()
            binding.inputText.setText(text)
        }

        binding.capitalizeBtn.setOnClickListener {
            var text : String = binding.inputText.text.toString()
            text = text.lowercase()
            val stringBuilder = StringBuilder(text)
            stringBuilder.setCharAt(0, stringBuilder.get(0).uppercase().get(0))
            binding.inputText.setText(stringBuilder)
        }

        binding.reverseBtn.setOnClickListener {
            var text : String = binding.inputText.text.toString()
            text = text.reversed()
            binding.inputText.setText(text)
        }
    }
}