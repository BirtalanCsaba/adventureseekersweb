import { AbstractControl, AsyncValidatorFn, ValidationErrors } from "@angular/forms";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { UserService } from "src/app/services/user.service";

export class EmailValidators {

    /**
     * Checks whether the email is already taken or not
     * @param userService The user service for checking the email
     * @returns null if the email is not taken, otherwise a ValidationError
     */
    static shouldByUnique(userService: UserService) : AsyncValidatorFn {
        return (control: AbstractControl): Observable<ValidationErrors | null> => {
            return userService.existsEmail(control.value).pipe(
                map((result: boolean) => {
                    // return an error if the email already exists
                    return !result ? null : { shouldByUnique: true }
                })
            );
        }
    }
}