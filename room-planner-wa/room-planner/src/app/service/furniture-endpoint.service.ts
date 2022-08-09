import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Furniture } from '../entity/furniture.interface';
import { catchError } from 'rxjs/operators';
import { of, throwError } from 'rxjs';
import { Page } from '../entity/page.interface';
import { PlanCreateUpdate, PlanListing } from '../entity/plan.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FurnitureEndpointService {

  private readonly endpoint = environment.server + '/api/v1/furniture';

  constructor(private http: HttpClient) {

  }

  public async getFurnitures(pageIndex: number, pageSize: number): Promise<Page<Furniture>> {
    const params = new HttpParams()
      .set('page[index]', pageIndex)
      .set('page[size]', pageSize);
    return this.http.get<Page<Furniture>>(this.endpoint, { params, withCredentials: true },).toPromise();
  }

  public async getFurniture(id: number): Promise<Furniture> {
    return this.http.get<Furniture>(this.endpoint + '/' + encodeURIComponent(id), { withCredentials: true }).toPromise();
  }

  public async addFurniture(furn: Furniture): Promise<Furniture> {
    return this.http.post<Furniture>(this.endpoint, furn, { withCredentials: true }).toPromise();
  }

  public async putFurniture(furnId: number, furn: Furniture): Promise<Furniture> {
    return this.http.put<Furniture>(this.endpoint + '/' + furnId.toFixed(), furn, { withCredentials: true }).toPromise();
  }

  public async removeFurniture(furnId: number) {
    return this.http.delete(this.endpoint + '/' + furnId.toFixed(), { withCredentials: true }).toPromise();
  }

}
