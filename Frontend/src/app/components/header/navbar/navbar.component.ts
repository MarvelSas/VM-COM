import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  of,
  Subscription,
  switchMap,
} from 'rxjs';
import { IProduct } from 'src/app/shared/models/product.model';
import { AuthService } from 'src/app/shared/services/auth.service';
import { ProductsService } from 'src/app/shared/services/products.service';
import { RoleService } from 'src/app/shared/services/role.service';
import { ShoppingCartService } from 'src/app/shared/services/shopping-cart.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  @ViewChild('searchInput') searchInput: ElementRef;
  @ViewChild('searchResultsContainer') searchResultsContainer: ElementRef;
  searchCategory: string = 'Wszystkie';
  userSub: Subscription;
  searchSub: Subscription;
  user = null;
  searchResults: IProduct[] = [];
  API_IMG = environment.API_IMG;
  cartCount = 0;

  constructor(
    private authService: AuthService,
    protected roleService: RoleService,
    private productsService: ProductsService,
    private router: Router,
    private cartService: ShoppingCartService
  ) {}

  ngOnInit(): void {
    document.addEventListener('click', this.onDocumentClick.bind(this));

    this.userSub = this.authService.user.subscribe((res) => {
      this.user = res;
    });

    this.cartService.cartQuantitySubject.subscribe((cartQuantity) => {
      this.cartCount = cartQuantity;
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
            .getProductsByName(term, this.searchCategory)
            .pipe(catchError(() => of({ data: { products: [] } })))
        )
      )
      .subscribe({
        next: (res) => {
          const products = res.data?.products || [];
          this.searchResults = products;
          console.log(this.searchResults);
        },
        error: (error) => {
          console.error(error);
        },
      });
  }

  onSelectProduct(product: IProduct) {
    console.log(`Selected product: ${product.name}`);
    this.router.navigate(['product', product.id]);
    this.closeSearchResults();
    this.clearSearchInput();
  }

  closeSearchResults() {
    this.searchResults = [];
  }

  clearSearchInput() {
    this.searchInput.nativeElement.value = '';
  }

  logout() {
    this.authService.signOut();
  }

  onChangeCategory(e: any) {
    this.searchCategory = e.target.textContent;
  }

  onDocumentClick(event: MouseEvent) {
    if (
      this.searchResultsContainer &&
      !this.searchResultsContainer.nativeElement.contains(event.target) &&
      !this.searchInput.nativeElement.contains(event.target)
    ) {
      this.closeSearchResults();
    }
  }

  ngOnDestroy(): void {
    if (this.userSub) {
      this.userSub.unsubscribe();
    }
    if (this.searchSub) {
      this.searchSub.unsubscribe();
    }

    document.removeEventListener('click', this.onDocumentClick.bind(this));
  }
}
