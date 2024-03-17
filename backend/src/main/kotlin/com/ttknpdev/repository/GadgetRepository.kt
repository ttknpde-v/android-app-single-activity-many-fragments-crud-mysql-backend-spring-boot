package com.ttknpdev.repository

import com.ttknpdev.entity.Gadget
import org.springframework.data.jpa.repository.JpaRepository

interface GadgetRepository : JpaRepository< Gadget,String> { }