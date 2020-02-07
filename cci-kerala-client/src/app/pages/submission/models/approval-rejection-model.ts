import { IQuestion } from 'lib/src/form/view-form/view-form.component';

export interface ApprovalRejectionModel {

    approved: boolean;

    remarks: string;

    rejectedSections: number[];

    questionModels: IQuestion[];

    formId:number;
}
