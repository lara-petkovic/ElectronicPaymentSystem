import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { WebsocketModule } from 'src/websocket/websocket.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PspFormComponent } from './psp-form/psp-form.component';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    PspFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    WebsocketModule,
    MatTableModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
