import { Package } from "./package.model";
import { User } from "./user.model";

export interface Subscription {
    id: number;
    userId: number;
    packageId: number;
    startDate: Date;
    durationInYears: number;
    user?: User;
    package?: Package;
    status: string;
}