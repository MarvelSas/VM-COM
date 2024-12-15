import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/header/navbar/navbar.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { ShopingCartComponent } from './pages/shoping-cart/shoping-cart.component';
import { CategoriesComponent } from './components/header/categories/categories.component';
import { VerifyEmailComponent } from './pages/verify-email/verify-email.component';
import { ProductItemComponent } from './shared/components/product-item/product-item.component';
import { SlideshowComponent } from './shared/slideshow/slideshow.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductsComponent } from './pages/products/products.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AdminCategoriesComponent } from './pages/admin/admin-categories/admin-categories.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';
import { Toast, ToastrModule } from 'ngx-toastr';
import { AuthInterceptorService } from './shared/services/auth-interceptor.service';
import { ProductListItemComponent } from './shared/components/product-list-item/product-list-item.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { HasRoleDirective } from './shared/directives/has-role.directive';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    LandingPageComponent,
    HeaderComponent,
    FooterComponent,
    NavbarComponent,
    PageNotFoundComponent,
    ShopingCartComponent,
    CategoriesComponent,
    VerifyEmailComponent,
    ProductItemComponent,
    SlideshowComponent,
    ProductsComponent,
    ProductDetailComponent,
    AdminComponent,
    AdminCategoriesComponent,
    AdminProductsComponent,
    LoadingSpinnerComponent,
    ProductListItemComponent,
    UserProfileComponent,
    HasRoleDirective,
  ],
  imports: [
    FontAwesomeModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    ToastrModule.forRoot(),
    TabsModule,
    FormsModule,
    PaginationModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
