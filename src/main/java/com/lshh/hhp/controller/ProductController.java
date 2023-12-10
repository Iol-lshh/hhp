package com.lshh.hhp.controller;

import com.lshh.hhp.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/product")
@AllArgsConstructor
@RestController
public class ProductController{
    final ProductService productService;


}