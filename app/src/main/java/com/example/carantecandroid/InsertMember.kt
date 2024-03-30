package com.example.carantecandroid

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
        val modelRequest = ViewModelProvider(this)[ModelRequest::class.java]

        binding = ActivityInsertMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.goBackButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf(resources.getString(R.string.adult), resources.getString(R.string.child))
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.pricing.adapter = adapter
        }

        binding.addButton.setOnClickListener {
            if (binding.licence.text.isEmpty() ||
                binding.name.text.isEmpty() ||
                binding.surname.text.isEmpty() ||
                binding.date.text.isEmpty() ||
                binding.password.text.isEmpty())
                return@setOnClickListener

            val isoFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            //Add in both API and DDB

            Thread {
                val newMember = Member(
                    licence = binding.licence.text.toString(),
                    name = binding.name.text.toString(),
                    surname = binding.surname.text.toString(),
                    dives = 99,
                    date = binding.date.text.toString(),
                    subdate = isoFormatter.format(Date()),
                    pricing = binding.pricing.selectedItem.toString()
                )
                modelRequest.base.memberDAO().insertOne(newMember)
            }.start()

            modelRequest.doRequest("https://dev-sae301grp5.users.info.unicaen.fr/api/members?" +
                                    "licence=" + binding.licence.text + "&" +
                                    "name=" + binding.name.text + "&" +
                                    "surname=" + binding.surname.text + "&" +
                                    "date_certification=" + binding.date.text + "&" +
                                    "pricing=" + binding.pricing.selectedItem.toString() + "&" +
                                    "password=" + binding.password.text + "&" +
                                    "subdate=" + isoFormatter.format(Date()), "POST")

            binding.licence.text.clear()
            binding.name.text.clear()
            binding.surname.text.clear()
            binding.date.text = "DATE DE CERTIFICATION"
            binding.pricing.setSelection(0)
            binding.password.text.clear()
        }

        val pickDateBtn = binding.date

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, myYear, myMonth, myDay ->
                    pickDateBtn.text = myYear.toString() + "-" + (myMonth + 1) + "-" + myDay.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}