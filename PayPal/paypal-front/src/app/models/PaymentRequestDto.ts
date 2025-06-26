export interface PaymentRequestDto {
    orderId: string;
    merchantId?: string;
    amount: number;
}