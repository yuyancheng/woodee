'use strict';

/*import React from 'react'
import { render } from 'react-dom'
import { Router, Route, Link } from 'react-router'*/

var Router = ReactRouter.Router
var Route = ReactRouter.Route
var Link = ReactRouter.Link
var hashHistory = ReactRouter.hashHistory
var IndexRoute = ReactRouter.IndexRoute

const App = React.createClass({
    render() {
        return (
            <div>
                {this.props.children}
            </div>
        )
    }
})

const Tpl_login = React.createClass({
    render() {
        return (
            <div className="m-b-lg clear">
                <div className="login-right">
                    <h4 className="text-center">登&nbsp;&nbsp;&nbsp;&nbsp;录</h4>
                    <form name="form" className="form-validation">
                        <div className="text-danger wrapper text-center" ng-show="authError || loginError"></div>
                        <div id="login_with_pswd">
                            <div className="list-group list-group-sm">
                                <div className="list-group-item">
                                    <input type="text" placeholder="玄关医生账号(手机号)" className="form-control no-border" ng-model="user.telephone" tabindex="1" required />
                                </div>
                                <div className="list-group-item">
                                    <input type="password" placeholder="玄关医生密码" className="form-control no-border" ng-model="user.password" tabindex="2" required />
                                </div>
                            </div>
                        </div>
                        <button type="submit" className="btn btn-lg btn-success btn-block" onClick="login()">登&nbsp;&nbsp;录</button>
                        <a href="access.register" className="btn btn-lg btn-primary btn-block">注&nbsp;&nbsp;册</a>
                    </form>
                </div>
            </div>
        )
    }
});

ReactDOM.render((
        <Router history={hashHistory}>
            <Route path="/" component={App}>
                <IndexRoute component={Tpl_login}/>
                <Route path="login" component={Tpl_login} />
            </Route>
        </Router>
    ), document.getElementById('login_wrapper')
);