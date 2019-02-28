import React, {Component} from 'react';
import Item from './navitem';

import '../assets/css/login-status.css';

class Navbar extends Component {
    render() {
        const items = [{
            labelName: '大数据-实时计算1',
            customClass: '',
        }, {
            labelName: '大数据-实时计算2',
            customClass: '',
        }];
        return (
            <ul className="login-status">
            {
                items.map((item, idx) => <Item key={idx} customClass="nav-item" labelName={item.labelName}/>)
            }
            </ul>
        );
    }
};

export default Navbar;