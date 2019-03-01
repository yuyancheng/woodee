import React, {Component} from 'react';
import PropTypes from 'prop-types';

import '../assets/css/main.css';
import {print} from './common/LifeCircleLine';

class Main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: 1
        };
        this.updateTime = this.updateTime.bind(this);
        this.handleClick = this.handleClick.bind(this);
        print(this);
    }
    render() {
        const items = [{
            labelName: '大数据-实时计算1',
            customClass: '',
        }, {
            labelName: '大数据-实时计算2',
            customClass: '',
        }];
        return (
            <form className="login-status">
                <input type="text" name="userName" value={this.state.data} onChange={this.handleTextChange} />
                <input type="password" name="password" onChange={this.handlePasswordChange} />
                <input type="checkbox" name="autoCheckIn" defaultChecked={false} onChange={this.handleCheckChange} />
                <input type="radio" name="remember" defaultChecked={false} />
                <button type="button" value="CLICK" onClick={this.handleClick}>CLICK ME</button>
            </form>
        );
    }
    updateTime() {
        this.timer = this.setState({
            date: new Date()
        });
    }
    componentDidMount() {
        // setInterval(() => this.updateTime(), 1000);
    }
    componentWillUnmount() {
        clearInterval(this.timer);
    }
    handleTextChange(event) {

    }
    handlePasswordChange(event) {

    }
    handleCheckChange(event) {

    }
    handleClick(event) {
        event.preventDefault();
        this.setState({
            data: this.state.data + 1
        });

        console.log(this.context.getNavbar());
    }
};

Main.contextTypes = {
    getNavbar: PropTypes.node
};

export default Main;