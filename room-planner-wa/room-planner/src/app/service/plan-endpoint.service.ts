import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Page } from '../entity/page.interface';
import { FurnitureInPlan, FurnitureInPlanCreateUpdate, PlanCreateUpdate, PlanDetail, PlanListing } from '../entity/plan.interface';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class PlanEndpointService {


  private readonly endpoint = environment.server + '/api/v1/plan';

  constructor(private http: HttpClient) { }

  public async getPlans(pageIndex: number, pageSize: number):
    Promise<Page<PlanListing>> {
    const params = new HttpParams()
      .set('page[index]', pageIndex)
      .set('page[size]', pageSize);
    return this.http.get<Page<PlanListing>>(
      this.endpoint,
      { params, withCredentials: true }
    ).toPromise();
  }

  public async getPlan(planId: number):
    Promise<PlanDetail> {
    return this.http.get<PlanDetail>(this.endpoint + '/' + planId.toFixed(), { withCredentials: true }).toPromise();
  }

  public async putPlan(planId: number, plan: PlanCreateUpdate):
    Promise<PlanCreateUpdate> {
    return this.http.put<PlanCreateUpdate>(this.endpoint + '/' + planId.toFixed(), plan, { withCredentials: true }).toPromise();
  }

  public async removePlan(planId: number):
    Promise<void> {
    return this.http.delete<void>(this.endpoint + '/' + planId.toFixed(), { withCredentials: true }).toPromise();
  }

  public async updateFurnitureInPlan(
    planId: number,
    furnitureId: number,
    furniture: FurnitureInPlanCreateUpdate
  ): Promise<void> {
    return this.http.put<void>(
      this.endpoint + '/' + planId + '/furniture/' + furnitureId,
      furniture,
      { withCredentials: true }
    ).toPromise();
  }

  public async createFurniutureInPlan(
    planId: number,
    furniture: FurnitureInPlanCreateUpdate
  ):
    Promise<void> {
    return this.http.post<void>(
      this.endpoint + '/' + encodeURIComponent(planId) + '/furniture',
      furniture,
      { withCredentials: true }
    ).toPromise();
  }

  public async addPlan(plan: PlanListing):
    Promise<PlanListing> {
    return this.http.post<PlanListing>(this.endpoint, plan, { withCredentials: true }).toPromise();
  }

}

