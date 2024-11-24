export interface ItemDto {
    name : string,
    description : string,
    type : string,
    price : number,
    isBusinessPackage : boolean,
    isPackage : boolean,
    status: string,
    startDate?: string
}