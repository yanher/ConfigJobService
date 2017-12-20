angular.module('MainBodyServiceModule', []).factory('MainBodyService', ['$http', 'Host','$rootScope', function($http, Host,$rootScope) {
	var rootPath = Host.rootPath();
	
	var refreshData = function() {
        delete $rootScope.AllData;
        $rootScope.$state.go('list', {}, {reload: true});
    };
    
	var getAllData = function(userName,password){
        return $http({
            method: 'get',
            url: rootPath+'jobinfo?userName='+userName+'&password='+password
        });
	};
	
	var updateJob = function(project,job,cron,delay,phoneNum){
        return $http({
        	method: 'put',
        	url: rootPath + 'jobinfo/',
            headers: {'Content-type': 'application/json;charset=UTF-8'},
            data: {
            	project:project,
            	job:job,
            	cron:cron,
            	delay:delay,
            	phoneNum:phoneNum
            }
        });
	};
        var startJob = function(project,job,cron,delay,phoneNum){
            return $http({
            	method: 'post',
            	url: rootPath + 'jobinfo/',
                headers: {'Content-type': 'application/json;charset=UTF-8'},
                data: {
                	project:project,
                	job:job,
                	cron:cron,
                	delay:delay,
                	phoneNum:phoneNum
                }
            });
        };
        var stopJob = function(project,job){
            return $http({
            	method: 'delete',
            	url: rootPath + 'jobinfo?project='+project+'&job='+job,
                headers: {'Content-type': 'application/json;charset=UTF-8'},
                data: {
                }
            });
        };
        
        
        
        var showLog = function(project,job,last,workspace){
            return $http({
                method: 'get',
                url: rootPath+'log?project='+project+'&job='+job+'&last='+last+'&workspace='+workspace,
            });
    	};
    	
    	var execute = function(project,job,workspace,command){
            return $http({
                method: 'post',
                url: rootPath+'log?project='+project+'&job='+job+'&workspace='+workspace+'&command='+command,
                headers: {'Content-type': 'text/html;charset=UTF-8'},
                data: {
                }
            });
    	};
    	
    return {
    	refreshData: refreshData,
    	updateJob: function(project,job,cron,delay,phone) {
            return updateJob(project,job,cron,delay,phone);
        },
        getAllData: function(userName,password) {
            return getAllData(userName,password);
        },
        startJob: function(project,job,cron,delay,phoneNum) {
            return startJob(project,job,cron,delay,phoneNum);
        },
        stopJob: function(project,job) {
            return stopJob(project,job);
        },
        showLog: function(project,job,last,workspace) {
            return showLog(project,job,last,workspace);
        },
        execute: function(project,job,workspace,command) {
            return execute(project,job,workspace,command);
        }
    };
}]);
