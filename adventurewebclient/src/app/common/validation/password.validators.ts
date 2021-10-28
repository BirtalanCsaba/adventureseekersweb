import { AbstractControl, FormGroup, ValidationErrors } from "@angular/forms";

export class PasswordValidators {
    static matchPassword(control: AbstractControl) : ValidationErrors | null {
        const password = control.get("password")?.value;
        const confirm = control.get("confirmpassword")?.value;
        
        if (password !== confirm) { return { matchPassword: true } }
     
        return null
     
      }
}