package com.ttknpdev.service

import com.ttknpdev.entity.Gadget


interface GadgetService {
    fun reads () : ArrayList<Gadget>
    fun read (gid : String) : Gadget
    fun delete (gid : String) : Boolean
    fun create (gadget : Gadget) : Boolean
    fun update (gadget : Gadget,gid: String) : Boolean
}