'use strict';

// 注册控制器
app.controller('SignupFormController', ['$scope', '$http', '$state',
    function ($scope, $http, $state) {
        $scope.user = {};
        $scope.authError = null;
        $scope.signup = function () {
            $http.post(app.url.register, {
                telephone: $scope.user.telephone,
                name: $scope.user.name,
                password: $scope.user.password
            }).then(function (response) {
                if (response.data.code == app.code.OK) {
                    $state.go('access.login');
                } else {
                    $scope.authError = response.data.msg;
                    alert(response.data.msg);
                }
            }, function (x) {
                $scope.authError = '服务器错误！';
            });
        };
    }
]);