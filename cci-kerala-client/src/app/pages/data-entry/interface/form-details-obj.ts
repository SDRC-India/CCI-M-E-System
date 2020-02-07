import { IQuestions } from './questions';

export interface FormDetailsObj {
    id: number;
    name: string;
    questions: IQuestions;
    isRejected: boolean;
}
