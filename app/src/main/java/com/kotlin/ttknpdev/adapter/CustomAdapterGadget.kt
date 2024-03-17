package com.kotlin.ttknpdev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.ttknpdev.R
import com.kotlin.ttknpdev.entity.Gadget

class CustomAdapterGadget(private var gadget : ArrayList<Gadget>) : RecyclerView.Adapter<CustomAdapterGadget.MyViewHolder>(){

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /* prepare id in item and we use take them for taking some value */
        var textViewGadgetId: TextView = view.findViewById(R.id.textViewGadgetId)
        var textViewGadgetBrand: TextView = view.findViewById(R.id.textViewGadgetBrand)
        var textViewGadgetModel: TextView = view.findViewById(R.id.textViewGadgetModel)
        var textViewGadgetPrice: TextView = view.findViewById(R.id.textViewGadgetPrice)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        /* This function sets the views to display the items. (retrieve item xml file)*/
        val itemView: View = LayoutInflater
            .from(p0.context)
            .inflate(R.layout.item_gadget, p0, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.textViewGadgetId.text = gadget[p1].gid
        p0.textViewGadgetBrand.text = gadget[p1].brand
        p0.textViewGadgetModel.text = gadget[p1].model
        p0.textViewGadgetPrice.text = gadget[p1].price.toString()
    }

    override fun getItemCount(): Int {
        return gadget.size
    }
}