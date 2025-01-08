import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IProduct } from 'src/app/shared/models/product.model';
import { ProductsService } from 'src/app/shared/services/products.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.scss'],
})
export class LandingPageComponent implements OnInit {
  MAX_PRODUCTS = 4;
  isLoading = false;

  products: IProduct[] = [];

  constructor(
    private productsService: ProductsService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.isLoading = true;
    this.productsService.getProducts().subscribe((res) => {
      this.products = res.data.products.splice(0, this.MAX_PRODUCTS); // GET ONLY 5 PRODUCTS
      this.isLoading = false;
    });
  }

  onShowMore() {
    this.router.navigate(['products']);
  }
}
