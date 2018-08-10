// config
var app = angular.module('app').config(
    ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$stateProvider',
        function ($controllerProvider, $compileProvider, $filterProvider, $provide, $stateProvider) {
            // lazy controller, directive and service
            app.controller = $controllerProvider.register;
            app.directive = $compileProvider.directive;
            app.filter = $filterProvider.register;
            app.factory = $provide.factory;
            app.service = $provide.service;
            app.constant = $provide.constant;
            app.value = $provide.value;

            app.code = {
                'OK': '1111',
                'ERROR': '1110',
                'WARNING': '1110',
                'NOT_FOUND': '0000'
            };

            // API路径集合
            app.urlRoot = '/api/';

            var common = {
                list: 'list.iv',
                save: 'save.iv',
                edit: 'edit.iv',
                modify: 'modify.iv',
                delete: 'delete.iv',
                find: 'findByIds.iv'
            };

            function getApi(name, common) {
                var apis = {};
                for (var n in common) {
                    apis[n] = serverApiRoot + name + '/' + common[n];
                }
                return apis;
            }

            // 定义统一api路径
            app.url = {
                access_token: localStorage.getItem('access_token'),
                groupId: function () {
                    return localStorage.getItem('curGroupId');
                },

                login: app.urlRoot + 'user/login',
                logout: app.urlRoot + 'user/logout',
                register: app.urlRoot + 'user/register',
                admin: {
                    check: {
                        getDoctors: app.urlRoot + 'admin/check/getDoctors',
                    }
                }
            };
        }
    ]).factory('authorityInterceptor', [
    function () {
        var authorityInterceptor = {
            response: function (response) {
                //console.log(response);
                if (response.resultCode === 1030102 || response.resultCode === 1030101 || response.data.resultCode === 1030102 || response.data.resultCode === 1030101) {
                    window.location.href = '#/access/signin';
                }
                if ('no permission' == response.data) {
                    app.controller('Interceptor', ['$state',
                        function ($state) {
                            app.state.go('access.404');
                        }
                    ]);
                }
                if ("no login" == response.data) {
                    app.state.go('access.signin');
                }
                return response;
            }
        };
        return authorityInterceptor;
    }
]).config(['$httpProvider',
    function ($httpProvider) {
        $httpProvider.interceptors.push('authorityInterceptor');
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
        $httpProvider.defaults.transformRequest = [

            function (data) {
                return angular.isObject(data) && String(data) !== '[object File]' ? $.param(data, true) : data;
            }
        ];
    }

]);
(function () {
    angular.module('app').directive('ngEnter', function () {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
                    scope.$apply(function () {
                        scope.$eval(attrs.ngEnter);
                    });

                    event.preventDefault();
                }
            });
        };
    });
})();
