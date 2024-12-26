import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { adminProductsService } from './admin-products.service';
import { IProduct } from 'src/app/shared/models/product.model';
import { ICategory } from '../admin-categories/category.model';
import { adminCategoriesService } from '../admin-categories/admin-categories.service';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { IProductNew } from './product.model';
import { IImage } from 'src/app/shared/models/image.model';

@Component({
  selector: 'app-admin-products',
  templateUrl: './admin-products.component.html',
  styleUrls: ['./admin-products.component.scss'],
})
export class AdminProductsComponent implements OnInit {
  isEditing: boolean = false;
  editProductId: number = null;
  addProductForm: FormGroup;
  products: IProduct[] = [];
  categories: ICategory[];
  formData: FormData = new FormData();
  characterCount: number = 0;
  images: IImage[] = [];
  imagesName: string[] = [];
  selectedMainPhoto: number = 0;
  isLoading = false;

  constructor(
    private adminProductsService: adminProductsService,
    private adminCategoriesService: adminCategoriesService,
    private toastr: ToastrService
  ) {}

  // INICJALIZACJA
  ngOnInit(): void {
    this.formInit();
    this.getProducts();
  }

  formInit() {
    this.addProductForm = new FormGroup({
      productName: new FormControl(null, Validators.required),
      productDescription: new FormControl(null, [
        Validators.required,
        Validators.maxLength(8000),
      ]),
      productPrice: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[\d,\.]+$/),
      ]),
      productAmount: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^\d+$/),
      ]),
      productImage: new FormControl(null),
      productCategory: new FormControl(null, Validators.required),
    });

    this.addProductForm
      .get('productDescription')!
      .valueChanges.subscribe((value) => {
        this.characterCount = value ? value.length : 0;
      });
  }

  getProducts() {
    this.isLoading = true;
    this.adminProductsService.getProducts().subscribe({
      next: (res) => {
        this.products = res.data.products;
      },
      error: (err) => {
        console.error(err);
      },
      complete: () => {
        this.isLoading = false;
      },
    });
  }

  selectMainPhoto(index) {
    console.log('Selected main photo: ' + index);
    // console.log(index);
    this.selectedMainPhoto = index;
    // console.log(this.selectedMainPhoto);
  }

  // OLD
  // onAddFile(event: Event) {
  //   const file = (event.target as HTMLInputElement).files[0];
  //   this.formData.append('picture', file);
  // }

  // PRZESYŁANIE ZDJĘĆ NA SERWER
  onAddImage(e: any) {
    for (let i = 0; i < e.target.files.length; i++) {
      this.adminProductsService.uploadPhoto(e.target.files[i]).subscribe({
        next: (res) => {
          this.images.push({
            imageUrl: environment.API_IMG + res.data.productPhotoName,
            isSelected: false,
          });
          this.imagesName.push(res.data.productPhotoName);
          // console.log(res.data.productPhotoName);
          console.log(environment.API_IMG + res.data.productPhotoName);
        },
        error: (err) => {
          console.error(err);
        },
      });
    }
  }

  onEditProduct(id: number) {
    this.isEditing = true;
    this.editProductId = id;
    const editedProduct: IProduct = this.products[id - 1];
    this.images = editedProduct.photos.map((photo) => {
      return { imageUrl: environment.API_IMG + photo, isSelected: false };
    });
    this.imagesName = editedProduct.photos;
    this.selectMainPhoto(editedProduct.mainPhotoId);
    this.addProductForm.setValue({
      productName: editedProduct.name,
      productDescription: editedProduct.description,
      productPrice: editedProduct.price,
      productAmount: 5,
      productImage: null,
      productCategory: editedProduct.productCategory.id,
    });

    this.imagesName = this.products[id].photos;
    this.images = this.products[id].photos.map((photo) => {
      return { imageUrl: environment.API_IMG + photo, isSelected: false };
    });
  }

  onDeleteProduct(id: number) {
    this.adminProductsService.deleteProduct(id).subscribe({
      next: (res) => {
        console.log(res);
        this.products = this.products.filter((product) => product.id !== id);
        if (res.statusCode === 200) {
          this.toastr.success('Pomyślnie dodano produkt!', null, {
            positionClass: 'toast-bottom-right',
          });
        }
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  // PRZESŁANIE FORMULARZA Z PRODUKTEM NA SERWER
  onSubmitNew() {
    if (!this.addProductForm.valid) {
      return;
    }

    const productName = this.addProductForm.value.productName;
    const productPrice = this.addProductForm.value.productPrice;
    const productDescription = this.addProductForm.value.productDescription;
    const productAmount = this.addProductForm.value.productAmount;
    const productCategory =
      this.categories[this.addProductForm.value.productCategory - 1];

    // WYODRĘBNIA Z TABLICY TYLKO URL ZDJĘĆ
    // const imagesUrls = this.images.map((image) => {
    //   return image.imageUrl;
    // });

    const product: IProductNew = {
      id: this.isEditing ? this.editProductId : null,
      name: productName,
      price: productPrice,
      productCategory: productCategory,
      amount: productAmount,
      description: productDescription,
      photos: this.imagesName,
      mainPhotoId: this.selectedMainPhoto,
    };

    // this.formData.append(
    //   'product',
    //   new Blob([JSON.stringify(product)], { type: 'application/json' })
    // );

    console.log(product);

    // DODANIE DO FORMULARZA URL ZDJĘĆ
    // this.formData.append('images', JSON.stringify(this.images));

    if (!this.isEditing) {
      this.adminProductsService.addProductNew(product).subscribe({
        next: (res) => {
          if (res.statusCode === 200) {
            this.toastr.success('Pomyślnie dodano produkt!', null, {
              positionClass: 'toast-bottom-right',
            });
          }
        },
        error: (err) => {
          console.error(err.message);
          this.toastr.error('Błąd dodawania produktu!', null, {
            positionClass: 'toast-bottom-right',
          });
        },
      });
    } else {
      this.adminProductsService
        .editProduct(this.editProductId, product)
        .subscribe({
          next: (res) => {
            console.log(res);
            this.isEditing = false;
          },
          error: (err) => {
            console.error(err.message);
          },
        });
    }

    // this.adminProductsService.addProductNew(this.formData).subscribe();

    this.onClear();
  }

  //
  // OLD
  //
  // onSubmit() {
  //   if (!this.addProductForm.valid) {
  //     return;
  //   }

  //   const productName = this.addProductForm.value.productName;
  //   const productPrice = this.addProductForm.value.productPrice;
  //   const productDescription = this.addProductForm.value.productDescription;
  //   const imageUrl = '';
  //   const productCategory =
  //     this.categories[this.addProductForm.value.productCategory - 1];

  //   this.adminProductsService
  //     .addProduct(
  //       productName,
  //       productPrice,
  //       imageUrl,
  //       productDescription,
  //       productCategory
  //     )
  //     .subscribe((res) => {
  //       console.log(res);
  //     });
  // }

  // WYCZYSZCZENIE FORMULARZA
  onClear() {
    this.addProductForm.reset();
    this.images = [];
    this.imagesName = [];
  }
}
