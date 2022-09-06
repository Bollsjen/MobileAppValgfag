package dk.bollsjen.calculatorv15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import dk.bollsjen.calculatorv15.databinding.ActivityMainBinding

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
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val spinner: Spinner = findViewById(R.id.spinner)
                spinner.onItemSelectedListener = this
                operator = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.equals.setOnClickListener {
            calculate()
        }
    }

    private fun calculate(){
        val number1 : Double = binding.number1.text.toString().toDouble()
        val number2 : Double = binding.number2.text.toString().toDouble()
        var result : Double = 0.0
        if(operator == "+"){
            result = number1 + number2
        }else if(operator == "-"){
            result = number1 - number2
        }else if(operator == "x"){
            result = number1 * number2
        }else if(operator == "%"){
            result = number1 / number2
        }

        binding.result.setText(result.toString())
    }
}