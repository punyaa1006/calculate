package com.example.calculater

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Vector

class MainActivity : AppCompatActivity() {

    //ประกาศตัวแปร
    private lateinit var display: TextView // แสดงผลตัวเลข/แสดงผลลัพธ์
    private var current = ""//ค่าปัจจุบันที่กำลังพิมพ์
    private var operator = "" //เครื่องหมายที่เราเลือก(+,-,*,/)
    private var firstNumber = 0.0 //ตัวเลขแรก
    private var secondNumber = 0.0 //ตัวเลขที่2
    private var result = 0.0 //
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //เชื่องdisplayเข้ากับlayout
        display = findViewById(R.id.txtdisplay)
        //ตัวแปรเก็บตัวเลข
        val numberButton = listOf(
            R.id.btnNum0,
            R.id.btnNum1,
            R.id.btnNum2,
            R.id.btnNum3,
            R.id.btnNum4,
            R.id.btnNum5,
            R.id.btnNum6,
            R.id.btnNum7,
            R.id.btnNum8,
            R.id.btnNum9,
        )
        for (id in numberButton){
            findViewById<Button>(id).setOnClickListener {
                numberClicked((it as Button).text.toString())
            }
        }

        //ปุ่มเครื่องหมายพื้นฐาน
        findViewById<Button>(R.id.btnplus).setOnClickListener {operatorClicked("+") }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {operatorClicked("-") }
        findViewById<Button>(R.id.btntimes).setOnClickListener {operatorClicked("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener {operatorClicked("/") }
        findViewById<Button>(R.id.btnMod).setOnClickListener {operatorClicked("%") }

        //ปุ่มเครื่องหมายพิเศษ
        findViewById<Button>(R.id.btnequal).setOnClickListener {calculateResult() }
        findViewById<Button>(R.id.btnClear).setOnClickListener {clearAll() }
        findViewById<Button>(R.id.btndeciml).setOnClickListener {adddot() }
        findViewById<Button>(R.id.btnplusminus).setOnClickListener {toggleSign() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {backSpace() }

    }
    //เมื่อกดปุ่มตัวเลข
    private fun numberClicked(value: String){
        if (isNewOperation){
            display.text = ""
            isNewOperation = false
        }

        current += value //เพิ่มค่าตัวเลขที่ต่อท้าย
        display.text = current //ให้แสดง่าบนหน้าจอ
    }
    //เมื่อกดเครื่องหมาย
    private fun operatorClicked(op: String){
        if (current.isNotEmpty()) {
            firstNumber = current.toDouble()
            operator = op
            current = ""//เคลียร์พื้นที่หน้าจอเพื่อรอรับตัวเลขถัดไป
        }
    }
    //เมื่อกดปุ่ม= (คำนวณ)
    private fun calculateResult(){
        if(current.isNotEmpty()){
            secondNumber = current.toDouble()
            result = when(operator){
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "*" -> firstNumber * secondNumber
                "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
                "%" -> if (secondNumber != 0.0) firstNumber % secondNumber else Double.NaN
                else -> 0.0
            }
            //ถ้าหารด้วย 0 แสดงข้อความ error
            display.text = if (result.isNaN())"Can nt devided by zero" else result.toString()
            current = result.toString()
            isNewOperation = true
        }
    }
    //เมื่อต้องการเคลียข้อมูล
    private fun clearAll(){
        current = ""
        operator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        result = 0.0
        display.text = "0"
        isNewOperation = true

    }
    //เพิ่มจุดทศนิยมในชุดตัวเลข
    private fun adddot(){
        if (!current.contains(",")){
            if (current.isEmpty()) current = "0"
            current += "."
            display.text = current
        }
    }
    //ปุ่มเปลี่ยนเครื่องหมาย (+) -> (-) , (*) -> (/)
    private fun toggleSign() {
        if (current.isNotEmpty() && current != "0") {
            current = if (current.startsWith("-")) {
                current.substring(1)
            } else {
                "-$current"
            }
            display.text = current
        }
    }
    //ปุ่มลบทีละตัว
    private fun backSpace(){
        if (isNewOperation) return //ถ้าจอกำลังแสดงผลลัพธ์ จะไม่ให้ทำการลบทีละตัว

        if(current.isNotEmpty()){
            current = current.dropLast(1) //ตัดตัวสุดท้ายออก 1 ทีละตัว

            if (current == "*" && current.isEmpty()) {
                current = " "
                display.text = "0"
            } else {
                display.text = current
            }
        }
   }
}