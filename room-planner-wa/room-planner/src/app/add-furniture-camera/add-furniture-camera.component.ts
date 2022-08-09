import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ModalController, NavController, NavParams } from '@ionic/angular';

@Component({
  selector: 'app-add-furniture-camera',
  templateUrl: './add-furniture-camera.component.html',
  styleUrls: ['./add-furniture-camera.component.scss'],
})
export class AddFurnitureCameraComponent implements OnInit {
  cameras = [];

  form = this.fb.group({
    name: ['', Validators.required],
    color: ['', Validators.required],
    dimensions: this.fb.group({
      width: ['', Validators.compose([Validators.required, Validators.min(1)])],
      height: ['', Validators.compose([Validators.required, Validators.min(1)])]
    })
  });


  qrResultString: string;
  private obj: any;

  constructor(private modalController: ModalController,
    private navParams: NavParams, public navCtrl: NavController, private fb: FormBuilder) { }

  ngOnInit() { }

  onCodeResult(resultString: string) {
    this.qrResultString = resultString;
    this.obj = JSON.parse(this.qrResultString);
    console.log(this.obj);

    this.form.patchValue(this.obj);
  }

  async closeModal() {
    //console.log(this.form.value);
    await this.modalController.dismiss(this.form.value);
  }

  async cancelModal() {
    await this.modalController.dismiss(null);
  }

  public onCamerasFound(info){
    this.cameras = info;
    console.log(info);
  }

}
