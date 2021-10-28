import { ErrorHandler, Injectable, NgZone } from "@angular/core";
import { NotificationService } from "../services/notification.service";
import { NotificationType } from "./notification.message";

@Injectable({ providedIn: 'root' })
export class AppErrorHandler implements ErrorHandler {

    constructor(private notificationService: NotificationService, private zone: NgZone) {
    }

    handleError(error: any): void {
        this.zone.run(() => this.notificationService.showError(error.message, "Adventure Seekers"));
    }

}