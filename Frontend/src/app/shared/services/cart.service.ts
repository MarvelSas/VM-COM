import { Injectable } from '@angular/core';
import { IProduct } from '../models/product.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { endpoints } from 'src/enums/endpoints.enum';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  cart: IProduct[] = [];

  constructor(private http: HttpClient) {}

  getCart() {}

  addToCart(product: IProduct) {
    this.cart.push(product);

    this.http.post(`${endpoints.cardAdd}`, product);
  }

  deleteFromCart() {}
}
