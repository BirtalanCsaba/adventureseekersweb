export class UserDetail {
    constructor(
        private description?: string | undefined,
        private city?: string | undefined,
        private country?: string | undefined,
        private county?: string | undefined,
        private profileImage?: Blob | undefined
    ) {}

    get Description() : string | undefined {
        return this.description;
    }

    set Description(description: string | undefined) {
        this.description = description;
    }

    get City() : string | undefined {
        return this.city;
    }

    set City(city: string | undefined) {
        this.city = city;
    }

    get Country() : string | undefined {
        return this.country;
    }

    set Country(country: string | undefined) {
        this.country = country;
    }

    get County() : string | undefined {
        return this.county;
    }

    set County(county: string | undefined) {
        this.county = county;
    }

    get ProfileImage() : Blob | undefined {
        return this.profileImage;
    }

    set ProfileImage(profileImage: Blob | undefined) {
        this.profileImage = profileImage;
    }

}