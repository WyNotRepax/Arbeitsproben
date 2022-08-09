import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import {
  ModalController,
  NavParams
} from '@ionic/angular';
import { NavController } from '@ionic/angular';


@Component({
  selector: 'app-add-furniture',
  templateUrl: './add-furniture.component.html',
  styleUrls: ['./add-furniture.component.scss'],
})
export class AddFurnitureComponent implements OnInit {
  form = this.fb.group({
    name: ['', Validators.required],
    color: ['', Validators.required],
    dimensions: this.fb.group({
      width: ['', Validators.compose([Validators.required, Validators.min(1)])],
      height: ['', Validators.compose([Validators.required, Validators.min(1)])]
    })
  });

  constructor(private modalController: ModalController,
              private navParams: NavParams, public navCtrl: NavController, private fb: FormBuilder) { }

  ngOnInit() {}

  async closeModal() {
    //console.log(this.form.value);
    await this.modalController.dismiss(this.form.value);
  }

  async cancelModal() {
    await this.modalController.dismiss(null);
  }
}
