import { Inject, Injectable, Injector } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subject } from 'rxjs';
import { NotificationMessage, NotificationType } from '../common/notification.message';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private get toastService() {
    return this.injector.get(ToastrService);
  }

  constructor(@Inject(Injector) private readonly injector: Injector) { 
    
  }

  showSuccess(message: string, title:string){
    this.toastService.success(message, title)
  }

  showError(message: string, title:string){
      this.toastService.error(message, title)
  }

  showInfo(message: string, title:string){
      this.toastService.info(message, title)
  }

  showWarning(message: string, title:string){
      this.toastService.warning(message, title)
  }

}
