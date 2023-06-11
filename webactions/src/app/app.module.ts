import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import {routes} from './app-routing.module';
import { AppComponent } from './app.component';
import {RouterModule} from "@angular/router";
import { WebActionComponent } from './web-action/web-action.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { WebActionSuccessComponent } from './web-action-success/web-action-success.component';

@NgModule({
  declarations: [
    AppComponent,
    WebActionComponent,
    WebActionSuccessComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    HttpClientModule,
    BrowserModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
