'use strict';

/*import React from 'react'
import { render } from 'react-dom'
import { Router, Route, Link } from 'react-router'*/


var Router = ReactRouter.Router
var Route = ReactRouter.Route
var Link = ReactRouter.Link
var hashHistory = ReactRouter.hashHistory
var IndexRoute = ReactRouter.IndexRoute

$.ajax({
    type: 'post',
    url: 'js/data/patient.json',
    success: function (resp) {
        //console.log(resp.pageData)
        var tHead = {
            'ID': 'id',
            '姓名': 'name',
            '性别': 'sex',
            '关系': 'relation',
            '年龄': 'age',
            '手机号': 'telephone'
        };

        var dt = [{
            "id": 1,
            "relation": "兄弟1",
            "name": "王大1",
            "sex": "男1",
            "age": "441",
            "telephone": "16890988900"
        }];

        var data;
        var df = {};
        df.setSt = function () {
            alert(3243)
        };

        var  Alert  = function(){
            //data = dt;
            df.setSt(dt);
        };

        var translation = {
            "lengthMenu": "每页 _MENU_ 条",
            "zeroRecords": "无数据",
            "processing": "<img style='position:fixed;top:30%;left:50%;z-index:10000;height:80px;border:1px solid #aaa;border-radius:6px;box-shadow:0 0 10px rgba(0,0,0,.5)' src=\"src/img/loading.gif\" />",
            "info": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
            "infoEmpty": "",
            "infoFiltered": "(从 _MAX_ 条记录中过滤)",
            "search": "搜索",
            "paginate": {
                "sFirst": "<<",
                "sPrevious": "<",
                "sNext": ">",
                "sLast": ">>"
            }
        };

        if(resp.pageData.length > 0){

            data = resp.pageData;

            ReactDOM.render(
                React.createElement("div", null, 
                    React.createElement(Actable, {data: data, dataSrc: data, tHeadKey: ['id','name','sex','relation','age','telephone'], tHeadName: 
                        React.createElement("thead", null, 
                            React.createElement("tr", null, React.createElement("td", null, "ID"), React.createElement("td", null, "姓名"), React.createElement("td", null, "性别"), React.createElement("td", null, "关系"), React.createElement("td", null, "年龄"), React.createElement("td", null, "手机号"))
                        ), 
                    onDataChange: df, translation: translation, processing: true, pagination: {
                        linkSize: 9
                    }, options: {
                        lengthMenu: [3, 5, 10, 20, 50, 100],
                        search: false
                    }, className: "table dataTable tbl"}), 

                    React.createElement("button", {onClick: Alert}, "SET")
                ),
                document.getElementById('dataTable')
            );

            //return;

            const App = React.createClass({displayName: "App",
                render() {
                    return (
                        React.createElement("div", null, 
                            React.createElement("h1", null, "App"), 
                            /* change the <a>s to <Link>s */
                            React.createElement("ul", null, 
                                React.createElement("li", null, React.createElement(Link, {to: "/login"}, "About")), 
                                React.createElement("li", null, React.createElement(Link, {to: "/about"}, "About")), 
                                React.createElement("li", null, React.createElement(Link, {to: "/home"}, "Home"))
                            ), 

                            this.props.children
                        )
                    )
                }
            })

            const Home = React.createClass({displayName: "Home",
                render() {
                    return (
                        React.createElement("div", null, 
                            React.createElement("h1", null, "Home"), 
                            React.createElement(User, null)
                        )
                    )
                }
            })
            const About = React.createClass({displayName: "About",
                params: {
                    id: 'Jkei3c3299999'
                },
                render() {
                    return (
                        React.createElement("div", null, 
                            React.createElement("h1", null, "About")
                        )
                    )
                }
            })
            const User = React.createClass({displayName: "User",

                render() {
                    return (
                        React.createElement("div", null, 
                            React.createElement("h1", null, "User")
                        )
                    )
                }
            })


            ReactDOM.render((
                    React.createElement(Router, {history: hashHistory}, 
                        React.createElement(Route, {path: "/", component: App}, 
                            React.createElement(IndexRoute, {component: Home}), 
                            React.createElement(Route, {path: "home", component: Home}), 
                            React.createElement(Route, {path: "about/:id", component: About, params: {id:'Jkei3c3299999'}})
                        )
                    )
                ), document.getElementById('dataTable2')
            );
        }
    }
});
//57c83519b522256c12804818
