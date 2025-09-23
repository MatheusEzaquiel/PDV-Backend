package com.mbe.viapdv.controller;


import com.mbe.viapdv.service.RoleService;
import com.mbe.viapdv.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseDTO> list() {
        ResponseDTO response = roleService.listActive();
        return ResponseEntity.status(response.status()).body(response);
    }
}
