import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreditCardComponent } from './credit-card/credit-card.component';
import { QrCodeComponent } from './qr-code/qr-code.component';

const routes: Routes = [
  { path: 'credit-card-input/:paymentId/:amount', component: CreditCardComponent }, // Default route
  { path: 'qr-code', component: QrCodeComponent } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
