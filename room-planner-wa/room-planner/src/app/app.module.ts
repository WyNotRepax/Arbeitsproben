import { NgModule } from '@angular/core';
import { BrowserModule, HammerModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddPlanComponent } from './add-plan/add-plan.component';
import { EditPlanComponent } from './edit-plan/edit-plan.component';
import { EditFurnitureComponent } from './edit-furniture/edit-furniture.component';
import { AddFurnitureComponent } from './add-furniture/add-furniture.component';

import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { AddFurnitureCameraComponent } from './add-furniture-camera/add-furniture-camera.component';

@NgModule({
  declarations: [
    AppComponent,
    AddPlanComponent,
    EditPlanComponent,
    EditFurnitureComponent,
    AddFurnitureComponent,
    AddFurnitureCameraComponent
  ],
  entryComponents: [],
  imports: [
    BrowserModule,
    HammerModule,
    HttpClientModule,
    IonicModule.forRoot(),
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ZXingScannerModule
  ],
  providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy }],
  bootstrap: [AppComponent],
})
export class AppModule { }
