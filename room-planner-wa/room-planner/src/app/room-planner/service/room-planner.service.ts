import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PlanDetail } from 'src/app/entity/plan.interface';
import { PlanEndpointService } from 'src/app/service/plan-endpoint.service';

@Injectable({
  providedIn: 'root'
})
export class RoomPlannerService {

  public readonly plan: Observable<PlanDetail>;

  private readonly plan$: BehaviorSubject<PlanDetail | null>;


  private planId?: number;

  constructor(private endpointService: PlanEndpointService) {
    this.plan$ = new BehaviorSubject<PlanDetail | null>(null);
    this.plan = this.plan$.pipe(filter(plan => plan !== null), map(plan => plan as PlanDetail));
  }

  public setPlanId(planId: number): void {
    if (planId !== this.planId) {
      this.planId = planId;
      this.refresh();
    }
  }

  public refresh(): void {
    this.endpointService.getPlan(this.planId).then(plan => this.plan$.next(plan));
  }

  public addFurniture(furnitureId: number) {
    if (this.planId) {
      this.endpointService.createFurniutureInPlan(this.planId, {
          furnitureId,
          position: {x:0,y:0,z:0,rotation:0}
      }).then(_ => this.refresh());
    }
  }
}
