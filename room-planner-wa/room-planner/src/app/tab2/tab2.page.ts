import { Component, ViewChild } from '@angular/core';
import { IonInfiniteScroll } from '@ionic/angular';
import { AlertController } from '@ionic/angular';
import { ModalController } from '@ionic/angular';
import { FurnitureEndpointService } from '../service/furniture-endpoint.service';
import { Furniture } from '../entity/furniture.interface';
import { Page } from '../entity/page.interface';
import { AddFurnitureComponent } from '../add-furniture/add-furniture.component';
import { EditFurnitureComponent } from '../edit-furniture/edit-furniture.component';
import { AddFurnitureCameraComponent } from '../add-furniture-camera/add-furniture-camera.component';


@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})
export class Tab2Page {
  items: Furniture[];
  pageSize = 20;
  pageIndex = 0;
  dataReturned: any;

  infiniteScrollDisabled = false;

  constructor(public furnitureEndpointService: FurnitureEndpointService,
    public alertController: AlertController,
    public modalController: ModalController) {
    this.doRefresh(null);
  }

  doInfinite(event) {
    this.pageIndex++;
    this.furnitureEndpointService.getFurnitures(this.pageIndex, this.pageSize).then(page => {
      this.items.push(...page.data);
      event.target.complete();
      if (this.items.length >= page.totalCount) {
        this.infiniteScrollDisabled = true;
      }
    });
  }

  doRefresh(event) {
    this.pageIndex = 0;
    this.furnitureEndpointService.getFurnitures(this.pageIndex, this.pageSize).then(data => {
      this.items = data.data;
      event?.target.complete();
      this.infiniteScrollDisabled = false;
    });
  }

  async presentAlertConfirm(id) {
    const alert = await this.alertController.create({
      cssClass: 'my-custom-class',
      header: 'Confirm!',
      message: 'Wollen Sie das Möbelstück wirklich Löschen?',
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          cssClass: 'secondary',
          handler: (blah) => {
            this.doRefresh(null);
          }
        }, {
          text: 'Okay',
          handler: () => {
            this.removeFurniture(id);
          }
        }
      ]
    });

    await alert.present();
  }

  removeFurniture(id) {
    console.log('rm' + id);
    this.furnitureEndpointService.removeFurniture(id).then(r => {
      this.doRefresh(null);
    });
  }

  async openModalAdd() {
    const modal = await this.modalController.create({
      component: AddFurnitureComponent,
      componentProps: {}
    });

    modal.onDidDismiss().then((dataReturned) => {
      if (dataReturned !== null) {
        this.dataReturned = dataReturned.data;
        if (this.dataReturned !== null) {
          console.log(this.dataReturned);
          this.furnitureEndpointService.addFurniture(this.dataReturned).then(r => {
            this.doRefresh(null);
          });

        } else {
          console.log('abgebrochen');
        }
      }
    });

    return await modal.present();
  }

  async openModalEdit(id, oldname, dim, oldcolor) {
    const modal = await this.modalController.create({
      component: EditFurnitureComponent,
      componentProps: {
        id,
        name: oldname,
        dimensions: dim,
        color: oldcolor
      }
    });

    modal.onDidDismiss().then((dataReturned) => {
      if (dataReturned.data) {
        this.furnitureEndpointService.putFurniture(id, dataReturned.data).then(r => {
          this.doRefresh(null);
        });
      }
    });

    return await modal.present();
  }

  async openModalAddCam() {
    const modal = await this.modalController.create({
      component: AddFurnitureCameraComponent,
      componentProps: {}
    });

    modal.onDidDismiss().then((dataReturned) => {
      if (dataReturned.data) {
        this.furnitureEndpointService.addFurniture(dataReturned.data).then(r => {
          this.doRefresh(null);
        });
      }
    });

    return await modal.present();
  }

}
