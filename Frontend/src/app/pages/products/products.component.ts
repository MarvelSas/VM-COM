import { Component } from '@angular/core';
import { IPageableParams, IProduct } from 'src/app/shared/models/product.model';
import { ProductsService } from 'src/app/shared/services/products.service';
import { ICategory } from '../admin/admin-categories/category.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent {
  products: IProduct[] = [];
  isLoading = false;

  avalibleCategories: ICategory[] = [];

  currentPage: number = 1;
  totalPages: number = 1;
  totalAmountOfItems: number = 0;
  itemsPerPage: number = 10;
  category: string = 'All';
  minPrice: number | undefined = undefined;
  maxPrice: number | undefined = undefined;
  inStock: boolean = true;
  sortBy: string = 'name';
  order: string = 'asc';
  name: string = '';

  constructor(
    private productsService: ProductsService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.getCategories();

    this.route.queryParams.subscribe((queryParams) => {
      if (queryParams['category']) {
        this.category = queryParams['category'];
      }

      this.getPageableProducts();
    });
  }

  getCategories() {
    this.productsService.getCategories().subscribe({
      next: (res) => {
        this.avalibleCategories = res.data.productCategories;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  getProducts() {
    this.isLoading = true;

    this.productsService.getProducts().subscribe({
      next: (res) => {
        this.products = res.data.products;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
      },
    });

    this.productsService.getProducts().subscribe((_) => {});
  }

  getPageableProducts() {
    const newParams: IPageableParams = {
      page: this.currentPage,
      pageSize: this.itemsPerPage,
      category: this.category,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      sortBy: this.sortBy,
      order: this.order,
    };

    this.isLoading = true;
    this.productsService.getPageableProducts(newParams).subscribe({
      next: (res) => {
        this.products = res.data.products;
        this.totalPages = res.data.totalAmountOfPages;
        this.totalAmountOfItems = res.data.totalAmountOfItems;
        this.isLoading = false;
      },
      error: (err) => {
        console.error(err);
      },
    });
  }

  pageChanged(event: any) {
    if (this.currentPage !== event.page) {
      this.currentPage = event.page;
      this.getPageableProducts();
    }
  }

  changePageSize(newPageSize: string) {
    this.itemsPerPage = parseInt(newPageSize);
    this.currentPage = 1;
    this.getPageableProducts();
  }

  changeCategory(newCategory: string) {
    this.category = newCategory;
    this.currentPage = 1;
    this.router.navigate([], {
      queryParams: {
        category: newCategory,
        minPrice: this.minPrice,
        maxPrice: this.maxPrice,
      },
      queryParamsHandling: 'merge',
    });
    this.getPageableProducts();
  }
}
