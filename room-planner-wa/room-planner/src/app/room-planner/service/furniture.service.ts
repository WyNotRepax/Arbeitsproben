import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Furniture } from 'src/app/entity/furniture.interface';
import { FurnitureInPlan } from 'src/app/entity/plan.interface';
import { FurnitureEndpointService } from 'src/app/service/furniture-endpoint.service';
import { PlanEndpointService } from 'src/app/service/plan-endpoint.service';

@Injectable({
  providedIn: 'root'
})
export class FurnitureService {

  public readonly furniture: Observable<Furniture[]>;

  private readonly furniture$: BehaviorSubject<Furniture[] | null>;

  constructor(private furnitureService: FurnitureEndpointService, private planService: PlanEndpointService) {
    this.furniture$ = new BehaviorSubject<Furniture[] | null>(null);
    this.furniture = this.furniture$.pipe(filter(furniture => furniture !== null),map(furniture => furniture as Furniture[]));
    furnitureService.getFurnitures(0,100).then(page => this.furniture$.next(page.data));
  }

  public async getFurniture(furnitureInPlan: FurnitureInPlan): Promise<Furniture> {
    return this.furnitureService.getFurniture(furnitureInPlan.furnitureId);
  }

  public async updateFurnitureInPlan(planId: number, furniture: FurnitureInPlan){
    return this.planService.updateFurnitureInPlan(planId,furniture.id,furniture);
  }
}
