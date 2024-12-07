import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PspFormComponent } from './psp-form/psp-form.component';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { PspClientRegComponent } from './psp-client-reg/psp-client-reg.component';
import { SuccessPageComponent } from './success-page/success-page.component';
import { FailedPageComponent } from './failed-page/failed-page.component';
import { ErrorPageComponent } from './error-page/error-page.component';
import { RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { OptionsManagmentComponent } from './options-managment/options-managment.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    PspFormComponent,
    PspClientRegComponent,
    SuccessPageComponent,
    FailedPageComponent,
    ErrorPageComponent,
    HomeComponent,
    OptionsManagmentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatSortModule,
    MatPaginatorModule,
    MatTableModule,
    RouterModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
