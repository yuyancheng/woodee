import React, {Component} from 'react';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';

import Register from '../../sys/Register';


class UserInfo extends Component {
    constructor(props) {
        super(props);
        this.state = props;
    }

    render() {
        const menuItems = [{
            name: '设置',
            url: '/settings',
        }, {
            name: '论坛',
            url: '/forum2',
        }, {
            name: '添加用户',
            url: '/register',
        }];
        return (
            <div>
                <Router>
                <ul className="menu menu-pop">
                    {
                        menuItems.map((v) => {
                            return (
                                <li className="menu-item">
                                    <Link to={v.url} >{v.name}</Link>
                                </li>
                            );
                        })
                    }
                </ul>
                </Router>
            </div>
        );
    }
}

export default UserInfo;