import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-product-list-item',
  templateUrl: './product-list-item.component.html',
  styleUrls: ['./product-list-item.component.scss'],
})
export class ProductListItemComponent {
  @Input() id: number;
  @Input() photos: any;
  @Input() name: any;
  @Input() price: any;
  @Input() mainPhotoId: number;
  @Input() description: any;
  API_IMG = environment.API_IMG;

  constructor(private router: Router, private route: ActivatedRoute) {}

  get imageUrl() {
    return this.API_IMG + this.photos[this.mainPhotoId];
  }

  onSelectItem() {
    this.router.navigate(['product', this.id]);
  }
}
