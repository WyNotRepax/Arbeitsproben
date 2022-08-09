import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { AddFurnitureToPlanComponent } from './add-furniture-to-plan.component';

describe('AddFurnitureToPlanComponent', () => {
  let component: AddFurnitureToPlanComponent;
  let fixture: ComponentFixture<AddFurnitureToPlanComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ AddFurnitureToPlanComponent ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(AddFurnitureToPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
