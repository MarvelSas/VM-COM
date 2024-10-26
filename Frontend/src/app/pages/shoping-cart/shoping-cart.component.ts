import { Component } from '@angular/core';
import { IProduct } from 'src/app/shared/models/product.model';

@Component({
  selector: 'app-shoping-cart',
  templateUrl: './shoping-cart.component.html',
  styleUrls: ['./shoping-cart.component.scss'],
})
export class ShopingCartComponent {
  productsInCart: IProduct[] = [
    {
      id: 1,
      name: 'Nothing Phone',
      price: '2499.00',
      photos: ['http://localhost:8080/api/v1/product/images/nothing1.jpg'],
      mainPhotoId: 1,
      productCategory: { id: 1, name: 'Telefony' },
      description: 'Telefon Nothing Phone',
    },
  ];
}
