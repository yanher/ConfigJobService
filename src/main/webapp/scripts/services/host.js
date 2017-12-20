angular.module('HostServiceModule', []).factory('Host', function() {
	//var hostServer = '192.168.176.180:8081';

	var hostServer = 'localhost:8080';
	//var hostServer = '10.126.88.150:8082';
	var rootUrl = '/ConfigJobService/webapi/';
    var rootPath = function() {
        return 'http://'+hostServer+rootUrl;
    };
    return {
    	rootPath: function() {
        	return rootPath();
        }
    };
});
