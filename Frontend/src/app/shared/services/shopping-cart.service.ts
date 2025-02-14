import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { endpoints } from 'src/enums/endpoints.enum';
import { environment } from 'src/environments/environment';
import { IAddProductReq, IProduct } from '../models/product.model';
import { IApiResponse } from '../models/api-response.model';
import { IShopCard } from '../models/shop-cart.model';
import { BehaviorSubject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ShoppingCartService {
  API_URL = environment.API_URL;
  private items = [];
  public cartQuantitySubject = new BehaviorSubject<number>(0);

  constructor(private http: HttpClient) {}

  addItem(product: IAddProductReq) {
    this.cartQuantitySubject.next(this.cartQuantitySubject.value + 1);

    return this.http.post(`${this.API_URL + endpoints.cardAddItem}`, product);
  }

  getItems() {
    return this.http
      .get<IApiResponse<IShopCard>>(`${this.API_URL + endpoints.cardGetItems}`)
      .pipe(
        tap((res: IApiResponse<IShopCard>) => {
          const totalQuantity = res.data.data.shopCardLines.reduce(
            (sum, item) => sum + item.quantity,
            0
          );
          this.cartQuantitySubject.next(totalQuantity);
        })
      );
  }

  changeQuantity(productId: number, quantity: number) {
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
    this.cartQuantitySubject.next(0);
    return this.http.delete(`${this.API_URL + endpoints.cardClearItems}`);
  }
}
