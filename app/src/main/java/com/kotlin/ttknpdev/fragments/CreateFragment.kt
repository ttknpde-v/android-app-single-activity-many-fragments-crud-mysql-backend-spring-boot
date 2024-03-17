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
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.constants.Router
import com.kotlin.ttknpdev.entity.Gadget
import org.json.JSONException
import org.json.JSONObject

class CreateFragment : Fragment(), View.OnClickListener, Response.Listener<String?>, Response.ErrorListener,
    DialogInterface.OnClickListener {

    private lateinit var view: View // it is importance for access activity

    // Input
    private lateinit var button: AppCompatButton
    private lateinit var textInputLayoutGadgetId: TextInputLayout
    private lateinit var textInputEditGadgetId: TextInputEditText
    private lateinit var textInputLayoutGadgetBrand: TextInputLayout
    private lateinit var textInputEditGadgetBrand: TextInputEditText
    private lateinit var textInputLayoutGadgetModel: TextInputLayout
    private lateinit var textInputEditGadgetModel: TextInputEditText
    private lateinit var textInputLayoutGadgetPrice: TextInputLayout
    private lateinit var textInputEditGadgetPrice: TextInputEditText

    // For request
    private lateinit var requestCreate: StringRequest
    private lateinit var queue: RequestQueue

    // For changing color TextInputLayout
    private val myColors = arrayOf(
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.RED)),
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.GREEN))
    )

    private fun initialReadFragmentWidget() {
        button = view.findViewById(R.id.appCompatButton)
        textInputLayoutGadgetId = view.findViewById(R.id.textInputLayoutGadgetId)
        textInputEditGadgetId = view.findViewById(R.id.textInputEditGadgetId)
        textInputLayoutGadgetBrand = view.findViewById(R.id.textInputLayoutGadgetBrand)
        textInputEditGadgetBrand = view.findViewById(R.id.textInputEditGadgetBrand)
        textInputLayoutGadgetModel = view.findViewById(R.id.textInputLayoutGadgetModel)
        textInputEditGadgetModel = view.findViewById(R.id.textInputEditGadgetModel)
        textInputLayoutGadgetPrice = view.findViewById(R.id.textInputLayoutGadgetPrice)
        textInputEditGadgetPrice = view.findViewById(R.id.textInputEditGadgetPrice)
    }


    // ** Way to make alert message
    private fun myAlertSuccess() {
        // Create the object of AlertDialog Builder class
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        // Set the message show for the Alert time
        alertDialogObject!!.setMessage("Created successfully.")
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

    private fun myAlertFailed() {
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        alertDialogObject!!.setMessage("Failed to create.")
        alertDialogObject.setTitle("Warning!!")
        alertDialogObject.setCancelable(false)
        alertDialogObject.setNegativeButton("Got it", this::onClick)
        val alertDialog = alertDialogObject.create()
        alertDialog.show()
    }

    private fun validateInput(
        gid: String,
        brand: String,
        model: String,
        price: String
    ): Boolean {
        return gid.trim().isNotEmpty() &&
                brand.trim().isNotEmpty() &&
                model.trim().isNotEmpty() &&
                price.trim().isNotEmpty()
    }

    // pass arg for mapping @RequestParam
    // api look like /create?gid=Y&brand=Y2&price=Y3 this is request and pass param on URL
    private fun stringRequestCreateMethod(gadget: Gadget) = object :
        StringRequest(
            Request.Method.POST,
            "${Router.ROUTER_GADGET}/create",
            this::onResponse,
            this::onErrorResponse
        ) {
        override fun getHeaders(): MutableMap<String, String> { // Map @RequestParam annotation
            val params = hashMapOf<String, String>()
            params["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8"
            return params
        }
        override fun getParams(): MutableMap<String, String>? {
            val mapRequest = hashMapOf<String, String>()
            mapRequest["gid"] = gadget.gid
            mapRequest["brand"] = gadget.brand
            mapRequest["model"] = gadget.model
            mapRequest["price"] = gadget.price.toString()
            return mapRequest
        }

    }

    private fun requestCreate(gadget: Gadget) {
        requestCreate = stringRequestCreateMethod(gadget)
        queue = Volley.newRequestQueue(this.context)
        queue.add(requestCreate)
    }

    override fun onResponse(p0: String?) {
        try {
            Log.d("DEBUG",p0.toString()) // be only true or false
            if (p0.toString() == "true") {
                myAlertSuccess()
            } else {
                myAlertFailed()
            }
        } catch (e: JSONException) {
            Log.d("RESPONSE ERROR (onResponse)", "${e.cause}")
            throw e
        }
    }

    override fun onErrorResponse(p0: VolleyError?) {
        Log.e("RESPONSE ERROR (onErrorResponse)", p0!!.message.toString())
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        p0!!.cancel() // If user click Got it then dialog box is canceled.
    }

    override fun onClick(p0: View?) {
        val gid = textInputEditGadgetId.text.toString()
        val brand = textInputEditGadgetBrand.text.toString()
        val model = textInputEditGadgetModel.text.toString()
        val price = textInputEditGadgetPrice.text.toString()
        val check = validateInput(gid, brand, model, price)
        if (check) {
            val gadget = Gadget(gid, brand, model, price.toFloat())
            // Log.d("DEBUG","$gadget")
            requestCreate(gadget)
            textInputLayoutGadgetId.hintTextColor = myColors[1]
            textInputLayoutGadgetBrand.hintTextColor = myColors[1]
            textInputLayoutGadgetModel.hintTextColor = myColors[1]
            textInputLayoutGadgetPrice.hintTextColor = myColors[1]
            textInputLayoutGadgetId.hint = gid
            textInputLayoutGadgetBrand.hint = brand
            textInputLayoutGadgetModel.hint = model
            textInputLayoutGadgetPrice.hint = price
        } else {
            textInputLayoutGadgetId.hintTextColor = myColors[0]
            textInputLayoutGadgetBrand.hintTextColor = myColors[0]
            textInputLayoutGadgetModel.hintTextColor = myColors[0]
            textInputLayoutGadgetPrice.hintTextColor = myColors[0]
            textInputLayoutGadgetId.hint = "Gid shouldn't be empty"
            textInputLayoutGadgetBrand.hint = "Brand shouldn't be empty"
            textInputLayoutGadgetModel.hint = "Model shouldn't be empty"
            textInputLayoutGadgetPrice.hint = "Price shouldn't be empty"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        view = inflater.inflate(R.layout.creat_fragment,container,false) // Inflate the layout for this fragment
        initialReadFragmentWidget()
        button.setOnClickListener(this::onClick)
        return view
    }
}