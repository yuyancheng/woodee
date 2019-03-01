import React, {Component} from 'react';
import PropTypes from 'prop-types';
// import logo from './logo.svg';
import logo from './assets/img/logo.png';
import './App.css';

import './assets/css/common.css';
import TopNav from './components/TopNav';
import LoginStatus from './components/LoginStatus';
import SideNav from './components/SideNav';
import Main from './components/Main';



class App extends Component {
    render () {
        return (
            <div className="app">
                <header className="app-header">
                    {/* logo */}
                    <div className="app-logo">
                        <img src={logo} className="" alt="logo"/>
                    </div>
                    {/* 顶部导航 */}
                    <TopNav className="top-nav" />
                    {/* 登录状态 */}
                    <LoginStatus className="login-status" />
                </header>
                
                <div>
                    {/* 侧边导航 */}
                    <aside className="side-nav">
                        <SideNav />
                    </aside>
                    {/* 主体内容 */}
                    <div className="app-main">
                        <Main />
                    </div>
                </div>

                <footer className="app-footer"></footer>
            </div>
        );
    }
    getNavbar() {
        return TopNav;
    }
    getChildContext() {
        return {
            getNavbar: this.getNavbar
        };
    }
}

App.childContextTypes = {
    getNavbar: PropTypes.node
};

export default App;
