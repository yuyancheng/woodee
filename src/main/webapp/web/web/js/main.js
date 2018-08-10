'use strict';
angular.module('app').controller('AppCtrl', ['$rootScope', '$scope', '$window', '$http', '$state',
    function($rootScope, $scope, $window, $http, $state) {

        //var dt = Group.getData();
        // add 'ie' classes to html
        //var isIE = !!navigator.userAgent.match(/MSIE/i);
        //isIE && angular.element($window.document.body).addClass('ie');
        //isSmartDevice($window) && angular.element($window.document.body).addClass('smart');
        app.state = $state;
        // config
        $scope.app = {
            name: '玄关健康平台',
            version: '1.0.0',
            // for chart colors
            color: {
                primary: '#7266ba',
                info: '#23b7e5',
                success: '#27c24c',
                warning: '#fad733',
                danger: '#f05050',
                light: '#e8eff0',
                dark: '#3a3f51',
                black: '#1c2b36'
            },
            settings: {
                themeID: 8,
                navbarHeaderColor: 'bg-black',
                navbarCollapseColor: 'bg-white-only',
                asideColor: 'bg-black',
                headerFixed: true,
                asideFixed: false,
                asideFolded: false,
                asideDock: false,
                container: false
            }
        };

        // save settings to local storage
        /*if (angular.isDefined($localStorage.settings)) {
            $scope.app.settings = $localStorage.settings;
        } else {
            $localStorage.settings = $scope.app.settings;
        }*/
        /*$scope.$watch('app.settings', function() {
            if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
                // aside dock and fixed must set the header fixed.
                $scope.app.settings.headerFixed = true;
            }
            // save to local storage
            $localStorage.settings = $scope.app.settings;
        }, true);*/

        // 用户退出
        $scope.logout = function() {
            $http.get(app.url.logout + '?' + $.param({
                access_token: app.url.access_token
            })).then(function(response) {
                if (response.statusText === 'OK') {

                    if (!$rootScope.isCompany) {
                        $state.go('access.signin', {
                            "reload": true
                        });
                    } else {
                        $state.go('access.enterprise_signin', {
                            "reload": true
                        });
                    }
                } else {
                    console.log("Logout: " + response.statusText);
                }
            }, function(x) {
                console.log("Logout: " + x.statusText);
            });
        };

        function isSmartDevice($window) {
            // Adapted from http://www.detectmobilebrowsers.com
            var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
            // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
            return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
        }

        //$state.go('access.signin');
    }
]);
