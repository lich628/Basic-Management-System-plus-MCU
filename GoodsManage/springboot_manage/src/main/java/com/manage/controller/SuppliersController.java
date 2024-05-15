package com.manage.controller;

import com.manage.entity.Suppliers;
import com.manage.service.SuppliersService;
import com.manage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/suppliers")
public class SuppliersController {
    @Autowired
    SuppliersService suppliersService;

    @GetMapping("/getAllSuppliers")
    public Result getAllSuppliers() {
        return Result.ok().data("suppliers", suppliersService.getAllSuppliers());
    }

    @PostMapping("/addSupplier")
    public Result addSupplier(@RequestBody Suppliers supplier) {
        return suppliersService.addSupplier(supplier) == 1 ? Result.ok() : Result.error();
    }

    @PutMapping("/updateSupplier")
    public Result updateSupplier(@RequestBody Suppliers supplier) {
        return suppliersService.updateSupplier(supplier) == 1 ? Result.ok() : Result.error();
    }

    @DeleteMapping("/deleteSupplier/{id}")
    public Result deleteSupplier(@PathVariable int id) {
        return suppliersService.deleteSupplier(id) == 1 ? Result.ok() : Result.error();
    }

}
