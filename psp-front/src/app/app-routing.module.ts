import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PspFormComponent } from './psp-form/psp-form.component';
import { PspClientRegComponent } from './psp-client-reg/psp-client-reg.component';

const routes: Routes = [
  { path: 'payment-options/:id', component: PspFormComponent }, 
  { path: 'app-psp-client-reg', component: PspClientRegComponent }, 

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 

}
