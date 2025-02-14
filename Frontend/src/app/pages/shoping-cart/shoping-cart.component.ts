import { Component, OnInit } from '@angular/core';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { ICartProducts } from 'src/app/shared/models/shop-cart.model';
import { ShoppingCartService } from 'src/app/shared/services/shopping-cart.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-shoping-cart',
  templateUrl: './shoping-cart.component.html',
  styleUrls: ['./shoping-cart.component.scss'],
})
export class ShopingCartComponent implements OnInit {
  productsInCart: ICartProducts[] = [];
  totalPrice = 0;
  productsPrice = 0;
  shipmentPrice = 19.99;
  API_IMG = environment.API_IMG;
  isLoading = false;

  constructor(
    private shoppingCartService: ShoppingCartService,
    private libary: FaIconLibrary
  ) {
    this.libary.addIconPacks(fas);
  }

  getPhotoUrl(photo, index) {
    return this.API_IMG + photo[index];
  }

  ngOnInit(): void {
    this.getShoppingCart();
  }

  getShoppingCart(): void {
    this.isLoading = true;
    this.shoppingCartService.getItems().subscribe({
      next: (data) => {
        this.productsInCart = data.data['data'].shopCardLines;
        this.productsPrice = data.data['data'].totalPrice;
        this.totalPrice = data.data['data'].totalPrice + this.shipmentPrice;
      },
      error: (error) => {
        console.error(error);
      },
      complete: () => {
        this.isLoading = false;
      },
    });
  }

  changeQuantity(productId: number, quantity: number): void {
    if (quantity >= 1) {
      this.shoppingCartService.changeQuantity(productId, quantity).subscribe({
        next: (_) => {},
        error: (error) => {
          console.error('Error updating quantity', error);
        },
        complete: () => {
          this.getShoppingCart();
        },
      });
    } else {
      this.removeFromCart(productId);
    }
  }

  removeFromCart(productId: number): void {
    this.shoppingCartService.removeItem(productId).subscribe({
      next: (_) => {
      },
      error: (error) => {
        console.error('Error removing item', error);
      },
      complete: () => {
        this.getShoppingCart();
      },
    });
  }

  finalizePurchase(): void {
    alert('To jest tylko wersja demonstracyjna, nie można dokonywać zakupów!');
  }
}
