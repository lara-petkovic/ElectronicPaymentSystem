import { Subscription } from "./subscription.model";

export interface User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    password: string;
    email: string;
    subscriptions?: Subscription[];
    role: string;
}
  