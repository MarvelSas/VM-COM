package com.VMcom.VMcom.controller;


import com.VMcom.VMcom.model.*;
import com.VMcom.VMcom.services.ProductService;
import jakarta.activation.FileTypeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



    //Product endpoints
    @GetMapping("/products")
    public ResponseEntity<Response> getAllProductsWithPagingAndFilter(@RequestParam("page") int page,
                                                   @RequestParam("pageSize") int pageSize,
                                                   @RequestParam(value = "category",required = false) String category,
                                                   @RequestParam(value = "sortBy", defaultValue = "name",required = false) String sortBy,
                                                   @RequestParam(value = "order", defaultValue = "asc",required = false) String order ,
                                                   @RequestParam(value = "minPrice", defaultValue = "0",required = false) Double minPrice,
                                                   @RequestParam(value = "maxPrice", defaultValue = "100000000",required = false) Double maxPrice,
                                                   @RequestParam(value = "hideOutOfStock",defaultValue = "false",required = false) boolean hideOutOfStock,
                                                   @RequestParam(value = "name",defaultValue = "",required = false) String name

    ){

        try {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(productService.getAllProductsWithPagingAndFilter(page, pageSize,category,sortBy,order,minPrice,maxPrice, hideOutOfStock,name))
                            .message("All products was returned successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );
        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("All products was not returned successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );


        }

    }




    @GetMapping("/getAll")
    public ResponseEntity<Response> getAllProducts(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("products", productService.getAllProducts()))
                        .message("All products returned")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }



    @PostMapping("/add")
    public ResponseEntity<Response> addProduct(@RequestBody ProductWithSpecification productWithSpecification){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("product", productService.addProduct(productWithSpecification)))
                        .message("Product was added successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }



    @PostMapping("/add/productPhoto")
    public ResponseEntity<Response> addProductPhoto(@RequestPart("picture") MultipartFile pictureFiles ){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("productPhotoName", productService.addProductPhoto(pictureFiles)))
                        .message("Product photo was added successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }



    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) throws IOException{
        File img = new File("src/main/resources/static/uploaded-pictures/"+filename);
        return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(img))).body(Files.readAllBytes(img.toPath()));
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<Response> getProduct(@PathVariable("productId") Long productId){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("product",productService.getProductById(productId)))
                        .message("Product returned")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Response> putProduct(@RequestBody ProductWithSpecification productWithSpecification, @PathVariable("productId") Long productId){

        try {

           return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("product",productService.updateProduct(productWithSpecification,productId)))
                            .message("Product returned")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );


        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Product was not updated successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );

        }


    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Response> deleteProduct(@PathVariable("productId") long productId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("product",productService.deleteProduct(productId)))
                            .message("Product was deleted successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Product was not deleted successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );
        }

    }











    //Product category


    @GetMapping("/productCategory/getAll")
    public ResponseEntity<Response> getAllProductCategories(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("productCategories", productService.getAllProductCategories()))
                        .message("All product categories returned")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }



    @PatchMapping("/productCategory/update/{productCategoryId}")
    public ResponseEntity<Response> addProductCategory(@PathVariable("productCategoryId") Long productCategoryId,  @RequestBody String name){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("productCategory", productService.updateProductCategory(productCategoryId,name)))
                        .message("Product category was updated successfully")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }


    @PostMapping("/productCategory/add")
    public ResponseEntity<Response> addProductCategory(@RequestBody ProductCategory productCategory){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("productCategory", productService.addProductCategory(productCategory)))
                            .message("Product category was added successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Product category was not added successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );

        }

    }


    @GetMapping("/get/category/{categoryId}")
    public ResponseEntity<Response> getProductByCategoryId(@PathVariable("categoryId") Long categoryId){

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(LocalDateTime.now())
                        .data(Map.of("products", productService.getProductsByCategory(categoryId)))
                        .message("All products returned with category Id:"+ categoryId)
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/productCategory/delete/{categoryId}")
    public ResponseEntity<Response> deleteProductCategory(@PathVariable("categoryId") Long categoryId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("isProductCategoryDeleted", productService.deleteProductCategory(categoryId)))
                            .message("Product category with id:"+ categoryId+" was deleted successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("message", e.getMessage()))
                            .message("Product category was not deleted successfully")
                            .status(HttpStatus.CONFLICT)
                            .statusCode(HttpStatus.CONFLICT.value())
                            .build()
            );

        }
    }




    //Product specification

    @PostMapping("/{productId}/productSpecificationLine")
    public ResponseEntity<Response> addSpecificationLine(@RequestBody ProductSpecificationLine productSpecificationLine,@PathVariable("productId") Long productId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("productSpecificationLine", productService.addProductSpecificationLine(productSpecificationLine,productId)))
                            .message("Product specification line was added successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Product specification line was not added successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );

        }

    }


    @DeleteMapping("/productSpecificationLine/{productSpecificationLineId}")
    public ResponseEntity<Response> deleteProductSpecificationLine(@PathVariable("productSpecificationLineId") Long productSpecificationLineId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("isProductCategoryDeleted", productService.deleteProductSpecificationLine(productSpecificationLineId)))
                            .message("Product specification with id:"+ productSpecificationLineId+" was deleted successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );

        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("message", e.getMessage()))
                            .message("Product specification was not deleted successfully")
                            .status(HttpStatus.CONFLICT)
                            .statusCode(HttpStatus.CONFLICT.value())
                            .build()
            );

        }
    }


    @PutMapping("/productSpecificationLine/{productSpecificationLineId}")
    public ResponseEntity<Response> putProductSpecificationLine(@RequestBody ProductSpecificationLine productSpecificationLine,@PathVariable("productSpecificationLineId") Long productSpecificationLineId){

        try {

            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("productSpecificationLine",productService.updateProductSpecificationLine(productSpecificationLine,productSpecificationLineId)))
                            .message("Product specification line was updated successfully")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build()
            );


        }catch (Exception e){

            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .timeStamp(LocalDateTime.now())
                            .data(Map.of("Message", e.getMessage()))
                            .message("Product specification line was not updated successfully")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .build()
            );

        }


    }





}
