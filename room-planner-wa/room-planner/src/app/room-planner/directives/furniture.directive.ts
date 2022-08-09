/* eslint-disable no-underscore-dangle */
import { Directive, ElementRef, HostListener, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { Furniture } from 'src/app/entity/furniture.interface';
import { FurnitureInPlan } from 'src/app/entity/plan.interface';
import { Position } from 'src/app/entity/position.interface';
import { FurnitureService } from '../service/furniture.service';

@Directive({
  selector: 'rect[appFurniture]'
})
export class FurnitureDirective implements OnChanges {

  @Input()
  furniture?: FurnitureInPlan;

  @Input()
  planId?: number;

  @Input()
  scale = 1;



  private get rect() {
    return this.el.nativeElement as SVGRectElement;
  }

  private position: Position = { x: 0, y: 0, z: 0, rotation: 0 };
  private width = 0;
  private height = 0;
  private clicked = false;

  private positionSubject = new Subject<void>();

  constructor(private el: ElementRef, private furnitureService: FurnitureService) {
    this.positionSubject.pipe(debounceTime(1000)).subscribe(_ => {
      if (this.planId && this.furniture) {
        this.furnitureService.updateFurnitureInPlan(this.planId, Object.assign({}, this.furniture, { position: this.position }));
      }
    });
  }




  @HostListener('pan', ['$event'])
  onMove(event) {
    if (event.pointers?.length > 0) {
      const pointerEvent = event.pointers[0];
      this.position.x += pointerEvent.movementX / this.scale;
      this.position.y += pointerEvent.movementY / this.scale;
      this.positionSubject.next();
      this.updateTransform();
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.furniture !== undefined && changes.furniture.currentValue !== changes.furniture.previousValue) {
      const curr = changes.furniture.currentValue as FurnitureInPlan;
      this.furnitureService.getFurniture(curr).then(furniture => {
        this.rect.setAttribute('fill', furniture.color);
        this.width = furniture.dimensions.width;
        this.height = furniture.dimensions.height;
        this.position = curr.position;
        this.updateTransform();
      });
    }
  }

  private updateTransform() {
    this.rect.setAttribute('width', this.width.toFixed());
    this.rect.setAttribute('height', this.height.toFixed());
    this.rect.setAttribute(
      'transform',
      `translate(${this.position.x} ${this.position.y}) ` +
      `rotate(${this.position.rotation}) ` +
      `translate(${-this.width / 2} ${-this.height / 2})`
    );
  }
}
