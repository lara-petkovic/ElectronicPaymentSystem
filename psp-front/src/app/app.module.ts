import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { WebsocketModule } from 'src/websocket/websocket.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PspFormComponent } from './psp-form/psp-form.component';

@NgModule({
  declarations: [
    AppComponent,
    PspFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    WebsocketModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
