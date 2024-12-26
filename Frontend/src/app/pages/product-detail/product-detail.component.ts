import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { environment } from 'src/environments/environment';

import { ProductsService } from 'src/app/shared/services/products.service';
import { ShoppingCartService } from 'src/app/shared/services/shopping-cart.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit {
  id: number = 0;
  product: any = {};
  selectedImage: number = 0;
  isLoading = false;
  isAddingToCart = false;
  API_IMG = environment.API_IMG;
  productSpecs = [
    { name: 'Producent', value: 'Acer' },
    { name: 'Model', value: 'Nitro 5' },
    { name: 'Procesor', value: 'Intel Core i5-10300H' },
    { name: 'Pamięć RAM', value: '8GB DDR4' },
    { name: 'Dysk', value: '512GB SSD' },
    { name: 'Karta graficzna', value: 'NVIDIA GeForce GTX 1650' },
    { name: 'System operacyjny', value: 'Windows 10 Home' },
    { name: 'Kolor', value: 'Czarny' },
    { name: 'Waga', value: '2.3 kg' },
    { name: 'Wysokość', value: '23.9 mm' },
    { name: 'Szerokość', value: '363.4 mm' },
    { name: 'Głębokość', value: '255 mm' },
    { name: 'Gwarancja', value: '24 miesiące' },
  ];

  constructor(
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private shoppingCartService: ShoppingCartService,
    private toastr: ToastrService
  ) {}

  onBuyProduct() {
    // console.log('Buy product ID: ', this.id);
    this.isAddingToCart = true;
    this.shoppingCartService
      .addItem({ product: this.product, quantity: 1 })
      .subscribe({
        next: (data) => {
          // console.log(data);
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
        complete: () => {
          this.isAddingToCart = false;
        },
      });
  }

  onImageClick(url) {
    // console.log('Image opened in new tab!');
    window.open(url, '_blank');
  }

  onPreviousImage() {
    if (this.selectedImage > 0) {
      this.selectedImage--;
    }
  }

  onNextImage() {
    if (this.selectedImage < this.product.photos.length - 1) {
      this.selectedImage++;
    }
  }

  get imageUrl() {
    return this.API_IMG + this.product.photos[this.selectedImage];
  }

  ngOnInit(): void {
    this.isLoading = true;
    this.route.params.subscribe((params: Params) => {
      this.id = params['id'];
      this.loadProduct();
    });
  }

  loadProduct() {
    this.productsService.getProduct(this.id).subscribe((product) => {
      this.product = product.data.product;
      this.selectedImage = this.product.mainPhotoId;
      this.isLoading = false;
      // console.log(this.product.photos[0]);
    });
  }
}
