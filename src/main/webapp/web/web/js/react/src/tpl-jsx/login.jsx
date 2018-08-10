import React, { Component, PropTypes } from "react";

export default class Tpl_login extends Component{
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
                        <button type="submit" className="btn btn-lg btn-success btn-block" ng-click="login()" ng-disabled='form.$invalid'>登&nbsp;&nbsp;录</button>
                        <a ui-sref="access.register" className="btn btn-lg btn-primary btn-block">注&nbsp;&nbsp;册</a>
                    </form>
                </div>
            </div>
        )
    }
};