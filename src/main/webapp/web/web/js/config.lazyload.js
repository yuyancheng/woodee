angular.module('app')
    .constant('JQ_CONFIG', {
        moment: ['lib/moment/moment.js'],
        dateTimePicker: ['lib/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js', 'lib/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css', 'lib/bootstrap-datetimepicker-master/js/locales/bootstrap-datetimepicker.zh-CN.js'],
        clipboard: ['lib/clipboard/dist/clipboard.min.js'],
    })
    .config(['$ocLazyLoadProvider',
        function($ocLazyLoadProvider) {
            $ocLazyLoadProvider.config({
                debug: false,
                events: true,
                modules: [{
                    name: 'ui.bootstrap',
                    files: ['lib/angular-ui-bootstrap/ui-bootstrap-tpls-0.14.2.min.js']
                }, {
                    name: 'ngFileUpload',
                    files: ['lib/angular-file-upload/angular-file-upload.min.js']
                }, {
                    name: 'angularBootstrapNavTree',
                    files: ['lib/angular-bootstrap-nav-tree/dist/abn_tree_directive.js', 'lib/angular-bootstrap-nav-tree/dist/abn_tree.css']
                }]
            });
        }
    ]);
