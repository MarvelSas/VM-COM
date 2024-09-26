import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from 'src/environments/environment';
import { endpoints } from 'src/enums/endpoints.enum';

import { ProductsService } from 'src/app/shared/services/products.service';

import {
  IProductNew,
  IProductResponseData,
  IResPhotoUpload,
} from './product.model';
import { IProduct } from 'src/app/shared/models/product.model';

@Injectable({
  providedIn: 'root',
})
export class adminProductsService {
  API_URL = environment.API_URL;
  constructor(
    private http: HttpClient,
    private productsService: ProductsService
  ) {}

  getProducts() {
    return this.productsService.getProducts();
  }

  addProductNew(body: any) {
    console.log(body);
    return this.http.post<IProductResponseData>(
      this.API_URL + endpoints.addProduct,
      body
    );
  }

  uploadPhoto(photoFile: File) {
    const formData = new FormData();
    formData.append('picture', photoFile);
    console.log(formData);

    return this.http.post<IResPhotoUpload>(
      this.API_URL + endpoints.uploadImage,
      // 'http://localhost:8088/upload', //DEBUG
      formData
    );
  }

  editProduct(id: number, product: IProductNew) {
    console.log(product);
    return this.http.patch(
      this.API_URL + endpoints.editProduct + '/' + id,
      product
    );
  }

  deleteProduct(id: number) {
    console.log(id);
    return this.http.delete<IProductResponseData>(
      this.API_URL + endpoints.deleteProduct + '/' + id
    );
  }

  // addProduct(
  //   name,
  //   price,
  //   imageUrl,
  //   description,
  //   productCategory
  // ): Observable<IProductResponseData> {
  //   console.log(productCategory);
  //   const body = {
  //     name: name,
  //     price: price,
  //     url: imageUrl,
  //     photoUrl: imageUrl, //TODO
  //     productCategory: productCategory, //TODO
  //     amount: 5, //TODO
  //     description: description,
  //   };

  //   return this.http.post<IProductResponseData>(
  //     this.API_URL,
  //     body
  //   );
  // }
}
