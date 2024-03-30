package com.example.carantecandroid

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.carantecandroid.databinding.ActivityInsertMemberBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InsertMember : AppCompatActivity() {
    private lateinit var binding: ActivityInsertMemberBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInsertMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.goBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.addButton.setOnClickListener {
            if (binding.licence.text.isEmpty() ||
                binding.name.text.isEmpty() ||
                binding.surname.text.isEmpty() ||
                binding.date.text.isEmpty() ||
                binding.pricing.text.isEmpty() ||
                binding.password.text.isEmpty())
                return@setOnClickListener

            val modelRequest: ModelRequest by viewModels()

            val isoFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            modelRequest.doRequest("https://dev-sae301grp5.users.info.unicaen.fr/api/members?" +
                                    "licence=" + binding.licence.text + "&" +
                                    "name=" + binding.name.text + "&" +
                                    "surname=" + binding.surname.text + "&" +
                                    "date_certification=" + binding.date.text + "&" +
                                    "pricing=" + binding.pricing.text + "&" +
                                    "password=" + binding.password.text + "&" +
                                    "subdate=" + isoFormatter.format(Date()), "POST")
        }

        val pickDateBtn = binding.date

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, myMonth, myDay ->
                    pickDateBtn.text = year.toString() + "-" + (myMonth + 1) + "-" + myDay.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}