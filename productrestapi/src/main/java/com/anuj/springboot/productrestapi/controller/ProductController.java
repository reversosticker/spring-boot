package com.anuj.springboot.productrestapi.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anuj.springboot.productrestapi.model.Product;
import com.anuj.springboot.productrestapi.repos.ProductRepository;

@RestController
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@GetMapping("/products")
	public List<Product> getProducts(){
		return productRepository.findAll();
	}
	
	@GetMapping("/product/{id}")
	//read only is true when we are caching select query
	//cacheable is accepting the name of cache for which we has created config
	@Transactional(readOnly = true)
	@Cacheable("product-cache")
	public Product getProducts(@PathVariable("id") int id){
		return productRepository.findById(id).get();
	}
	
	@RequestMapping(value="/products/",method=RequestMethod.PUT)
	public Product updateProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}
	
	@RequestMapping(value="/products/",method=RequestMethod.POST)
	public Product createProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}

	//CacheEvict is used to delete data from cache
	@CacheEvict("product-cache")
	@RequestMapping(value="/products/{id}",method=RequestMethod.DELETE)
	public void deleteProduct(@PathVariable("id") int id) {
		productRepository.deleteById(id);
	}
	
}
