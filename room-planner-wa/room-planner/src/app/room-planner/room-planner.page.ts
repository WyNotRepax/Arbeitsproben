import { Component, OnInit, ViewChild } from '@angular/core';
import { PickerController, PickerOptions } from '@ionic/angular';
import { MainComponent } from './components/main/main.component';
import { FurnitureService } from './service/furniture.service';

@Component({
  selector: 'app-room-planner',
  templateUrl: './room-planner.page.html',
  styleUrls: ['./room-planner.page.scss'],
})
export class RoomPlannerPage implements OnInit {

  @ViewChild(MainComponent)
  mainComponent: MainComponent;


  private opts?: PickerOptions;



  constructor(private pickerController: PickerController, private furnitureService: FurnitureService) {
    this.furnitureService.furniture.subscribe(furnitures => {
      console.log(furnitures);
      this.opts = {
        buttons: [{
          text: 'Add',
          handler: (value) => this.mainComponent?.addFurniture(value.furniture.value)
        }],
        columns: [{
          name: 'furniture',
          options: furnitures.map(furniture => ({ text: furniture.name, value: furniture.id }))
        }]
      };
      console.log(this.opts);
    });
  }

  ngOnInit() {
  }

  addButtonClicked() {
    console.log(this.mainComponent);
    if (this.opts) {
      this.pickerController.create(this.opts).then(picker => picker.present());
    }
  }

}
