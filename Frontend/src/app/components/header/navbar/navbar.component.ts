import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  of,
  Subscription,
  switchMap,
} from 'rxjs';
import { IProductResponseData } from 'src/app/pages/admin/admin-products/product.model';
import { IProduct } from 'src/app/shared/models/product.model';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ProductsService } from 'src/app/shared/services/products.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  searchCategory: string = 'Kategoria';
  userSub: Subscription;
  searchSub: Subscription;
  user = null;
  searchResults: IProduct[] = [];

  constructor(
    private authService: AuthService,
    private productsService: ProductsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe((res) => {
      this.user = res;
    });
  }

  onSearch(event: Event) {
    const inputValue = (event.target as HTMLInputElement).value;

    if (this.searchSub) {
      this.searchSub.unsubscribe();
    }

    this.searchSub = of(inputValue)
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((term) =>
          this.productsService
            .getProductsByName(term)
            .pipe(catchError(() => of({ data: { products: [] } })))
        )
      )
      .subscribe((response) => {
        const products = response.data?.products || [];
        this.searchResults = products;
        console.log(this.searchResults);
      });
  }

  onSelectProduct(product: IProduct) {
    console.log(`Selected product: ${product.name}`);
  }

  logout() {
    this.authService.signOut();
  }

  onChangeCategory(e: any) {
    // console.log(e.target.textContent);
    this.searchCategory = e.target.textContent;
  }

  onSelectItem(id: number) {
    this.router.navigate(['product', id]);
  }

  ngOnDestroy(): void {
    if (this.userSub) {
      this.userSub.unsubscribe();
    }
    if (this.searchSub) {
      this.searchSub.unsubscribe();
    }
  }
}
