import React, { Component } from 'react';
import Item from './Navitem';
import '../assets/css/top-nav.css';

class Navbar extends Component {
  render() {
    const items = [
      {
        labelName: '大数据-实时计算1',
        customClass: '',
      },
      {
        labelName: '大数据-实时计算2',
        customClass: '',
      },
    ];
    return (
      <ul className="top-nav">
        {items.map((item, idx) => (
          <Item key={idx} customClass="nav-item" labelName={item.labelName} />
        ))}
      </ul>
    );
  }
}

export default Navbar;
