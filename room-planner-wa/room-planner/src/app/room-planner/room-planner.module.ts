import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { RoomPlannerPageRoutingModule } from './room-planner-routing.module';

import { RoomPlannerPage } from './room-planner.page';
import { MainComponent } from './components/main/main.component';
import { FurnitureDirective } from './directives/furniture.directive';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RoomPlannerPageRoutingModule
  ],
  declarations: [RoomPlannerPage, MainComponent, FurnitureDirective]
})
export class RoomPlannerPageModule { }
