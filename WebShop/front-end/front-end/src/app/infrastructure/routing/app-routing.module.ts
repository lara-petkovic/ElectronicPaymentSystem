import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../auth/login/login.component';
import { RegistrationComponent } from '../auth/registration/registration.component';
import { AuthGuard } from '../auth/auth.guard';
import { HomeComponent } from 'src/app/feature-modules/home/home.component';
import { SubscribedItemsOverviewComponent } from 'src/app/feature-modules/subscribed-items-overview/subscribed-items-overview.component';

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegistrationComponent},
  {path: 'my-subscriptions', component: SubscribedItemsOverviewComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }