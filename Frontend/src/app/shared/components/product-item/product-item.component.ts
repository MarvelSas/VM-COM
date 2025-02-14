import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { environment } from 'src/environments/environment';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { IAddProductReq, IProduct } from '../../models/product.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss'],
})
export class ProductItemComponent implements OnInit {
  // @Input() id: number;
  // @Input() photos: any;
  // @Input() name: any;
  // @Input() description: any;
  // @Input() price: any;
  // @Input() mainPhotoId: number;
  @Input() product: IProduct;
  shortDescription: string;

  API_IMG = environment.API_IMG;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private shoppingCartService: ShoppingCartService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.shortDescription = this.product.description.slice(0, 50) + '...';
  }

  get imageUrl() {
    return this.API_IMG + this.product.photos[this.product.mainPhotoId];
  }

  onSelectItem(event: Event) {
    this.router.navigate(['product', this.product.id]);
  }

  addToCart(event: Event) {
    event.stopPropagation();

    this.shoppingCartService
      .addItem({ product: this.product, quantity: 1 })
      .subscribe({
        next: (res) => {
          this.toastr.success('Dodano do koszyka!', null, {
            positionClass: 'toast-bottom-right',
          });
        },
        error: (error) => {
          console.error(error);
          this.toastr.error('Błąd przy dodawaniu!', null, {
            positionClass: 'toast-bottom-right',
          });
        },
        complete: () => {},
      });
  }
}
