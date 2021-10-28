export class User {
    
    constructor(
        private userName?: string | undefined,
        private firstName?: string | undefined, 
        private lastName?: string | undefined,
        private email?: string | undefined,
        private birthDate?: Date | undefined,
        private password?: string | undefined) { }
    
    get UserName() : string | undefined {
        return this.userName;
    }

    set UserName(value: string | undefined) {
        this.userName = value;
    }

    get FirstName() : string | undefined {
        return this.firstName;
    }

    set FirstName(value: string | undefined) {
        this.firstName = value; 
    }

    get LastName() : string | undefined {
        return this.lastName;
    }

    set LastName(value: string | undefined) {
        this.lastName = value;
    }

    get Email() : string | undefined {
        return this.email;
    }

    set Email(value: string | undefined) {
        this.email = value;
    }

    get BirthDate() : Date | undefined {
        return this.birthDate;
    }

    set BirthDate(value : Date | undefined) {
        this.birthDate = value;
    }

    get Password() : string | undefined {
        return this.password;
    }

    set Password(value : string | undefined) {
        this.password = value;
    }
}