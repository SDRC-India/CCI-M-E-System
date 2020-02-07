This package is developed by SDRC UI team. It automatically creates form elements along with their validation as per the provided json. Please follow the installation steps.

Steps to use SDRC Table
1. Install Bootstrap, Angular material

2. Import angular material theme css file inside style.css
```css
@import "~@angular/material/prebuilt-themes/indigo-pink.css";
```

3. Install SdrcForm
```bass
npm i sdrc-form
```

4. Import FormModule of sdrc-form
```js
import { FormModule } from 'sdrc-form';

imports: [
    BrowserModule,
    FormModule
  ]
```

5. Use component sdrc-form
```ts
<sdrc-form [numberOfColumn]="1" [questionArray]="allQuestions" (onSubmit)="submit($event)" (onButtonClicked)="deleteButton($event)"></sdrc-form>
```

6. allQuestion model example: 
```js
allQuestions: any[] = [
    {
        "key": 1,
        "label": "First Name:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a0",
        "required": true,
        "pattern":"name",
        "dependentCondition":[],
        "maxLength": 30
    },
    {
        "key": 19,
        "label": "Password:",       
        "type": "password",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a19",
        "required": true,        
        "dependentCondition":[],
        "maxLength": 30
    },
    {
        "key": 2,
        "label": "Email:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a1",
        "required": true,
        "pattern": "email",
        "dependentCondition":[]

    },
    {
        "key": 3,
        "label": "Organization Name:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textarea",
        "value":"",
        "columnName": "a2",
        "required": true,
        "dependentCondition":[],
        "maxLength": 30

    },
    {
        "key": 4,
        "label": "Website Name:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a3",
        "required": true,
        "pattern":"domiwebsite",
        "dependentCondition":[]

    },
    {
        "key": 5,
        "label": "Mobilenumber:",       
        "type": "number",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a4",
        "required": true,
        "pattern":"phone",
        "dependentCondition":[]

    },
    {
        "key": 12,
        "label": "Landline Number:",       
        "type": "number",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a6",
        "required": true,
        "pattern":"landlinephone",
        "dependentCondition":[]

    },
    {
        "key": 6,
        "label": "Address:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textarea",
        "value":"",
        "columnName": "a5",
        "required": true,
        "minLength": 30,
        "maxLength": 250,
        "dependentCondition":[]

    },
    {
        "key": 7,
        "label": "State",       
        "type": "multiSelect",
        "options": [{"key":1,"value":"Odisha"},{"key":2,"value":"Jharkhand"},{"key":3,"value":"Bihar"}],
        "typeId": null,
        "controlType": "multiSelect",
        "value":[1, 2, 3],
        "columnName": "a7",
        "required": true,
        "selectAllOption": true,
        "allChecked": true,
        "dependentCondition":[]
    },
    {
        "key": 13,
        "label": "District",       
        "type": "dropdown",
        "options": [{
            "key":1,"value":"puri","parentKey":1},
            {"key":2,"value":"khorda","parentKey":2},
            {"key":3,"value":"cuttack"},
            {"key":4,"value":"kendrapada"}
        ],
        "typeId": null,
        "controlType": "dropdown",
        "value":"",
        "columnName": "a13",
        "required": true,
        "optionsParentColumn":"a7"
        
    },
    {
        "key": 14,
        "label": "Aadhar ID",       
        "type": "dropdown",
        "options": [
            {"key":1,"value":"yes"},
            {"key":2,"value":"No"}
        ],
        "typeId": null,
        "controlType": "dropdown",
        "value":"",
        "columnName": "a14",
        "required": true,
        "dependentCondition":[]

    },
    {
        "key": 15,
        "label": "ID No.",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a15",
        "required": true,
        "parentColumns":["a14","a13"],
        "dependentCondition":["isDependencyValue#1","isDependencyValue#1"]
    },
    {
        "key": 16,
        "label": "ID No.",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a16",
        "required": false,
        "parentColumns":["a14","a13"],
        "dependentCondition":["isDependencyValue#1","isDependencyValue#2"]
    },
    {
        "key": 18,
        "label": "Qualification",       
        "type": "multiSelect",
        "options": [
            {"key":1,"value":"10th"},
            {"key":2,"value":"+2"},
            {"key":3,"value":"+3"},
            {"key":4,"value":"Btech"}
        ],
        "typeId": null,
        "controlType": "multiSelect",
        "value":"",
        "columnName": "a18",
        "required": true,
        "selectAllOption": true
    },
    {
        "key": 8,
        "label": "Language known",       
        "type": "checkbox",
        "options": [
            {"key":1,"value":"Hindi","checked":false},
            {"key":2,"value":"English","checked":false},
            {"key":3,"value":"Odia","checked":false}
        ],
        "typeId": null,
        "controlType": "checkbox",
        "value":"",
        "columnName": "a8",
        "required": true,
        "dependentCondition":[]
    },
    {
        "key": 9,
        "label": "Gender",       
        "type": "radio",
        "options": [
            {"key":1,"value":"Male"},
            {"key":2,"value":"Female"}
        ],
        "typeId": null,
        "controlType": "radio",
        "value":"1",
        "columnName": "a9",
        "required": true,
        "dependentCondition":[]
    },
    {
        "key": 24,
        "label": "Website Name:",       
        "type": "text",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a23",
        "required": true,
        "pattern":"domiwebsite",
        "parentColumns": ["a9"],
        "dependentCondition":["isDependencyValue#2"]
    },
    {
        "key": 11,
        "label": "Registation Number",       
        "type": "datepicker",
        "options": null,
        "typeId": null,
        "controlType": "datepicker",
        "value":"",
        "columnName": "a11",
        "required": true,
        "dependentCondition":[],
        "minDate": "today"
    },
    {
        "key": 12,
        "label": "Pincode",       
        "type": "textbox",
        "options": null,
        "typeId": null,
        "controlType": "textbox",
        "value":"",
        "columnName": "a12",
        "required": true,
        "pattern":"pincode",
        "dependentCondition":[]
    },
    {
        "key": 20,
        "label": "File",       
        "type": "file",
        "options": null,
        "typeId": null,
        "controlType": "file",
        "value":"",
        "columnName": "a20",
        "required": true,
        "fileValues":[],
        "multiple":false,
        "fileExtension":["xls","xlsx"],
        "fileExtensionValidationMessage":"",
        "fileSize":"1024000"
    },
    {
        "key": 17,
        "label": "Tags",
        "value":[],
        "required":true,
        "controlType": "chips",
        "columnName": "a17"
    },
    {
        "key": 6,
        "label": "",       
        "type": "submit",
        "options": null,
        "typeId": null,
        "controlType": "submit",
        "value":"",
        "columnName": "a5",
        "dependentCondition":[]

    },
    {
        "key": 6,
        "label": "",       
        "type": "button",
        "options": null,
        "typeId": null,
        "controlType": "button",
        "value":"Preview",
        "columnName": "a5",
        "dependentCondition":[]
    }
]
```