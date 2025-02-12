import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from 'src/environments/environment';
import { endpoints } from 'src/enums/endpoints.enum';

import {
  IOneProductResponse,
  IPageableParams,
  IProductsResponse,
} from '../models/product.model';
import { ICategoriesGetResponseData } from 'src/app/pages/admin/admin-categories/category.model';
import { IApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  API_URL = environment.API_URL;
  constructor(private http: HttpClient) {}

  getProducts(): Observable<IApiResponse<IProductsResponse>> {
    return this.http.get<IApiResponse<IProductsResponse>>(
      `${this.API_URL + endpoints.getAllProducts}`,
      {
        headers: {
          skipInterceptor: 'true',
        },
      }
    );
  }

  getPageableProducts(params: IPageableParams) {
    let pageableParams: HttpParams = new HttpParams()
      .set('page', (params.page - 1).toString())
      .set('pageSize', params.pageSize.toString());

    if (params.category) {
      if (params.category !== 'All') {
        pageableParams = pageableParams.set('category', params.category);
      }
    }

    if (params.sortBy) {
      pageableParams = pageableParams.set('sortBy', params.sortBy);
    }

    if (params.minPrice) {
      pageableParams = pageableParams.set(
        'minPrice',
        params.minPrice.toString()
      );
    }

    if (params.maxPrice) {
      pageableParams = pageableParams.set(
        'maxPrice',
        params.maxPrice.toString()
      );
    }

    if (params.name) {
      pageableParams = pageableParams.set('name', params.name);
    }

    if (params.order) {
      pageableParams = pageableParams.set('order', params.order);
    }

    return this.http.get<IApiResponse<IProductsResponse>>(
      `${this.API_URL + endpoints.getPageableProducts}`,
      { params: pageableParams, headers: { skipInterceptor: 'true' } }
    );
  }

  getProduct(id: number): Observable<IApiResponse<IOneProductResponse>> {
    return this.http.get<IApiResponse<IOneProductResponse>>(
      `${this.API_URL + endpoints.getProduct}/${id}`,
      { headers: { skipInterceptor: 'true' } }
    );
  }

  getProductsByName(
    name: string,
    searchCategory: string
  ): Observable<IApiResponse<IProductsResponse>> {
    let params = new HttpParams()
      .set('name', name)
      .set('page', '0')
      .set('pageSize', '5');

    if (searchCategory !== 'Wszystkie') {
      params = params.set('category', searchCategory);
    }

    return this.http.get<IApiResponse<IProductsResponse>>(
      `${this.API_URL + endpoints.getPageableProducts}`,
      { params, headers: { skipInterceptor: 'true' } }
    );
  }

  addProduct() {}

  updateProduct() {}

  deleteProduct() {}

  getCategories() {
    return this.http.get<ICategoriesGetResponseData>(
      this.API_URL + endpoints.getAllCategories
    );
  }
}
