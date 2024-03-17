package com.kotlin.ttknpdev.fragments

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.constants.Router
import com.kotlin.ttknpdev.entity.Gadget

class SubUpdateFragment(private val gadget: Gadget) : Fragment() ,View.OnClickListener , DialogInterface.OnClickListener{

    // it is importance for access activity
    private lateinit var view: View

    // Input
    private lateinit var button: AppCompatButton
    private lateinit var textInputLayoutGadgetBrand: TextInputLayout
    private lateinit var textInputEditGadgetBrand: TextInputEditText
    private lateinit var textInputLayoutGadgetModel: TextInputLayout
    private lateinit var textInputEditGadgetModel: TextInputEditText
    private lateinit var textInputLayoutGadgetPrice: TextInputLayout
    private lateinit var textInputEditGadgetPrice: TextInputEditText

    // For request
    /*private lateinit var requestRead : JsonObjectRequest*/
    private lateinit var requestUpdate : StringRequest
    private lateinit var queue : RequestQueue

    // For changing color TextInputLayout
    private val myColors = arrayOf(
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)),intArrayOf(Color.RED)),
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)),intArrayOf(Color.GREEN))
    )

    private fun initialSubUpdateFragmentWidget() {
        button = view.findViewById(R.id.appCompatButtonSub)
        //***
        textInputLayoutGadgetBrand = view.findViewById(R.id.textInputLayoutGadgetBrand)
        textInputEditGadgetBrand = view.findViewById(R.id.textInputEditGadgetBrand)
        textInputLayoutGadgetBrand.hint = gadget.brand
        //***
        textInputLayoutGadgetModel = view.findViewById(R.id.textInputLayoutGadgetModel)
        textInputEditGadgetModel = view.findViewById(R.id.textInputEditGadgetModel)
        textInputLayoutGadgetModel.hint = gadget.model
        //***
        textInputLayoutGadgetPrice = view.findViewById(R.id.textInputLayoutGadgetPrice)
        textInputEditGadgetPrice = view.findViewById(R.id.textInputEditGadgetPrice)
        textInputLayoutGadgetPrice.hint = gadget.price.toString()
        //***
    }

/*
    private fun jsonObjectRequestReadMethod(gid: String) = object :
        JsonObjectRequest( "${Router.ROUTER_GADGET}/read/$gid",
            Response.Listener { response ->
                // *** GSON library to deserialize or parse a JSON array to a Java array or List object.
                val gson = Gson()
                val gadget = gson.fromJson(response.toString(), Gadget::class.java)
                textInputLayoutGadgetBrand.hint = gadget.brand
                textInputLayoutGadgetModel.hint = gadget.model
                textInputLayoutGadgetPrice.hint = gadget.price.toString()
            },
            Response.ErrorListener { error ->
                Log.e("RESPONSE ERROR (onErrorResponse)", error!!.message.toString())
                // throw p0 // if you need to exit app after found err Just throw it
                myAlertFailed()
            }) {
        // this block can override function of JsonObjectRequest
    }
*/

    // request url look like /update/G00N?brand=Y1&model=Y2...
    private fun stringRequestUpdateMethod(gadget: Gadget) = object :
        StringRequest( Request.Method.PUT,"${Router.ROUTER_GADGET}/update/${gadget.gid}",
            Response.Listener { response ->
                Log.d("DEBUG",response.toString()) // be only true or false
                if (response.toString() == "true") {
                    myAlertSuccess()
                }
            },
            Response.ErrorListener { error ->
                Log.e("RESPONSE ERROR (onErrorResponse)", error!!.message.toString())
                throw error // if you need to exit app after found err Just throw it
            }) {
        override fun getHeaders(): MutableMap<String, String> { // Map @RequestParam annotation
            val params = hashMapOf<String, String>()
            params["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8"
            return params
        }
        override fun getParams(): MutableMap<String, String>? {
            val mapRequest = hashMapOf<String, String>()
            mapRequest["brand"] = gadget.brand
            mapRequest["model"] = gadget.model
            mapRequest["price"] = gadget.price.toString()
            return mapRequest
        }
    }

/*
    private fun myAlertFailed() {
        // Create the object of AlertDialog Builder class
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        // Set the message show for the Alert time
        alertDialogObject!!.setMessage("ID ${gadget.gid} did not exist.")
        // Set Alert Title
        alertDialogObject.setTitle("Warning!!")
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        alertDialogObject.setCancelable(false)
        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        alertDialogObject.setNegativeButton("Got it", this::onClick)
        // Create the Alert dialog
        val alertDialog = alertDialogObject.create()
        // Show the Alert Dialog box
        alertDialog.show()
        // **
    }
*/

    private fun myAlertSuccess() {
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        alertDialogObject!!.setMessage("ID ${gadget.gid} updated")
        alertDialogObject.setTitle("Warning!!")
        alertDialogObject.setCancelable(false)
        alertDialogObject.setNegativeButton("Got it", this::onClick)
        val alertDialog = alertDialogObject.create()
        alertDialog.show()
    }

/*
    private fun requestReadAndSetTextInputHint() {
        requestRead = jsonObjectRequestReadMethod(gid)
        queue = Volley.newRequestQueue(this.context)
        queue.add(requestRead)
    }
*/

    private fun requestUpdate(gadget: Gadget) {
        requestUpdate = stringRequestUpdateMethod(gadget) // for pass param on url http read
        queue = Volley.newRequestQueue(this.context)
        queue.add(requestUpdate)
    }

    private fun validateInput(
        brand: String,
        model: String,
        price: String
    ): Boolean {
        return brand.trim().isNotEmpty() &&
                model.trim().isNotEmpty() &&
                price.trim().isNotEmpty()
    }


    override fun onClick(p0: View?) {
        val brand = textInputEditGadgetBrand.text.toString()
        val model = textInputEditGadgetModel.text.toString()
        val price = textInputEditGadgetPrice.text.toString()
        val check = validateInput(brand, model, price)
        if (check) {
            val gadget = Gadget(gadget.gid, brand, model, price.toFloat())
            requestUpdate(gadget)
            textInputLayoutGadgetBrand.hintTextColor = myColors[1]
            textInputLayoutGadgetModel.hintTextColor = myColors[1]
            textInputLayoutGadgetPrice.hintTextColor = myColors[1]
            textInputLayoutGadgetBrand.hint = brand
            textInputLayoutGadgetModel.hint = model
            textInputLayoutGadgetPrice.hint = price
        } else {
            textInputLayoutGadgetBrand.hintTextColor = myColors[0]
            textInputLayoutGadgetModel.hintTextColor = myColors[0]
            textInputLayoutGadgetPrice.hintTextColor = myColors[0]
            textInputLayoutGadgetBrand.hint = "Brand shouldn't be empty"
            textInputLayoutGadgetModel.hint = "Model shouldn't be empty"
            textInputLayoutGadgetPrice.hint = "Price shouldn't be empty"
        }
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        p0!!.cancel() // If user click Got it then dialog box is canceled.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.sub_update_fragment, container, false)
        initialSubUpdateFragmentWidget()
        /* requestReadAndSetTextInputHint() // prepare for editing */
        button.setOnClickListener(this::onClick)
        return view
    }
}