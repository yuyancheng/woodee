'use strict';

angular.module('app').run(
    ['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]).config(['$stateProvider', '$urlRouterProvider', 'JQ_CONFIG',
    function ($stateProvider, $urlRouterProvider, JQ_CONFIG) {
        //$urlRouterProvider.when('/app/home');
        //$urlRouterProvider.otherwise('/app/customer_service');
        $urlRouterProvider.otherwise('/access/login');
        $stateProvider
            .state('app', {
                abstract: true,
                url: '/app',
                views: {
                    '': {
                        templateUrl: 'tpl/app.html'
                    },
                    'footer': {
                        template: '<div id="dialog-container" ui-view></div>'
                    },
                    'nav@app': {
                        templateUrl: 'tpl/nav/nav.html'
                    }
                },
                resolve: {
                    deps: ['$ocLazyLoad',
                        function ($ocLazyLoad) {
                            return $ocLazyLoad.load(['js/controllers/nav/nav.js', 'js/directives/ui-medicine.js']);
                        }
                    ]
                }
            })
            .state('app.home', {
                url: '/home',
                templateUrl: 'tpl/home.html',
                resolve: {
                    deps: ['$ocLazyLoad',
                        function($ocLazyLoad) {
                            return $ocLazyLoad.load(['src/js/controllers/home.js']);
                        }
                    ]
                }
            })
            .state('access', {
                url: '/access',
                template: '<div ui-view class="fade-in-right-big smooth"></div>'
            })
            .state('access.login', {
                url: '/login',
                templateUrl: 'tpl/login.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controllers/login.js']);
                        }
                    ]
                }
            })
            .state('access.register', {
                url: '/register',
                templateUrl: 'tpl/register.html',
                resolve: {
                    deps: ['uiLoad',
                        function (uiLoad) {
                            return uiLoad.load(['js/controllers/register.js']);
                        }
                    ]
                }
            })
            .state('app.relationship.list.delete', {
                url: '/delete',
                views: {
                    "modalDialog@app": {
                        templateUrl: 'tpl/group/relationship_delete.html'
                    }
                },
                resolve: {
                    deps: ['$ocLazyLoad',
                        function ($ocLazyLoad) {
                            return $ocLazyLoad.load('js/controllers/group/relationship_delete.js');
                        }
                    ]
                }
            })
            .state('app.followUp', {
                url: '/followUp/:planId',
                templateUrl: 'tpl/care/followUp/followUp.html',
                resolve: {
                    deps: ['$ocLazyLoad', 'uiLoad',
                        function ($ocLazyLoad, uiLoad) {
                            return $ocLazyLoad.load([
                                // 时间选择组件
                                'components/timeSetCpn/timeSetCpnDirective.js'
                            ]).then(
                                function () {
                                    return $ocLazyLoad.load('angularBootstrapNavTree');
                                }
                            ).then(function () {
                                return uiLoad.load(JQ_CONFIG.dataTable.concat(JQ_CONFIG.tree, JQ_CONFIG.databox));
                            });
                        }
                    ]
                }
            })
            .state('access.404', {
                url: '/404',
                templateUrl: 'tpl/404.html'
            });
    }
]);
