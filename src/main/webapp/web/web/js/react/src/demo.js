'use strict';

/*import React from 'react'
import { render } from 'react-dom'
import { Router, Route, Link } from 'react-router'*/

import Tpl_login from './login.jsx'

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
                <div>
                    <Actable data={data} dataSrc={data} tHeadKey={['id','name','sex','relation','age','telephone']} tHeadName={
                        <thead>
                            <tr><td>ID</td><td>姓名</td><td>性别</td><td>关系</td><td>年龄</td><td>手机号</td></tr>
                        </thead>
                    } onDataChange={df} translation={translation} processing={true} pagination={{
                        linkSize: 9
                    }} options={{
                        lengthMenu: [3, 5, 10, 20, 50, 100],
                        search: false
                    }} className="table dataTable tbl"/>

                    <button onClick={Alert}>SET</button>
                </div>,
                document.getElementById('dataTable')
            );

            //return;

            const App = React.createClass({
                render() {
                    return (
                        <div>
                            <h1>App</h1>
                            {/* change the <a>s to <Link>s */}
                            <ul>
                                <li><Link to="/login">About</Link></li>
                                <li><Link to="/about">About</Link></li>
                                <li><Link to="/home">Home</Link></li>
                            </ul>

                            {this.props.children}
                        </div>
                    )
                }
            })

            const Home = React.createClass({
                render() {
                    return (
                        <div>
                            <h1>Home</h1>
                            <User ></User>
                        </div>
                    )
                }
            })
            const About = React.createClass({
                params: {
                    id: 'Jkei3c3299999'
                },
                render() {
                    return (
                        <div>
                            <h1>About</h1>
                        </div>
                    )
                }
            })
            const User = React.createClass({

                render() {
                    return (
                        <div>
                            <h1>User</h1>
                        </div>
                    )
                }
            })


            ReactDOM.render((
                    <Router history={hashHistory}>
                        <Route path="/" component={App}>
                            <IndexRoute component={Tpl_login}/>
                            <Route path="login" component={Tpl_login}></Route>
                            <Route path="home" component={Home}></Route>
                            <Route path="about/:id" component={About} params={{id:'Jkei3c3299999'}} />
                        </Route>
                    </Router>
                ), document.getElementById('dataTable2')
            );
        }
    }
});
//57c83519b522256c12804818
