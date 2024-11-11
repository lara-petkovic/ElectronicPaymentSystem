import { Service } from "./service.model";

export interface Package {
    id: number;
    name: string;
    price: number;
    isBusinessPackage: boolean;
    packageServices: { service: Service }[]; 
}