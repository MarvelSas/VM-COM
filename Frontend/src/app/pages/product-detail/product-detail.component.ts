import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { environment } from 'src/environments/environment';

import { ProductsService } from 'src/app/shared/services/products.service';
import { ShoppingCartService } from 'src/app/shared/services/shopping-cart.service';
import { ToastrService } from 'ngx-toastr';
import { IProduct } from 'src/app/shared/models/product.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit {
  id: number = 0;
  product: IProduct;
  selectedImage: number = 0;
  isLoading = false;
  isAddingToCart = false;
  API_IMG = environment.API_IMG;
  selectedModalImage: string | null = null;
  isModalOpen = false;
  stars: number[] = [0, 1, 2, 3, 4];
  rating: number = 4;

  constructor(
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private shoppingCartService: ShoppingCartService,
    private toastr: ToastrService
  ) {}

  onBuyProduct() {
    this.isAddingToCart = true;
    this.shoppingCartService
      .addItem({ product: this.product, quantity: 1 })
      .subscribe({
        next: (data) => {
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

  onPreviousImageInModal() {
    if (this.selectedImage > 0) {
      this.selectedImage--;
      this.selectedModalImage = this.imageUrl;
    }
  }

  onNextImageInModal() {
    if (this.selectedImage < this.product.photos.length - 1) {
      this.selectedImage++;
      this.selectedModalImage = this.imageUrl;
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

    addEventListener('keydown', (event) => {
      if (this.isModalOpen) {
        if (event.key === 'ArrowLeft') {
          this.onPreviousImageInModal();
        } else if (event.key === 'ArrowRight') {
          this.onNextImageInModal();
        }
      }
    });
  }

  openImage(image: string) {
    this.selectedModalImage = image;
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  loadProduct() {
    this.productsService.getProduct(this.id).subscribe((product) => {
      this.product = product.data.product;
      this.selectedImage = this.product.mainPhotoId;
      this.isLoading = false;
    });
  }
}
