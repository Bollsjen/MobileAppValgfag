package dk.bollsjen.calculatorv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dk.bollsjen.calculatorv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var operator : String = ""
    var currentNumber : String = ""
    var sumNum : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //R.layout.activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = binding.root
        setContentView(view)

        binding.btn0.setOnClickListener {
            currentNumber = currentNumber + "0"
            writeNumber()
        }

        binding.btn1.setOnClickListener {
            currentNumber = currentNumber + "1"
            writeNumber()
        }

        binding.btn2.setOnClickListener {
            currentNumber = currentNumber + "2"
            writeNumber()
        }

        binding.btn3.setOnClickListener {
            currentNumber = currentNumber + "3"
            writeNumber()
        }

        binding.btn4.setOnClickListener {
            currentNumber = currentNumber + "4"
            writeNumber()
        }

        binding.btn5.setOnClickListener {
            currentNumber = currentNumber + "5"
            writeNumber()
        }

        binding.btn6.setOnClickListener {
            currentNumber = currentNumber + "6"
            writeNumber()
        }

        binding.btn7.setOnClickListener {
            currentNumber = currentNumber + "7"
            writeNumber()
        }

        binding.btn8.setOnClickListener {
            currentNumber = currentNumber + "8"
            writeNumber()
        }

        binding.btn9.setOnClickListener {
            currentNumber = currentNumber + "9"
            writeNumber()
        }


        binding.btnKomma.setOnClickListener {
            currentNumber = currentNumber + "."
            writeNumber()
        }

        binding.btnPlus.setOnClickListener {
            operator = "+"
            handleCalculation()
        }

        binding.btnMinus.setOnClickListener {
            operator = "-"
            handleCalculation()
        }

        binding.btnGange.setOnClickListener {
            operator = "*"
            handleCalculation()
        }

        binding.btnDividere.setOnClickListener {
            operator = "/"
            handleCalculation()
        }


        binding.btnLigmed.setOnClickListener {
            handleCalculation()
            operator = ""
        }



        binding.clearAll.setOnClickListener {
            sumNum = 0.0
            currentNumber = ""
            operator = ""
            writeNumber()
        }

        binding.remove.setOnClickListener{
            currentNumber = currentNumber.dropLast(1)
            writeNumber()
        }
    }

    private fun writeNumber(){
        binding.numberField.setText(currentNumber)
    }

    private fun handleCalculation(){
        if(currentNumber != "") {
            val number1: Double = currentNumber.toDouble()
            if (operator == "+") {
                sumNum = sumNum + number1
            } else if (operator == "-") {
                sumNum = sumNum - number1
            } else if (operator == "*") {
                sumNum = sumNum * number1
            } else if (operator == "/") {
                sumNum = sumNum / number1
            }else{
                binding.numberField.setText("OPERATOR ERROR")
            }
        }else{
            binding.numberField.setText("ERROR")
        }
        currentNumber = ""
        binding.numberField.setText(sumNum.toString())
    }
}