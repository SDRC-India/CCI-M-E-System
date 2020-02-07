import { Component, OnInit } from '@angular/core';
import { ReportsService } from '../reports.service';
import { IfStmt } from '@angular/compiler';

@Component({
  selector: 'scps-summary-report',
  templateUrl: './summary-report.component.html',
  styleUrls: ['./summary-report.component.scss']
})
export class SummaryReportComponent implements OnInit {

  tableData: any;
  allData:any;
  masterIndicatorData:any[]=[];
  tableColumns:string[];
  indicatorList:any;
  reportSummary:any;
  selectedIndicator: any;
  indicators: any[]=[];
  areaId=0;
  areaLevelId=0;
  indicatorData: any;
  currentDate: Date;
  summaryreport: string;

  constructor(private  reportService: ReportsService) { }

  ngOnInit() {
    this.reportService.getSummaryReport(this.areaId,this.areaLevelId).subscribe((data)=>{
      let allData = data;
      this.tableData = data.tableData;
      this.tableColumns = data.tableColumn;
      this.indicatorList = data.coreIndicator;
      this.masterIndicatorData=this.tableData;
      this.currentDate = data.reportGenerationDate;
      // if(this.indicatorList.length)
      // this.selectedIndicator=this.indicatorList[0];
    })
  }

  indicatorSelected(indicator) {
    if (indicator) {
      this.tableData =this.allData.filter(d => d.coreIndicator == indicator);
    }
    else {
      this.tableData = this.allData;
    }
  }

  indicatorSelection(){
    if(this.selectedIndicator){
    this.tableData=  this.masterIndicatorData.filter(d=>d.coreIndicator==this.selectedIndicator);
    }
    else{
      this.tableData=this.masterIndicatorData;
    }
  }
  reset(){
    this.selectedIndicator=null;
    this.tableData=this.masterIndicatorData;
  }
  downloadTableAsExcel(){
    
  }
}
