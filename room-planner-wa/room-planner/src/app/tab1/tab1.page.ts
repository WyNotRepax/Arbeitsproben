import { Component, ViewChild } from '@angular/core';
import { IonInfiniteScroll } from '@ionic/angular';
import { PlanEndpointService } from '../service/plan-endpoint.service';
import { Page } from '../entity/page.interface';
import { PlanListing } from '../entity/plan.interface';
import { AlertController } from '@ionic/angular';
import { ModalController } from '@ionic/angular';
import { AddPlanComponent } from '../add-plan/add-plan.component';
import { EditPlanComponent } from '../edit-plan/edit-plan.component';

@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {
  @ViewChild(IonInfiniteScroll) infiniteScroll: IonInfiniteScroll;

  items: PlanListing[];
  pageSize = 20;
  pageIndex = 0;
  infiniteScrollDisabled = false;

  constructor(
    public planEndpointService: PlanEndpointService,
    public alertController: AlertController,
    public modalController: ModalController) {
    this.doRefresh(null);
  }

  doInfinite(event) {
    this.pageIndex++;
    this.planEndpointService.getPlans(this.pageIndex, this.pageSize).then(page => {
      this.items.push(...page.data);
      event.target.complete();
      if (this.items.length >= page.totalCount) {
        this.infiniteScrollDisabled = true;
      }
    });
  }

  doRefresh(event) {
    this.pageIndex = 0;
    this.planEndpointService.getPlans(this.pageIndex, this.pageSize).then(data => {
      this.items = data.data;
      event?.target.complete();
      this.infiniteScrollDisabled = false;
    });
  }

  itemClick(tmp) {
    console.log(tmp);
  }

  async presentAlertConfirm(id) {
    const alert = await this.alertController.create({
      cssClass: 'my-custom-class',
      header: 'Confirm!',
      message: 'Wollen Sie den Plan wirklich Löschen?',
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
            this.removePlan(id);
          }
        }
      ]
    });

    await alert.present();
  }

  removePlan(id) {
    console.log('rm' + id);
    this.planEndpointService.removePlan(id).then(r => {
      this.doRefresh(null);
    });
  }

  async openModalAdd() {
    const modal = await this.modalController.create({
      component: AddPlanComponent,
      componentProps: {}
    });

    modal.onDidDismiss().then((dataReturned) => {
      if (dataReturned.data) {
        this.planEndpointService.addPlan(dataReturned.data).then(r => {
          this.doRefresh(null);
        });
      }
    });

    return await modal.present();
  }

  async openModalEdit(id, oldname, dim) {
    const modal = await this.modalController.create({
      component: EditPlanComponent,
      componentProps: {
        id,
        name: oldname,
        dimensions: dim
      }
    });

    modal.onDidDismiss().then((dataReturned) => {
      if (dataReturned.data) {
        this.planEndpointService.putPlan(id, dataReturned.data).then(r => {
          this.doRefresh(null);
        });
      }
    });

    return await modal.present();
  }

}
