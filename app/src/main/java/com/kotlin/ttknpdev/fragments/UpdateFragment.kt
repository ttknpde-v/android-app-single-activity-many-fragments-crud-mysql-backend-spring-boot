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
import androidx.fragment.app.*
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

/*
  this is Fragment one of my fragments, It can include another fragment
  May write a different ,but it is not hard !!
*/
class UpdateFragment : Fragment(), View.OnClickListener, Response.Listener<JSONObject?>, Response.ErrorListener  , DialogInterface.OnClickListener {

    // it is importance for access activity
    private lateinit var view: View

    // Input
    private lateinit var button: AppCompatButton
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEdit: TextInputEditText

    // for include another fragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    
    // For changing color TextInputLayout
    private val myColors = arrayOf(
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.RED)),
        ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)), intArrayOf(Color.GREEN))
    )

    // For request
    private lateinit var request: JsonObjectRequest
    private lateinit var queue: RequestQueue

    private var gadget = Gadget("","","",0F)

    // way to include another fragment
    private fun loadFragment(fragment: Fragment) {
        // create a FragmentManager and get parent of fragment (means This fragment)
        fragmentManager = parentFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.fragmentContainerViewSubUpdate,fragment) // called your fragment id , fragment that you want to place
        // save the changes
        fragmentTransaction.commit()
    }

    private fun initialReadFragmentWidget() {
        button = view.findViewById(R.id.appCompatButton)
        textInputLayout = view.findViewById(R.id.textInputLayout)
        textInputEdit = view.findViewById(R.id.textInputEdit)
    }

    private fun validateInput(input: String): Boolean {
        return input.trim().isNotEmpty()
    }

    private fun requestReadAndInitialFragment(gid: String) {
        request = JsonObjectRequest("${Router.ROUTER_GADGET}/read/$gid", this::onResponse, this::onErrorResponse)
        queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    private fun myAlertFailed() {
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
    }

    private fun removeFragments() {
        // Create and commit a new transaction
        fragmentManager.commit {
            setReorderingAllowed(true)
            // Replace whatever is in the fragment_container view with this fragment
            replace<Fragment>(R.id.fragmentContainerViewSubUpdate)
        }
    }

    override fun onClick(p0: View?) {
        val gid = textInputEdit.text.toString()
        val check = validateInput(gid)
        if (check) {
            /* Why I don't call read method then validate it on this class. Because Any functions for request As (StringRequest(...) , JsonObjectRequest(...))
            *  It is Asynchronous [Meaning it do works and another do works too]. Clear!
            *  loadFragment(SubUpdateFragment(gid))
            *  So if you need to use response before do something
            *  Do it on functions override onResponse(...)
            */
            requestReadAndInitialFragment(gid)
        } else {
            textInputLayout.hintTextColor = myColors[0]
            textInputLayout.hint = "Gid shouldn't be empty"
        }
    }

    // If you want to do after response Just do in this function onResponse(...) it's common!!
    override fun onResponse(p0: JSONObject?) {
        try {

            val gson = Gson()
            val gadget = gson.fromJson(p0.toString(), Gadget::class.java)

            // Don't validate if not exist onErrorResponse function will work
            this.gadget.gid = gadget.gid
            this.gadget.brand = gadget.brand
            this.gadget.model = gadget.model
            this.gadget.price = gadget.price

            textInputLayout.hintTextColor = myColors[1]
            textInputLayout.hint = this.gadget.gid

            loadFragment(SubUpdateFragment(this.gadget))

        } catch (e: JSONException) {
            Log.d("RESPONSE ERROR (onResponse)", "${e.cause}")
            throw e
        }
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        // If user click Got it then dialog box is canceled.
        p0!!.cancel()
    }

    override fun onErrorResponse(p0: VolleyError?) {
        Log.e("RESPONSE ERROR (onErrorResponse)", p0!!.message.toString())
        textInputLayout.hintTextColor = myColors[0] // if gid didn't exist
        textInputLayout.hint = textInputEdit.text.toString()
        myAlertFailed()
        removeFragments() // Note !! if first case not found will close app Why??
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.update_fragment, container, false) // Inflate the layout for this fragment
        initialReadFragmentWidget()
        button.setOnClickListener(this::onClick)
        return view
    }

}