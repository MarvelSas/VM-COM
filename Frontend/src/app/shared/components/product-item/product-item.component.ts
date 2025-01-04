import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss'],
})
export class ProductItemComponent {
  @Input() id: number;
  @Input() photos: any;
  @Input() name: any;
  @Input() price: any;
  @Input() mainPhotoId: number;
  API_IMG = environment.API_IMG;
  // console.log(this.name)

  constructor(private router: Router, private route: ActivatedRoute) {}

  get imageUrl() {
    return this.API_IMG + this.photos[this.mainPhotoId];
  }

  onSelectItem() {
    this.router.navigate(['product', this.id]);
    // console.log(this.id);
  }

  addToCart(event: Event) {
    event.stopPropagation();
    console.log('add to cart');
  }
}
