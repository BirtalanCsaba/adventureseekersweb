export class NotificationMessage {
    message: string | undefined;
    type: NotificationType | undefined;
}

export enum NotificationType {
    success = 0,
    warning = 1,
    error = 2,
    info = 3
}