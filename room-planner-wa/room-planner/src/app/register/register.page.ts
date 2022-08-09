import { Component, OnInit } from '@angular/core';
import { Router } from  '@angular/router';
import {UserEndpointService} from '../service/user-endpoint.service';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
})
export class RegisterPage implements OnInit {

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    confirm: ['', Validators.required]
  });

  constructor(private  router:  Router, public userEndpointService: UserEndpointService,
              private fb: FormBuilder) { }

  ngOnInit() {
  }

  register(){
    if(this.form.value.password === this.form.value.confirm && this.form.value.password) {
      this.userEndpointService.addUser(this.form.value).then(r => {
        console.log('login');
        this.router.navigateByUrl('login');
      });
    }
  }


}
