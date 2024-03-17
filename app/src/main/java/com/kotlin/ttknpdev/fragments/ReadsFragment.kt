package com.kotlin.ttknpdev.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.adapter.CustomAdapterGadget
import com.kotlin.ttknpdev.entity.Gadget
import org.json.JSONArray
import org.json.JSONException
import com.kotlin.ttknpdev.constants.Router


class ReadsFragment : Fragment(), Response.Listener<JSONArray?>, Response.ErrorListener {

    private lateinit var view: View // it is importance for access activity

    // for adapter
    private val gadgetList : ArrayList<Gadget> = ArrayList()

    // for adapter too
    private lateinit var recyclerViewGadgetTable: RecyclerView
    private lateinit var myAdapter: RecyclerView.Adapter<CustomAdapterGadget.MyViewHolder>
    private lateinit var setLayoutManager: RecyclerView.LayoutManager

    // for request
    private lateinit var request: JsonArrayRequest
    private lateinit var queue: RequestQueue

    private fun initialReadsFragmentWidget() {
        recyclerViewGadgetTable = view.findViewById(R.id.recyclerViewGadgetTable)
    }

    private fun initialReadsFragmentRecyclerView() {
        // set layout
        setLayoutManager = LinearLayoutManager(view.context)
        recyclerViewGadgetTable.layoutManager = setLayoutManager
    }

    private fun requestAndSetAdapter() {
        // request = jsonRequestReads("http://192.168.1.104:8000/api/gadget/reads") // *** First way
        request = JsonArrayRequest("${Router.ROUTER_GADGET}/reads",this::onResponse,this::onErrorResponse) // *** Second way
        queue = Volley.newRequestQueue(this.context)
        queue.add(request)
    }

    // *** First way
/*
    private fun jsonRequestReads(url: String) = object : JsonArrayRequest(
        url,
        Response.Listener { response ->
            Log.d("RESPONSE", "logging after request api successes so you have got response!!")
            try {
                val gson = Gson() // good class for covert json array to Kotlin POJO object
                // Log.d("RESPONSE", p0.toString()) // [{"gid":"G001","brand":"LOGITECH","model":"G4029(MOUSE)","price":990},{"gid":"G002","brand":"LOGITECH","model":"H540(HEADSET)","price":1350},{"gid":"G003","brand":"LOGITECH","model":"G512 RGB(KEYBOARD)","price":2490},{"gid":"G007","brand":"LOGITECH","model":"LG-C505E(WEBCAM)","price":10000},{"gid":"G008","brand":"LOGITECH","model":"G502 HERO RGB(MOUSE)","price":1500},{"gid":"G010","brand":"NUBWO","model":"X43 PLUS(MOUSE)","price":329},{"gid":"G011","brand":"NUBWO","model":"N1 PRO(HEADSET)","price":299},{"gid":"G013","brand":"NUBWO","model":"NK-41(KEYBOARD)","price":230},{"gid":"G015","brand":"NUBWO","model":"NKB107(WIRELESS KEYBOARD)","price":420},{"gid":"G016","brand":"ASUS","model":"M3 GEN II(MOUSE)","price":790},{"gid":"G017","brand":"ASUS","model":"M4 AIR(MOUSE)","price":1290},{"gid":"G018","brand":"HP","model":"H200GS(HEADSET)","price":640},{"gid":"G019","brand":"HP","model":"GK400F(KEYBOARD)","price":840},{"gid":"G020","brand":"HP","model":"OMEN 24(Monitor)","price":6150},{"gid":"G021","brand":"MSI","model":"A13M-250(Notebook)","price":38990},{"gid":"G022","brand":"MSI","model":"MSI Stealth16 Mercedes-AMG Motorsport A13VG-269TH(Notebook)","price":86990}]
                val gadgetArray = gson.fromJson(response.toString(), Array<Gadget>::class.java)
                // Log.d("RESPONSE", gadgets[0].toString())
                for (i in gadgetArray.indices) { // ***
                    gadgetList.add(gadgetArray[i])
                }
                myAdapter = CustomAdapterGadget(gadgetList)
                recyclerViewGadgetTable.adapter = myAdapter
                // Log.d("RESPONSE","${gadgetList[0]}") // exist
            } catch (e: JSONException) {
                Log.d("RESPONSE ERROR", "${e.cause}")
                throw e
            }
        },
        Response.ErrorListener { error ->
            Log.e("RESPONSE ERROR", error.toString())
        }
    ) {
        // can override any functions of JsonArrayRequest class
        // this block

    }
*/

    // *** Second way
    override fun onResponse(p0: JSONArray?) {
        try {

            //  p0.toString() returns [{"gid":"G001","brand":"LOGITECH","model":"G4029(MOUSE)","price":990},{"gid":"G002","brand":"LOGITECH","model":"H540(HEADSET)","price":1350},{"gid":"G003","brand":"LOGITECH","model":"G512 RGB(KEYBOARD)","price":2490},{"gid":"G007","brand":"LOGITECH","model":"LG-C505E(WEBCAM)","price":10000},{"gid":"G008","brand":"LOGITECH","model":"G502 HERO RGB(MOUSE)","price":1500},{"gid":"G010","brand":"NUBWO","model":"X43 PLUS(MOUSE)","price":329},{"gid":"G011","brand":"NUBWO","model":"N1 PRO(HEADSET)","price":299},{"gid":"G013","brand":"NUBWO","model":"NK-41(KEYBOARD)","price":230},{"gid":"G015","brand":"NUBWO","model":"NKB107(WIRELESS KEYBOARD)","price":420},{"gid":"G016","brand":"ASUS","model":"M3 GEN II(MOUSE)","price":790},{"gid":"G017","brand":"ASUS","model":"M4 AIR(MOUSE)","price":1290},{"gid":"G018","brand":"HP","model":"H200GS(HEADSET)","price":640},{"gid":"G019","brand":"HP","model":"GK400F(KEYBOARD)","price":840},{"gid":"G020","brand":"HP","model":"OMEN 24(Monitor)","price":6150},{"gid":"G021","brand":"MSI","model":"A13M-250(Notebook)","price":38990},{"gid":"G022","brand":"MSI","model":"MSI Stealth16 Mercedes-AMG Motorsport A13VG-269TH(Notebook)","price":86990}]
            val gson = Gson()
            val gadgetArray = gson.fromJson(p0.toString(), Array<Gadget>::class.java)

            for (i in gadgetArray.indices) { // *** I need to use ArrayList<T> type
                gadgetList.add(gadgetArray[i])
            }

            // add adapter
            myAdapter = CustomAdapterGadget(gadgetList)
            recyclerViewGadgetTable.adapter = myAdapter

        } catch (e: JSONException) {
            Log.d("RESPONSE ERROR", "${e.cause}")
            throw e
        }
    }

    override fun onErrorResponse(p0: VolleyError?) {
        Log.e("RESPONSE ERROR", p0!!.message.toString())
        throw p0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.reads_fragment, container, false) // Inflate the layout for this fragment
        initialReadsFragmentWidget()
        initialReadsFragmentRecyclerView() // set layout of recycler view
        requestAndSetAdapter() // req/res and set adapter
        return view
    }
}