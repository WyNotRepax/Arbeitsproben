import { HttpClient, HttpParams, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NavParams } from '@ionic/angular';
import { stringify } from 'querystring';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserEndpointService {

  private readonly endpoint = environment.server + '/api/v1/user';
  private readonly loginEndpoint = environment.server + '/j_security_check';

  constructor(private http: HttpClient) { }

  public async addUser(param: any): Promise<any> {
    return this.http.post<any>(this.endpoint, param).toPromise();
  }

  public async login(username: string, password: string): Promise<boolean> {
    const formData = new FormData();
    formData.append('j_username', username);
    formData.append('j_password', password);
    return this.http.post(
      this.loginEndpoint,
      formData,
      { responseType: 'text', observe: 'response', withCredentials: true }
    ).toPromise().then(response => {
      console.log(response.url);
      const params = new HttpParams({ fromString: response.url.split('?')[1] });
      console.log(params);
      return params.get('error') !== 'true';
    }).catch(_ => {console.log(_);  return false;});
  }
}
