'use strict';

/*import React from 'react'
import { render } from 'react-dom'
import { Router, Route, Link } from 'react-router'*/

var Router = ReactRouter.Router
var Route = ReactRouter.Route
var Link = ReactRouter.Link
var hashHistory = ReactRouter.hashHistory
var IndexRoute = ReactRouter.IndexRoute

const App = React.createClass({displayName: "App",
    render() {
        return (
            React.createElement("div", null, 
                this.props.children
            )
        )
    }
})

const Tpl_login = React.createClass({displayName: "Tpl_login",
    render() {
        return (
            React.createElement("div", {className: "m-b-lg clear"}, 
                React.createElement("div", {className: "login-right"}, 
                    React.createElement("h4", {className: "text-center"}, "登    录"), 
                    React.createElement("form", {name: "form", className: "form-validation"}, 
                        React.createElement("div", {className: "text-danger wrapper text-center", "ng-show": "authError || loginError"}), 
                        React.createElement("div", {id: "login_with_pswd"}, 
                            React.createElement("div", {className: "list-group list-group-sm"}, 
                                React.createElement("div", {className: "list-group-item"}, 
                                    React.createElement("input", {type: "text", placeholder: "玄关医生账号(手机号)", className: "form-control no-border", "ng-model": "user.telephone", tabindex: "1", required: true})
                                ), 
                                React.createElement("div", {className: "list-group-item"}, 
                                    React.createElement("input", {type: "password", placeholder: "玄关医生密码", className: "form-control no-border", "ng-model": "user.password", tabindex: "2", required: true})
                                )
                            )
                        ), 
                        React.createElement("button", {type: "submit", className: "btn btn-lg btn-success btn-block", onClick: "login()"}, "登  录"), 
                        React.createElement("a", {href: "access.register", className: "btn btn-lg btn-primary btn-block"}, "注  册")
                    )
                )
            )
        )
    }
});

ReactDOM.render((
        React.createElement(Router, {history: hashHistory}, 
            React.createElement(Route, {path: "/", component: App}, 
                React.createElement(IndexRoute, {component: Tpl_login}), 
                React.createElement(Route, {path: "login", component: Tpl_login})
            )
        )
    ), document.getElementById('login_wrapper')
);