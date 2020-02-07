import { environment } from 'src/environments/environment';

export class Constants {
 
    public static get API_GATE_WAY(): string {return environment.api_url; }
    public static get SAVE_MESSAGE(): string {return 'Successfully saved'; }
    public static get FORM_ID_INSTITUTION(): number {return 1; }
    public static get FORM_ID_INMATE(): number {return 2; }
    public static get DEFAULT_ZOOM(): number{return 15;}
    public static get FILE_NAME_DEFAULT(): String{return 'SubmissionData';}
    public static get ACCESS_TOKEN(): string{return 'cci_access_token';}
    public static get REFRESH_TOKEN(): string{return 'cci_refresh_token';}
    public static get USER_DETAILS(): string{return 'cci_user_details';}
    public static get SERVER_ERROR_MESSAGE(): string{return 'Server error';}
    public static get RESOURCE_FOLDER(): string{return 'assets/resource/';}
    public static get EXCEEDING_SANCTIONED_LIMIT(): string { return 'You are exceeding your maximum strength. Click OK to continue.';}
}
