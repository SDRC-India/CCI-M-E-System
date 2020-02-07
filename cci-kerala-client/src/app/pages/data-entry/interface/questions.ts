export interface IQuestions {
      key: number;
      value: any;
      controlType: string;
      label: string;
      type?: string;
      required?: boolean;
      columnName: string;
      minLength?: number;
      maxLength?: number;
      pattern: string;
      options?: any[];
      minDate?: string;
      maxDate?: string;
      fileExtension?: string[];
      fileExtensionValidationMessage?: string;
      fileValues?: any[];
      deletedFileValue?: any[];
      multiple?: boolean;
      fileSize?: number;
      optionsParentColumn?: any;
      dependentCondition?: string[];
      selectAllOption?: boolean;
      allChecked?: boolean;
      disabled?: boolean;
      childQuestionModels?: any;
      groupParentId?: string;
      indexNumberTrack?: string;
      serialNumb?: string;
}

