var angularAPP = angular.module('angularAPP', ['ui.router', 'ui.grid', 'ui.grid.infiniteScroll', 'ngStorage', 'ui.bootstrap','HostServiceModule','UserServiceModule',/*'LogServiceModule','logControllerModule',*/'mainbodyControllerModule','TopbarControllerModule',/*'datatables',*/'ngResource','MainBodyServiceModule','angularSpinner','UtilityServiceModule','ui.grid.edit', 'ui.grid.cellNav','ui.grid.exporter','ui.grid.pagination','ui.grid.resizeColumns','ui.grid.autoResize','ui.grid.selection','angularSpinner']);

angularAPP.run(['$rootScope', '$state', '$stateParams', '$localStorage','$sessionStorage','MainBodyService','UserService', function($rootScope, $state, $stateParams, $localStorage, $sessionStorage ,MainBodyService,UserService) {
	$rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$localStorage = $localStorage;
    $rootScope.$sessionStorage = $sessionStorage;
    $rootScope.showLoader = {
        show: false
    };

    if($rootScope.$sessionStorage.user){
        $state.go('list');   
    }else{
        $state.go('login');    	
    }
  }
]);

angularAPP.config(['$stateProvider','$urlRouterProvider','usSpinnerConfigProvider','$httpProvider',  function($stateProvider, $urlRouterProvider,usSpinnerConfigProvider,$httpProvider){
	usSpinnerConfigProvider.setTheme('bigBlue', {color: 'blue', radius: 20});
    usSpinnerConfigProvider.setTheme('smallRed', {color: 'red', radius: 6});
    usSpinnerConfigProvider.setTheme('smallBlue', {color: 'blue', radius: 6});
    usSpinnerConfigProvider.setTheme('smallGray', {color: '#bcbcbc', radius: 6});
    
    if( !$httpProvider.defaults.headers.get ) {
        $httpProvider.defaults.headers.get = {};
    }
    // 禁用 IE AJAX 请求缓存
    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    
    $stateProvider
    .state('login', {
        views: {
            'login': {
                templateUrl: 'ngviews/login.html',
                controller: ['$rootScope', '$scope', '$state', '$localStorage','$sessionStorage','UserService','usSpinnerService', function($rootScope, $scope, $state, $localStorage,$sessionStorage, UserService,usSpinnerService) {
                    $scope.login = function() {
                      $scope.loginasuser();
                    }
                    $scope.loginasuser = function() {
                    	usSpinnerService.spin('spinner-1');
                        UserService.login($scope.userName, $scope.password).success(function(data, status) {
                        	usSpinnerService.stop('spinner-1');

                                $rootScope.user = {
                                	userName: data.username,
                                	password: data.password
                                };

                                $sessionStorage.user = $rootScope.user;
                                
                                $state.go('list');

                        }).error(function(data, status) {
                            alert('Failed to login.'); //hardcode
                            usSpinnerService.stop('spinner-1');
                        });
                    };
                    
                    $rootScope.$on('us-spinner:spin', function(event, key) {
                        $scope.spinneractive = true;
                      });

                      $rootScope.$on('us-spinner:stop', function(event, key) {
                        $scope.spinneractive = false;
                      });
                }]
            }
        }
    })
    .state('list', {
        views: {
            'topbar': {
                templateUrl: 'ngviews/topbar.html',
                controller: 'TopbarController'
            },
            'mainbody': {
                templateUrl: 'ngviews/mainbody.html',
                controller: 'mainbodyController'
            }
        }
    })
}]);
