package com.ttknpdev.controller

import com.ttknpdev.entity.Gadget
import com.ttknpdev.log.Log4j
import com.ttknpdev.service.GadgetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
/* @RequestParam type application/x-www-form-urlencoded;charset=UTF-8
*  @RequestBody type application/json */
// my bean
@RestController
@RequestMapping(value = ["/api/gadget"])
class ApiController {
    private final val log4j : Log4j
    private final val gadgetService : GadgetService
    @Autowired
    constructor(gadgetService : GadgetService) {
        this.gadgetService = gadgetService
        log4j = Log4j(this.javaClass) // like you write -> (ApiController.class)
    }
    @GetMapping(value = ["/reads"])
    private fun getAllGadget() : ResponseEntity<ArrayList<Gadget>> {
        log4j.logBack.info("you accessed /api/gadget/reads")
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(gadgetService.reads())
    }
    @GetMapping(value = ["/read/{gid}"])
    private fun getGadget(@PathVariable gid : String) : ResponseEntity<Gadget> {
        log4j.logBack.info("you accessed /api/gadget/read/$gid")
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(gadgetService.read(gid))
    }
    @PostMapping(value = ["/create"])
    /*
    // In kotlin I found this way for passing params data to api and map it by @RequestParam
    // why I don't pass json
    // because StringRequest() method I used can't pass Json
    // Why u dont use JsonObjectRequest
    // because I need to use my response it was type boolean
    */
    private fun saveGadget(@RequestParam body : Map<String,String>) : ResponseEntity<Boolean> {
        log4j.logBack.info("you accessed /api/gadget/create")
        val gadget = Gadget(body["gid"].toString(),body["brand"].toString(),body["model"].toString(),body["price"].toString().toFloat())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(gadgetService.create(gadget))
    }
    @DeleteMapping(value = ["/delete/{gid}"])
    private fun removeGadget(@PathVariable gid : String) : ResponseEntity<Boolean> {
        log4j.logBack.info("you accessed /api/gadget/delete/${gid}")
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(gadgetService.delete(gid))
    }

    @PutMapping(value = ["/update/{gid}"])
    /*
    // api look like /update/G027?brand=LOGITECH&model=X43 PLUS(MOUSE)&price=7900.0
    // this is request param (@RequestParam) not json
    */
    private fun updateGadget(@RequestParam body : Map<String,String> ,@PathVariable gid : String) : ResponseEntity<Boolean> {
        log4j.logBack.info("you accessed /api/gadget/update/${gid}")
        // log4j.logBack.info("{}", body) {brand=LOGITECH, model=X43 PLUS(MOUSE), price=7900.0}
        val gadget = Gadget(gid,body["brand"].toString(),body["model"].toString(),body["price"].toString().toFloat())
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(gadgetService.update(gadget,gid))
    }
}