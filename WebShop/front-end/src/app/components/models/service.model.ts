export interface Service {
    id: number;
    name: string;
    description: string;
    type: ServiceType;
    price: number;
}

export enum ServiceType {
    Mobile = 'Mobile',
    FixedPhone = 'FixedPhone',
    Internet = 'Internet',
    DigitalTV = 'DigitalTV'
}