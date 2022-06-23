package com.example.investcalculator.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.investcalculator.App
import com.example.investcalculator.R
import com.example.investcalculator.Table
import com.example.investcalculator.api.RestClient
import com.example.investcalculator.api.dto.Coin
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class BuyFragment : Fragment(R.layout.fragment_buy) {

    var spinnerChoice = 0

    fun makeVisible() {
        view?.findViewById<LinearLayout>(R.id.searchRowContainer)?.visibility = View.VISIBLE
        view?.findViewById<Spinner>(R.id.choiceSpinner)?.visibility = View.VISIBLE
        view?.findViewById<LinearLayout>(R.id.convertContainer)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.buyButton)?.visibility = View.VISIBLE
    }

    fun makeInvisible() {
        view?.findViewById<LinearLayout>(R.id.searchRowContainer)?.visibility = View.GONE
        view?.findViewById<Spinner>(R.id.choiceSpinner)?.visibility = View.GONE
        view?.findViewById<LinearLayout>(R.id.convertContainer)?.visibility = View.GONE
        view?.findViewById<Button>(R.id.buyButton)?.visibility = View.GONE
    }

    fun getCoinBySymbol(symbol: String) {
        val filterCoin = symbol.uppercase(Locale.getDefault())
        RestClient.getReqResApi2.getCoinBySymbol(filterCoin)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val text = response.body().toString().substring(
                            response.body().toString().indexOf(
                                "$filterCoin\":{"
                            ) + 5, response.body().toString().length - 2
                        )
                        val coin = Gson().fromJson(text, Coin::class.java)
                        val price =
                            if (String.format("%.0f", coin.quote?.usd?.price?.toFloat())
                                    .toFloat() > 1.0f
                            ) {
                                String.format("%.0f", coin.quote?.usd?.price?.toFloat())
                            } else {
                                String.format("%.3f", coin.quote?.usd?.price?.toFloat())
                            }
                        displaySearch(
                            coin.name.toString(),
                            coin.symbol.toString(),
                            price,
                            coin.quote?.usd?.percent_change_24h.toString()
                        )
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("Error", t.toString())
                    displaySearch("N/A", "N/A", "N/A", "N/A")
                }
            })
    }

    fun displaySearch(name: String, symbol: String, price: String, change: String) {
        view?.findViewById<TextView>(R.id.searchName)?.text = name
        view?.findViewById<TextView>(R.id.searchSymbol)?.text = symbol
        view?.findViewById<TextView>(R.id.searchPrice)?.text = price
        if (change.contains("-")) {
            view?.findViewById<TextView>(R.id.searchChange)
                ?.setTextColor(ContextCompat.getColor(context!!, R.color.red))
            view?.findViewById<TextView>(R.id.searchChange)?.text = "$change%"
        } else {
            view?.findViewById<TextView>(R.id.searchChange)
                ?.setTextColor(ContextCompat.getColor(context!!, R.color.green))
            view?.findViewById<TextView>(R.id.searchChange)?.text = "+$change%"
        }
    }

    private fun buyCrypto() {
        App.instance.db.getStepDao().insert(
            Table(
                0,
                1,
                100.0,
                view?.findViewById<EditText>(R.id.converterEditText)?.text.toString().toDouble(),
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_buy, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val converterEditText = view.findViewById<EditText>(R.id.converterEditText)
        val convertedEditText = view.findViewById<EditText>(R.id.convertedEditText)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                searchView.isIconified = false
            }, 100)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                getCoinBySymbol(searchView.query.toString())
                makeVisible()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (searchView.query.isEmpty()) {
                    makeInvisible()
                } else {
                    makeVisible()
                    if (searchView.query.length > 2) {
                        getCoinBySymbol(searchView.query.toString())
                    }
                }
                return true
            }
        })
        searchView.setOnCloseListener {
            converterEditText.setText("")
            convertedEditText.setText("")
            makeInvisible()
            true
        }
        converterEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (converterEditText.text.isEmpty()) {
                    convertedEditText.setText("")
                } else {
                    convertedEditText.setText(
                        DecimalFormat("0.#").format(
                            converterEditText.text.toString().toFloat() * spinnerChoice
                        )
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val buyButton = view.findViewById<Button>(R.id.buyButton)
        buyButton?.setOnClickListener {
            buyCrypto()
        }
        val spinner = view.findViewById<Spinner>(R.id.choiceSpinner)
        ArrayAdapter.createFromResource(
            context!!,
            R.array.convert_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convertedEditText.setText("")
                converterEditText.setText("")
                if (spinnerChoice == 0) {
                    view?.findViewById<TextView>(R.id.converterIcon1)?.text = "C"
                    view?.findViewById<TextView>(R.id.converterIcon2)?.text = "$"
                } else {
                    view?.findViewById<TextView>(R.id.converterIcon1)?.text = "$"
                    view?.findViewById<TextView>(R.id.converterIcon2)?.text = "C"
                }
                spinnerChoice = if (position == 0) {
                    0
                } else {
                    1
                }
            }
        }
    }

}