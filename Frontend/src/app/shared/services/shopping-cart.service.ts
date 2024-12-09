import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { endpoints } from 'src/enums/endpoints.enum';
import { environment } from 'src/environments/environment';
import { IShopCardResponse } from '../models/shop-cart.model';
import { IProduct } from '../models/product.model';
import { ToastrService } from 'ngx-toastr';

export interface IProductCategory {
  id: number;
  name: string;
}

interface IAddProductReq {
  product: IProduct;
  quantity: number;
}
@Injectable({
  providedIn: 'root',
})
export class ShoppingCartService {
  API_URL = environment.API_URL;
  private items = [];

  constructor(private http: HttpClient) {}

  addItem(product: IAddProductReq) {
    // this.items.push(product);
    console.log(product);

    return this.http.post(`${this.API_URL + endpoints.cardAddItem}`, product);
  }

  getItems() {
    return this.http.get<IShopCardResponse>(
      `${this.API_URL + endpoints.cardGetItems}`
    );
  }

  changeQuantity(productId: number, quantity: number) {
    console.log(productId, quantity);
    return this.http.patch(
      `${this.API_URL + endpoints.cardChangeQuantity + '/' + productId}`,
      quantity
    );
  }

  removeItem(productId: number) {
    return this.http.delete(
      `${this.API_URL + endpoints.cardRemoveItem + '/' + productId}`
    );
  }

  clearCart() {
    this.items = [];
    return this.http.delete(`${this.API_URL + endpoints.cardClearItems}`);
  }
}
