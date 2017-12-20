angular.module('UserServiceModule', []).factory('UserService', ['$http', 'Host', function($http, Host) {

    var rootPath = Host.rootPath();
    
    var login = function(name, pwd) {
        return $http({
        	method: 'post',
        	url: rootPath + 'user',
            headers: {'Content-type': 'application/json;charset=UTF-8'},
            data: {
            	username:name,
            	password:pwd
            }
        });
    };

    return {
        login: function(name, pwd) {
            return login(name, pwd);
        }
    };  
}]);
