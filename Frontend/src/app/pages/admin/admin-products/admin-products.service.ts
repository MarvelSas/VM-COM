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
import {
  IProduct,
  IProductSpecificationLines,
} from 'src/app/shared/models/product.model';

@Injectable({
  providedIn: 'root',
})
export class adminProductsService {
  API_URL = environment.API_URL;
  constructor(
    private http: HttpClient,
    private productsService: ProductsService
  ) {}

  getProducts(productName: string) {
    return this.productsService.getPageableProducts({
      page: 1,
      pageSize: 1000,
      name: productName ? productName : '',
    });
  }

  addProductNew(body: any) {
    return this.http.post<IProductResponseData>(
      this.API_URL + endpoints.addProduct,
      body
    );
  }

  uploadPhoto(photoFile: File) {
    const formData = new FormData();
    formData.append('picture', photoFile);

    return this.http.post<IResPhotoUpload>(
      this.API_URL + endpoints.uploadImage,
      formData
    );
  }

  editProduct(id: number, product: IProductNew) {
    return this.http.put(
      this.API_URL + endpoints.editProduct + '/' + id,
      product
    );
  }

  deleteProduct(id: number) {
    return this.http.delete<IProductResponseData>(
      this.API_URL + endpoints.deleteProduct + '/' + id
    );
  }
}
