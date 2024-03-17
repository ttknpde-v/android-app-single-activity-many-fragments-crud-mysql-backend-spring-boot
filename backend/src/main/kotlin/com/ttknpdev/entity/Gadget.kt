package com.ttknpdev.entity


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "gadget")
data class Gadget(@Id var gid : String,
                  @Column var brand : String,
                  @Column var model : String,
                  @Column var price : Float) {
}


