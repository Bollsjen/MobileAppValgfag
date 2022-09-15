package dk.bollsjen.collectwords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.ui.AppBarConfiguration
import dk.bollsjen.collectwords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding : ActivityMainBinding
    private val words = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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