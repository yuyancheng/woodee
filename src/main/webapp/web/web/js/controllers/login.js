'use strict';

// 集团登录控制器
app.controller('SigninFormController', ['$scope', '$http', '$state', '$rootScope',
    function($scope, $http, $state, $rootScope) {
        var width_pswd,
            width_code,
            smsid = '',
            _remember;

        $scope.user = {};
        $scope.authError = null;

        $scope.login = function() {

            $scope.authError = null;

            $http({
                url: app.url.login,
                method: 'post',
                data: {
                    telephone: $scope.user.telephone,
                    password: $scope.user.password
                }
            }).then(function(response) {
                if (response.data.resultCode === 1) {
                } else {
                    $scope.authError = response.data.msg;
                }
            }, function(x) {
                $scope.authError = '服务器错误';
            });
        };

        $scope.register = function() {
            $state.go('access.register');
        };
    }
]);
