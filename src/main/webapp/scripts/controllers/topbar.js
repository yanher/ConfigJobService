angular.module('TopbarControllerModule', []).controller('TopbarController', ['$rootScope', '$scope', '$timeout', '$sessionStorage', '$state', 
        function ($rootScope, $scope, $timeout, $sessionStorage, $state) {
	        
	        $scope.user = $rootScope.$sessionStorage.user;
	        
	        $scope.logoff = function () {
                  delete $rootScope.$sessionStorage.user;
                  //$rootScope.$sessionStorage.user = null;
           	  //sessionStorage.clear();  
             	  //$sessionStorage.user = null; 
                  $state.go('login');
            };
        }
    ]);