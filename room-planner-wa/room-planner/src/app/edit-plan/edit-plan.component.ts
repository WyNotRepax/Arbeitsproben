import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import {
  ModalController,
  NavParams
} from '@ionic/angular';
import { NavController } from '@ionic/angular';

@Component({
  selector: 'app-edit-plan',
  templateUrl: './edit-plan.component.html',
  styleUrls: ['./edit-plan.component.scss'],
})
export class EditPlanComponent implements OnInit {
  id: number;

  form = this.fb.group({
    name: ['', Validators.required],
    dimensions: this.fb.group({
      width: ['', Validators.compose([Validators.required, Validators.min(1)])],
      height: ['', Validators.compose([Validators.required, Validators.min(1)])]
    })
  });

  constructor(private modalController: ModalController,
    private navParams: NavParams, public navCtrl: NavController, private fb: FormBuilder) { }

  ngOnInit() {
    this.id = this.navParams.data.id;
    this.form.patchValue(this.navParams.data);
  }

  async closeModal() {
    //console.log(this.form.value);
    await this.modalController.dismiss(this.form.value);
  }

  async cancelModal() {
    await this.modalController.dismiss(null);
  }

}
