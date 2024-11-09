import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PspFormComponent } from './psp-form/psp-form.component';
import { PspClientRegComponent } from './psp-client-reg/psp-client-reg.component';
import { SuccessPageComponent } from './success-page/success-page.component';
import { FailedPageComponent } from './failed-page/failed-page.component';
import { ErrorPageComponent } from './error-page/error-page.component';

const routes: Routes = [
  { path: 'payment-options/:id', component: PspFormComponent }, 
  { path: 'app-psp-client-reg', component: PspClientRegComponent }, 
  { path: 'success', component: SuccessPageComponent }, 
  { path: 'fail', component: FailedPageComponent }, 
  { path: 'error', component: ErrorPageComponent }, 


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 

}
