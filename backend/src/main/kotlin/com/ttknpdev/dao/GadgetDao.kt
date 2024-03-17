package com.ttknpdev.dao

import com.ttknpdev.entity.Gadget
import com.ttknpdev.repository.GadgetRepository
import com.ttknpdev.service.GadgetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service // my bean
class GadgetDao @Autowired constructor(private val gadgetRepository: GadgetRepository) : GadgetService {
    /*
     private final var gadgetRepository: GadgetRepository
     constructor(gadgetRepository: GadgetRepository) {
        this.gadgetRepository = gadgetRepository
     }
    */
    override fun reads(): ArrayList<Gadget> {
        // in java looks like (ArrayList<Gadget>) gadgetRepository.findAll()
        return gadgetRepository.findAll() as ArrayList<Gadget>
    }

    override fun read(gid: String): Gadget {
        // if not exist returns null ??
        return gadgetRepository.findById(gid).orElse(null)
    }

    override fun create(gadget: Gadget): Boolean {
        val gadgetExist = gadgetRepository.findById(gadget.gid).isPresent // for return false
        if (gadgetExist) {
            return false
        }
        gadgetRepository.save(gadget)
        return true
    }

    override fun delete(gid: String): Boolean {
        val gadgetExist = gadgetRepository.findById(gid).isPresent
        if (gadgetExist) {
            gadgetRepository.deleteById(gid)
            return true
        }
        return false
    }

    override fun update(gadget: Gadget, gid: String): Boolean {
                                                    // ** response from findById(gid)
        return gadgetRepository.findById(gid).map( fun(it: Gadget) : Boolean {
            it.brand = gadget.brand
            it.model = gadget.model
            it.price = gadget.price
            gadgetRepository.save(it)
            return true
        }).orElseGet  {
            return@orElseGet false
        }
    }
}