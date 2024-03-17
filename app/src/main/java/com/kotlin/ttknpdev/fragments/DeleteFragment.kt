package com.kotlin.ttknpdev.fragments

import com.google.gson.Gson
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.constants.Router
import com.kotlin.ttknpdev.entity.Gadget
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class DeleteFragment : Fragment(), View.OnClickListener, DialogInterface.OnClickListener {

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
    private lateinit var requestRead: JsonObjectRequest
    private lateinit var requestDelete: StringRequest
    private lateinit var queue: RequestQueue

    // For changing color TextInputLayout
    private val myColors = arrayOf(
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)),intArrayOf(Color.RED)),
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)),intArrayOf(Color.GREEN))
    )

    private fun jsonObjectRequestReadMethod(gid: String) = object :
        JsonObjectRequest( "${Router.ROUTER_GADGET}/read/$gid",
            Response.Listener { response ->
                val gson = Gson()
                val gadget = gson.fromJson(response.toString(), Gadget::class.java)
                textViewGadgetId.text = gadget.gid
                textViewGadgetBrand.text = gadget.brand
                textViewGadgetModel.text = gadget.model
                textViewGadgetPrice.text = gadget.price.toString()
                // request api again for removing the id
                requestDelete(gid)
            },
            Response.ErrorListener { error ->
                Log.e("RESPONSE ERROR (onErrorResponse)", error!!.message.toString())
                myAlertWarning()
                // throw p0 // if you need to exit app after found err Just throw it
            }) {
        // this block can override function of JsonObjectRequest
    }

    private fun stringRequestDeleteMethod(gid: String) = object :
        StringRequest( Request.Method.DELETE,"${Router.ROUTER_GADGET}/delete/$gid",
            Response.Listener { response ->
                Log.d("DEBUG",response) // true or false *** always true
                myAlertSuccess()
            },
            Response.ErrorListener { error ->
                Log.e("RESPONSE ERROR (onErrorResponse)", error!!.message.toString())
                // if you need to exit app after found err Just throw it
                throw error
            }) {
    }

    private fun setDefaultTextViewResult() {
        textViewGadgetId.text = "null"
        textViewGadgetBrand.text = "null"
        textViewGadgetModel.text = "null"
        textViewGadgetPrice.text = "null"
    }

    private fun initialDeleteFragmentWidget() {
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
    private fun myAlertWarning() {
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        alertDialogObject!!.setMessage("ID ${textInputEdit.text} did not exist.")
        alertDialogObject.setTitle("Warning!!")
        alertDialogObject.setCancelable(false)
        alertDialogObject.setNegativeButton("Got it", this::onClick)
        val alertDialog = alertDialogObject.create()
        alertDialog.show()
        // **
        setDefaultTextViewResult()
        textInputLayout.hintTextColor = myColors[0]
    }
    private fun myAlertSuccess() {
        val alertDialogObject = this.context?.let { AlertDialog.Builder(it) }
        alertDialogObject!!.setMessage("ID ${textInputEdit.text} deleted")
        alertDialogObject.setTitle("Warning!!")
        alertDialogObject.setCancelable(false)
        alertDialogObject.setNegativeButton("Got it", this::onClick)
        val alertDialog = alertDialogObject.create()
        alertDialog.show()
        textInputLayout.hintTextColor = myColors[1]
    }

    private fun validateInput(input: String): Boolean {
        return input.trim().isNotEmpty()
    }

    private fun requestRead(gid: String) {
        requestRead = jsonObjectRequestReadMethod(gid)
        queue = Volley.newRequestQueue(this.context)
        queue.add(requestRead)
    }

    private fun requestDelete(gid: String) {
        requestDelete = stringRequestDeleteMethod(gid)
        queue = Volley.newRequestQueue(this.context)
        queue.add(requestDelete)
    }

    override fun onClick(p0: View?) {
        val gid = textInputEdit.text.toString()
        val check = validateInput(gid)
        if (check) {
            requestRead(gid)
            textInputLayout.hintTextColor = myColors[1]
            textInputLayout.hint = gid
        }
        else {
            textInputLayout.hintTextColor = myColors[0]
            textInputLayout.hint = "Gid shouldn't be empty"
        }
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        p0!!.cancel()  // If user click Got it then dialog box is canceled.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.delete_fragment, container, false) // Inflate the layout for this fragment
        initialDeleteFragmentWidget()
        button.setOnClickListener(this::onClick)
        return view
    }

}