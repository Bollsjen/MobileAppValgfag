package dk.bollsjen.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import dk.bollsjen.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var operator : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        //val spinner : Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_dropdown_item)
            .also{adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long){
                binding.spinner.onItemSelectedListener = this
                operator = binding.spinner.selectedItem.toString()
                calculate()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        binding.equals.setOnClickListener {
            calculate()
        }
    }

    private fun calculate(){
        if(binding.number1.text.toString() != "" && binding.number2.text.toString() != "") {
            val number1: Double = binding.number1.text.toString().toDouble()
            val number2: Double = binding.number2.text.toString().toDouble()
            var result: Double = 0.0
            if (operator == "+") {
                result = number1 + number2
            } else if (operator == "-") {
                result = number1 - number2
            } else if (operator == "x") {
                result = number1 * number2
            } else if (operator == "%") {
                result = number1 / number2
            }

            binding.result.setText(result.toString())
        }
    }
}