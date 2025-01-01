import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import { ShopingCartComponent } from './pages/shoping-cart/shoping-cart.component';
import { VerifyEmailComponent } from './pages/verify-email/verify-email.component';
import { ProductsComponent } from './pages/products/products.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AdminProductsComponent } from './pages/admin/admin-products/admin-products.component';
import { AdminCategoriesComponent } from './pages/admin/admin-categories/admin-categories.component';
import { AuthGuard } from './shared/guards/auth.guard';
import { UserProfileComponent } from './pages/user-profile/user-profile.component';
import { RoleGuard } from './shared/guards/role.guard';
import { HelpUserComponent } from './pages/help-user/help-user.component';
import { FavoritesProductsComponent } from './pages/favorites-products/favorites-products.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'products', component: ProductsComponent },
  {
    path: 'product/:id',
    component: ProductDetailComponent,
  },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [AuthGuard],
  },
  { path: 'cart', component: ShopingCartComponent, canActivate: [AuthGuard] },
  { path: 'verify-email', component: VerifyEmailComponent },
  { path: 'help', component: HelpUserComponent },
  { path: 'favorites', component: FavoritesProductsComponent },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ROLE_ADMIN'] },
    children: [
      { path: 'products', component: AdminProductsComponent },
      { path: 'categories', component: AdminCategoriesComponent },
    ],
  },

  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
