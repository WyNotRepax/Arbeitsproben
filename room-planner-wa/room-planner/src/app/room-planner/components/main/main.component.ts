import { AfterViewInit, Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { Subscription } from 'rxjs';
import { take } from 'rxjs/operators';
import { FurnitureInPlan } from 'src/app/entity/plan.interface';
import { FurnitureService } from '../../service/furniture.service';
import { RoomPlannerService } from '../../service/room-planner.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements AfterViewInit, OnDestroy {


  @ViewChild('svg')
  svgElementRef?: ElementRef<SVGElement>;

  @ViewChild('mainGroup')
  mainGroup?: ElementRef<SVGGraphicsElement>;

  @ViewChild('roomRect')
  roomRect?: ElementRef<SVGRectElement>;

  scale = 1;
  offset = {
    x: 0,
    y: 0
  };

  private dragging = false;

  private routeSubscription: Subscription;


  private get svgElement() {
    return this.svgElementRef?.nativeElement;
  }

  constructor(public service: RoomPlannerService, public route: ActivatedRoute) {
    this.routeSubscription = this.route.params.subscribe(params => this.service.setPlanId(parseInt(params.planId, 10)));
     this.service.plan.subscribe((plan) => {
    //   console.log(plan)
    //   console.log(this.roomRect)
    //   this.roomRect?.nativeElement.setAttribute('width', plan.dimensions.width.toFixed());
    //   this.roomRect?.nativeElement.setAttribute('height', plan.dimensions.height.toFixed());
    });
  }


  @HostListener('pan', ['$event'])
  onPan(event: { pointers?: PointerEvent[]; srcEvent: any }) {
    if (
      event.pointers?.length > 0 &&
      !event.srcEvent.srcElement.hasAttribute('appFurniture')) {
      const pointerEvent = event.pointers[0];
      this.offset.x += pointerEvent.movementX;
      this.offset.y += pointerEvent.movementY;
      this.updateTransform();
    }
  }

  @HostListener('mousewheel', ['$event'])
  onScroll(event: WheelEvent) {
    if (event.deltaY < 0) {
      this.scale *= 1.1;
    } else if (event.deltaY > 0) {
      this.scale *= 0.9;
    }
    this.updateTransform();
    console.log(this.scale);
  }

  @HostListener('pinch', ['$event'])
  onPinch(event: any) {

  }

  ngAfterViewInit() {
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }


  public addFurniture(id: number) {
    this.service.addFurniture(id);
  }

  private updateTransform() {
    this.mainGroup?.nativeElement.setAttribute(
      'transform',
      `translate(${this.offset.x} ${this.offset.y}) scale(${this.scale})`
    );
  }

}
