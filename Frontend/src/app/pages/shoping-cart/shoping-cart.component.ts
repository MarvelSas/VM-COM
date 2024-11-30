import { Component, OnInit } from '@angular/core';
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
  API_IMG = environment.API_IMG;

  constructor(private shoppingCartService: ShoppingCartService) {}

  getPhotoUrl(photo, index) {
    return this.API_IMG + photo[index - 1];
  }

  ngOnInit(): void {
    this.shoppingCartService.getItems().subscribe({
      next: (data) => {
        this.productsInCart = data.data['data'].shopCardLines;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
}
