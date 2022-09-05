package dk.bollsjen.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.bollsjen.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        binding.equals.setOnClickListener {
            val number1 : Float = binding.number1.text.toString().toFloat()
            val number2 : Float = binding.number2.text.toString().toFloat()
            val sum = number1 + number2
            binding.equalsField.setText(sum.toString())
        }
    }
}