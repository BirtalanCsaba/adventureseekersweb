import { AbstractControl, ValidationErrors } from "@angular/forms";

export class GlobalValidators {
    static shouldNotStartOrEndWithWhitespace(control: AbstractControl) : ValidationErrors | null {
        let value = control.value.toString();

        // check if begins or ends with space
        if (/^\s|\s$/.test(value)) {
            return { shouldNotStartOrEndWithWhitespace: true }
        }

        return null;
    }

    static cannotContainSpace(control: AbstractControl) : ValidationErrors | null {
        if ((control.value as string).indexOf(' ') >= 0)
            return { cannotContainSpace: true };
        return null;
    }
}