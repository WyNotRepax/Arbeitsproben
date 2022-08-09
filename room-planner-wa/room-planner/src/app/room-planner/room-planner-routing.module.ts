import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RoomPlannerPage } from './room-planner.page';

const routes: Routes = [
  {
    path: ':planId',
    component: RoomPlannerPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RoomPlannerPageRoutingModule {}
