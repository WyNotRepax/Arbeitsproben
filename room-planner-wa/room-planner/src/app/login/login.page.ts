import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserEndpointService } from '../service/user-endpoint.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {

  constructor(private userService: UserEndpointService, private router: Router) { }

  ngOnInit() {
  }

  login(form: {email: string; password: string}){
    this.userService.login(form.email,form.password).then(successfull =>{
      if(successfull){
        this.router.navigate(['tabs']);
      }else{
        console.log('bad Login');
      }
    });
  }
}
