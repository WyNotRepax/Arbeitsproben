
import { Dimensions } from './dimensions.interface';
import { Position } from './position.interface';

export interface PlanCreateUpdate {
    name: string;
    dimensions: Dimensions;
}

export interface PlanListing extends PlanCreateUpdate{
    id: number;
    date: string;
}

export interface PlanDetail extends PlanListing{
    furniture: FurnitureInPlan[];
}

export interface FurnitureInPlanCreateUpdate{
    furnitureId: number;
    position: Position;
}

export interface FurnitureInPlan extends FurnitureInPlanCreateUpdate{
    id: number;
}
