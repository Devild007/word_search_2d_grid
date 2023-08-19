package com.example.wordsearch.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.wordsearch.R
import com.example.wordsearch.databinding.ActivityMainBinding
import com.example.wordsearch.device.utility.SessionManager
import com.example.wordsearch.device.utility.dialogOk
import com.example.wordsearch.device.utility.getInt
import com.example.wordsearch.device.utility.getString
import com.example.wordsearch.device.utility.onTextChange
import com.example.wordsearch.ui.view.dialog.DialogOK

class MainActivity : AppCompatActivity(), DialogOK.IDialogOKCallback {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val session = SessionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        init()
    }

    private fun init() = binding?.apply {
        if (session.keyRow?.isNotEmpty() == true) {
            etRow.setText(session.keyRow)
            setAlphabetEditTextLenght()
        }
        if (session.keyColumn?.isNotEmpty() == true) {
            etColumn.setText(session.keyColumn)
            setAlphabetEditTextLenght()
        }
        if (session.keyAlphabets?.isNotEmpty() == true) {
            setAlphabetEditTextLenght()
            etAlphabet.setText(session.keyAlphabets)
        }

        etRow.onTextChange { setAlphabetEditTextLenght() }

        etColumn.onTextChange { setAlphabetEditTextLenght() }

        btnCreateGrid.setOnClickListener { onBtnClicked() }

        etAlphabet.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) onBtnClicked()
            false
        }

    }

    private fun onBtnClicked() = binding?.apply {
        when {
            etRow.getString().isEmpty() -> dialogOk(
                getString(R.string.attention), getString(R.string.enter_no_row_count), this@MainActivity
            )

            etColumn.getString().isEmpty() -> dialogOk(
                getString(R.string.attention), getString(R.string.enter_no_column_count), this@MainActivity
            )

            etAlphabet.getString().isEmpty() -> dialogOk(
                getString(R.string.attention), getString(R.string.enter_alphabet_in_one_go), this@MainActivity
            )

            (etRow.getInt() * etColumn.getInt()) != etAlphabet.getString().length -> dialogOk(
                getString(R.string.attention), "Alphabet's length should be ${etRow.getInt() * etColumn.getInt()} character's.",
                this@MainActivity
            )

            else -> {
                session.keyAlphabets = etAlphabet.getString()
                session.keyRow = etRow.getString()
                session.keyColumn = etColumn.getString()
                startActivity(Intent(this@MainActivity, GridWordActivity::class.java))
                finish()
            }
        }
    }

    private fun setAlphabetEditTextLenght() = binding?.apply {
        val row = if (etRow.getString().isNotEmpty()) etRow.getInt() else 0
        val column = if (etColumn.getString().isNotEmpty()) etColumn.getInt() else 0
        val length = row * column
        etAlphabet.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    override fun closeDialog() {
        //nothing to do here at the moment
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
