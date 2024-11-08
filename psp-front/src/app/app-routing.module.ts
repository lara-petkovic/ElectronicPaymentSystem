import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PspFormComponent } from './psp-form/psp-form.component';

const routes: Routes = [
  { path: 'payment-options/:id', component: PspFormComponent }, // Primer druge komponente
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 

}
