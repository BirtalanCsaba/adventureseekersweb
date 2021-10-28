import { AbstractControl, AsyncValidatorFn, ValidationErrors } from "@angular/forms";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { UserService } from "src/app/services/user.service";

export class UsernameValidators {
    
     /**
     * Checks whether the username is already taken or not
     * @param userService The user service for checking the username
     * @returns null if the username is not taken, otherwise a ValidationError
     */
    static shouldByUnique(userService: UserService) : AsyncValidatorFn {
        return (control: AbstractControl): Observable<ValidationErrors | null> => {
            return userService.existsUsername(control.value).pipe(
                map((result: boolean) => {
                    // return an error if the username already exists
                    return !result ? null : { shouldByUnique: true }
                })
            );
        }
    }
}