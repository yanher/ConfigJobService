var mainbodyModule = angular.module('mainbodyControllerModule', ['MainBodyServiceModule',/*'logControllerModule',*/'ui.grid.edit', 'ui.grid.cellNav', 'ui.grid.exporter','ui.grid.pagination','ui.grid.resizeColumns','ui.grid.autoResize','ui.grid.selection']);
mainbodyModule.controller('mainbodyController', ['$scope', '$rootScope', '$sessionStorage', /*'$timeout',*/ 'Utility', 'MainBodyService', 'uiGridConstants','i18nService','$state','$interval','$sce','$q','$timeout','usSpinnerService', function ($scope, $rootScope, $sessionStorage, /*$timeout,*/ Utility, MainBodyService, uiGridConstants, i18nService,$state,$interval,$sce,$q,$timeout,usSpinnerService) {
	i18nService.setCurrentLang("zh-cn");
	$scope.user = $rootScope.$sessionStorage.user;
	$scope.spinerService = usSpinnerService;
	$scope.start_job = function(row) {  
    	if(confirm("确定要启动定时计划吗？")) {
    		MainBodyService.startJob(row.entity.project,row.entity.job,row.entity.cron,row.entity.delay,row.entity.phoneNum).success(function(data, status) {
                alert("更新成功");
                MainBodyService.refreshData();
       	    }).error(function(data, status) {
       		    alert("更新失败");
       	    });
    	}
    };
    $scope.update_job = function(row) { 
    	if(confirm("确定要更新定时计划吗？")) {
        	MainBodyService.updateJob(row.entity.project,row.entity.job,row.entity.cron,row.entity.delay,row.entity.phoneNum).success(function(data, status) {
                 alert("更新成功");
                 MainBodyService.refreshData();
        	}).error(function(data, status) {
        		alert("更新失败");
        	});
    	}
    }; 
    $scope.stop_job = function(row) {  
    	if(confirm("确定要停止定时计划吗？")) {
    		MainBodyService.stopJob(row.entity.project,row.entity.job).success(function(data, status) {
                alert("更新成功");
                MainBodyService.refreshData();
       	    }).error(function(data, status) {
       		    alert("更新失败");
       	    });
    	} 
    };  
    $scope.refresh = function() { 
    	MainBodyService.refreshData();
    }
    	
    var timeout_upd;
    
    
    $scope.last = ''; 
    $scope.logdata = '';
    $scope.log = '';   
    $scope.show_log = function(row) {  
    	$scope.last = '0';
    	$scope.logRow = row;
    	$scope.firstLog = true;
    	$('#logviewer').modal({
    		//templateUrl: 'ngviews/logviewer.html',
 	    	backdrop: 'static',
 	    	width:'90%',
 	    	height:'90%'
 	    }).on("shown.bs.modal", function () {
 	    	var finished = true;
 	    	timeout_upd = $interval(function(){
 	    		if(finished === true) {
 	    			finished = false;
 	    			//if($scope.firstLog == true){
 	    				$scope.spinerService.spin('spinner-2');
 	    			//}
 	    			MainBodyService.showLog(row.entity.project,row.entity.job,$scope.last,row.entity.workspace).success(function(data, status) {
 	 	 		    	$scope.log += data[0].text;
 	 	 		    	//if(data[0].text.indexOf('查询日志时间超时') >= 0) {
 	 	 		    		//$scope.last = '0';
 	 	 		    	//} else {
 	 	 	 		    	$scope.last = data[1].last;
 	 	 		    	//}
 	 	                //$scope.logdata = $sce.trustAsHtml($scope.log);
 	 	                var log = $scope.log;
                                log = log.replace(/^ \r\n$/g,'');
                                log = log.replace(/^\s*\r\n\s*$/g,'');
                                log = log.replace(/^\s*\n\$/g,'');
                                log = log.replace(/^\s*\n\s*$/g,'');
                                log = log.replace(/^\s*\r\s*$/g,'');
 	 	                $scope.logdata = log + "\n";
 	 	                finished = true;
 	 	                $scope.firstLog = false;
                                //$scope.$apply();
 	 	                $scope.spinerService.stop('spinner-2');
 	 	 		     }).error(function(data, status) {
 	 	 			    alert("日志信息加载失败");
 	 	 			    $interval.cancel(timeout_upd); 
 	 	                $scope.firstLog = false;
 	 	                $scope.spinerService.stop('spinner-2');
 	 	 		     });
 	    		}
 	    	}, 2000,-1);
 	    }).on("hidden.bs.modal", function () {
            $(this).removeData("bs.modal");
            $interval.cancel(timeout_upd); 
            $scope.refresh();
            $scope.spinerService.stop('spinner-2');
        });
    }; 
    
    $scope.$on('$destroy',function(){  
        $interval.cancel(timeout_upd);  
    }); 

    $scope.gridOptions = {
            enableColumnResizing: true,
    	    exporterSuppressMenu: false,
            exporterMenuLabel: 'Export',
            exporterCsvColumnSeparator: ',',
            exporterOlderExcelCompatibility: true,
            exporterIsExcelCompatible: true,
            exporterHeaderFilterUseName: true,
            exporterHeaderFilter: function( name ){ return name; },
            exporterMenuAllData: true,
            exporterMenuVisibleData: true,
            exporterMenuSelectedData: false,
            exporterMenuCsv: true,
            exporterMenuExcel: false,
            exporterFieldApplyFilters: false,
            exporterAllDataFn: null,
            exporterSuppressColumns: ['action'],
            exporterFieldApplyFilters: true, //Defaults to false, which leads to filters being evaluated on export *
            exporterFieldCallback: function ( grid, row, col, value ){
          	  if ( col.name === 'lastStatus' ){
          	    if(value === '0') {
          	    	return 'initial';
          	    }else if(value === '-1'){
          	    	return 'fail';
          	    }else if(value === '1'){
          	    	return 'success';
          	    }
          	  }
          	  if ( col.name === 'running' ){
            	    if(value === 'true') {
            	    	return 'start';
            	    }else if(value === 'false'){
            	    	return 'stop';
            	    }
              }
          	  return value;
          	},
        enableSorting: true, //是否排序
        useExternalSorting: false, //是否使用自定义排序规则
        enableGridMenu: true, //是否显示grid 菜单
        exporterMenuPdf: false,
        exporterCsvFilename: '任务列表.csv', 
        //gridMenuTitleFilter: fakeI18n,
        showGridFooter: true, //是否显示grid footer
        enableHorizontalScrollbar :  1, //grid水平滚动条是否显示, 0-不显示  1-显示
        enableVerticalScrollbar : 1, //grid垂直滚动条是否显示, 0-不显示  1-显示
        showGridFooter: true,
        showColumnFooter: true,
        enableFiltering: true,
        enablePagination: true, //是否分页，默认为true
        enablePaginationControls: true, //使用默认的底部分页
        paginationPageSizes: [15, 30, 50, 100], //每页显示个数可选项
        paginationCurrentPage:1, //当前页码
        paginationPageSize: 15, //每页显示个数
        totalItems : 0, // 总数量
        useExternalPagination:false ,  //是否使用分页按钮
        gridMenuCustomItems: false,
        columnDefs: [{
            field: 'project',
            displayName: '项目',
            enableHiding: false,
            width: '9%',
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew'
        }, {
            field: 'job',
            displayName: '任务',
            width: '9%',
            enableHiding: false,
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew'
        }, {
            field: 'cron',
            displayName: 'CRON表达式',
            width: '11%',
            enableHiding: false,
            enableCellEdit: true,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew',
            cellTemplate : 
                '<div class="ui-grid-cell-contents ng-binding ng-scope">{{grid.appScope.showCron(row);}}</div>'
        }, {
            field: 'delay',
            width: '8%',
            displayName: '间隔（分）',
            enableHiding: false,
            enableCellEdit: true,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew',
            cellTemplate : 
                '<div class="ui-grid-cell-contents ng-binding ng-scope">{{grid.appScope.showDelay(row);}}</div>'
        }, {
            field: 'nextTime',
            width: '12%',
            displayName: '下次执行时间',
            enableHiding: false,
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew',
            cellTemplate : 
                '<div class="ui-grid-cell-contents ng-binding ng-scope">{{grid.appScope.showNext(row);}}</div>'
        }, {
            field: 'lastStatus',
            width: '8%',
            displayName: '上次结果',
            enableHiding: false,
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew',
            cellTemplate : 
                '<div class="ui-grid-cell-contents ng-binding ng-scope">{{grid.appScope.showResult(row);}}</div>'
        }, {
            field: 'phoneNum',
            width: '10%',
            displayName: '负责人电话',
            enableHiding: false,
            enableCellEdit: true,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew'
        },{
            field: 'running',
            width: '7%',
            displayName: '程序状态',
            enableHiding: false,
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew',
            cellTemplate : 
                '<div class="ui-grid-cell-contents ng-binding ng-scope">{{grid.appScope.showStatus(row);}}</div>'
        },{
            field: 'workspace',
            width: '12%',
            displayName: '工作目录',
            enableHiding: false,
            enableCellEdit: false,
            cellClass: 'gridCellClassNew',
            headerCellClass: 'gridCellClassNew'
        },{  
            field : 'action',  
            displayName : "操作",
            enableColumnMenu: false,
            cellTemplate : '<div id="action_column" class="container-fluid">'+
            	'<div class="row cell-action-style">'+
                '<div class="col-xs-3 text-center" >'+
                '<div class="div-click"  ng-click="grid.appScope.start_job(row)">'+
                    '<span class="glyphicon glyphicon-play-circle" aria-hidden="true"></span>'+
                '</div>'+
                '</div>'+
                   '<div class="col-xs-3 text-center">'+
                      '<div class="div-click"  ng-click="grid.appScope.update_job(row)">'+
                          '<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>'+
                      '</div>'+
                    '</div>'+
                    '<div class="col-xs-3 text-center" >'+
                        '<div class="div-click"  ng-click="grid.appScope.stop_job(row)">'+
                            '<span class="glyphicon glyphicon-off" aria-hidden="true"></span>'+
                        '</div>'+
                    '</div>'+
                    '<div class="col-xs-3 text-center" >'+
                    '<div class="div-click"  ng-click="grid.appScope.show_log(row)">'+
                        '<span class="glyphicon glyphicon-blackboard" aria-hidden="true"></span>'+
                    '</div>'+
                    '</div>'+
                    '<div></div></div></div>',  
            enableCellEdit: false,
            enableHiding: false,
            headerCellClass: 'gridCellClassNew'
        }],
        data: [],
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
        },
        rowTemplate: '<div ng-click="grid.appScope.clickRow(grid, row)" ng-class="{ \'hover-on-row\': true }">' +
            '<div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
            '</div>'
    }

    //Load all data here
    $scope.showResult = function(row){
    	if(row.entity.lastStatus === '0'){
    		return '首次初始';
    	}else if (row.entity.lastStatus === '1'){
    		return '成功';
    	}else if (row.entity.lastStatus === '-1'){
    		return '失败';
    	}else {
    		return '未知';
    	}
    };
    $scope.showStatus = function(row){
    	if(row.entity.running === 'true'){
    		return '开启';
    	}else if (row.entity.running === 'false'){
    		return '停止';
    	} else {
    		return '未知';
    	}
    };
    $scope.showNext = function(row){
    	if(row.entity.nextTime === ''){
    		return '--';
    	}
    	return row.entity.nextTime;
    };
    $scope.showCron = function(row){
    	if(row.entity.cron === ''){
    		return '--';
    	}
    	return row.entity.cron;
    };
    $scope.showDelay = function(row){
    	if(row.entity.delay === ''){
    		return '--';
    	}
    	return row.entity.delay;
    };
    $scope.submitted = false;
	$scope.command = '';
    $scope.grep = function() {
    	$interval.cancel(timeout_upd);
    	$scope.spinerService.spin('spinner-2');
    	$scope.submitted = true;
    	MainBodyService.execute($scope.logRow.entity.project,$scope.logRow.entity.job,$scope.logRow.entity.workspace,$scope.command).success(function(data, status) {
    		    //$interval.cancel(timeout_upd);
    		    $scope.log = data.text;
                $scope.logdata = $sce.trustAsHtml($scope.log);
                $scope.spinerService.stop('spinner-2');
                $scope.submitted = false;
		}).error(function(data, status) {
			  alert("执行命令失败！");
			  $scope.spinerService.stop('spinner-2');
			  $scope.submitted = false;
		});
    };
    
    $scope.spinerService.spin('spinner-2');
    MainBodyService.getAllData($scope.user.userName,$scope.user.password).success(function(data, status) {
    	$scope.gridOptions.data = data;
    	$scope.spinerService.stop('spinner-2');
	}).error(function(data, status) {
		$scope.spinerService.stop('spinner-2');
	});
}]);
