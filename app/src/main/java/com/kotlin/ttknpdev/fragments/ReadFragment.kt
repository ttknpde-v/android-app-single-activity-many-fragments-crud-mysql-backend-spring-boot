package com.kotlin.ttknpdev.fragments

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.constants.Router
import com.kotlin.ttknpdev.entity.Gadget
import org.json.JSONException
import org.json.JSONObject


class ReadFragment : Fragment(), View.OnClickListener, Response.Listener<JSONObject?>, Response.ErrorListener, DialogInterface.OnClickListener {

    private lateinit var view: View // it is importance for access activity
    // Input
    private lateinit var button: AppCompatButton
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEdit: TextInputEditText
    // Output
    private lateinit var textViewGadgetId: TextView
    private lateinit var textViewGadgetBrand: TextView
    private lateinit var textViewGadgetModel: TextView
    private lateinit var textViewGadgetPrice: TextView

    // For request
    private lateinit var request: JsonObjectRequest
    private lateinit var queue: RequestQueue

    // For changing color TextInputLayout
    private val colorRed = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_enabled)), // Enabled
        intArrayOf(Color.RED)  // The color for the Enabled state
    )
    private val colorGreen = ColorStateList(
        arrayOf(intArrayOf(android.R.attr.state_enabled)),
        intArrayOf(Color.GREEN)
    )

    private fun setDefaultTextViewResult() {
        textViewGadgetId.text = "null"
        textViewGadgetBrand.text = "null"
        textViewGadgetModel.text = "null"
        textViewGadgetPrice.text = "null"
    }

    private fun initialReadFragmentWidget() {
        button = view.findViewById(R.id.appCompatButton)
        textInputLayout = view.findViewById(R.id.textInputLayout)
        textInputEdit = view.findViewById(R.id.textInputEdit)
        textViewGadgetId = view.findViewById(R.id.textViewGadgetId)
        textViewGadgetBrand = view.findViewById(R.id.textViewGadgetBrand)
        textViewGadgetModel = view.findViewById(R.id.textViewGadgetModel)
        textViewGadgetPrice = view.findViewById(R.id.textViewGadgetPrice)
        setDefaultTextViewResult()
    }

    // ** Way to make alert message
    private fun myAlert() {
        // Create the object of AlertDialog Builder class
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        // Set the message show for the Alert time
        alertDialogObject!!.setMessage("ID ${textInputEdit.text} did not exist.")
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
        setDefaultTextViewResult()
        textInputLayout.hintTextColor = colorRed
    }

    private fun validateInput(input: String): Boolean {
        return input.trim().isNotEmpty()
    }

    private fun requestReadAndSetAdapter(gid: String) {
        request = JsonObjectRequest("${Router.ROUTER_GADGET}/read/$gid", this::onResponse, this::onErrorResponse)
        queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    override fun onResponse(p0: JSONObject?) {
        try {
            // *** GSON library to deserialize or parse a JSON array to a Java array or List object.
            val gson = Gson()
            val gadget = gson.fromJson(p0.toString(), Gadget::class.java)
            /* p0.toString() -> {"gid":"G001","brand":"LOGITECH","model":"G4029(MOUSE)","price":990}  *** Remember this is json object is not json array
            // Just use the correct function So this is json array [{"gid":"G001","brand":"LOGITECH","model":"G4029(MOUSE)","price":990} , {...} , {...}] */
            // Don't validate if not exist onErrorResponse function will work
            textViewGadgetId.text = gadget.gid
            textViewGadgetBrand.text = gadget.brand
            textViewGadgetModel.text = gadget.model
            textViewGadgetPrice.text = gadget.price.toString()
        } catch (e: JSONException) {
            Log.d("RESPONSE ERROR (onResponse)", "${e.cause}")
            throw e
        }
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        // If user click Got it then dialog box is canceled.
        p0!!.cancel()
    }

    override fun onErrorResponse(p0: VolleyError?) { // when response err will do (this case response null still err)
        Log.e("RESPONSE ERROR (onErrorResponse)", p0!!.message.toString())
        myAlert()
        // throw p0 // if you need to exit app after found err Just throw it
    }

    override fun onClick(p0: View?) {
        val gid = textInputEdit.text.toString()
        val check = validateInput(gid)
        if (check) {
            requestReadAndSetAdapter(gid)
            textInputLayout.hintTextColor = colorGreen
            textInputLayout.hint = gid

        } else {
            textInputLayout.hintTextColor = colorRed
            textInputLayout.hint = "Gid shouldn't be empty"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.read_fragment, container, false) // Inflate the layout for this fragment
        initialReadFragmentWidget()
        button.setOnClickListener(this::onClick)
        return view
    }
}