import { Routes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { SuccessComponent } from './components/success/success.component';
import { HomeComponent } from './components/home/home.component';

export const routes: Routes = [
  { path: 'product-list', component: ProductListComponent },
  { path: '', component: HomeComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'success', component: SuccessComponent}
];