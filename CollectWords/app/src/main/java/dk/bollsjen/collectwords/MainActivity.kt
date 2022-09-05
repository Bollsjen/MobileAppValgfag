package dk.bollsjen.collectwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.bollsjen.collectwords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val words = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        binding.saveWordBtn.setOnClickListener {
            var text : String = binding.inputText.text.toString()
            words.add(text)
            binding.inputText.setText("")
        }

        binding.clearWordsBtn.setOnClickListener {
            words.clear()
        }

        binding.showWordsBtn.setOnClickListener {
            binding.showWordsText.setText(words.toString())
        }
    }
}