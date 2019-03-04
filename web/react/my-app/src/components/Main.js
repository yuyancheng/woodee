import React, {Component} from 'react';
import PropTypes from 'prop-types';

import '../assets/css/main.css';
import {print} from './common/LifeCircleLine';

function FunCom2(props) {
    return <input ref={props.iptRef} defaultValue="123" />
}

class FunCom extends Component{
    render() {
        return <input ref={this.props.iptRef} defaultValue="234" />
    }
}

class Main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: 1,
            funCom: null
        };
        this.updateTime = this.updateTime.bind(this);
        this.handleClick = this.handleClick.bind(this);
        this.handleCheckChange = this.handleCheckChange.bind(this);
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
                <input type="checkbox" name="autoCheckIn" defaultChecked={true} onChange={this.handleCheckChange} />
                <input type="checkbox" name="autoCheckIn" defaultChecked={false} onChange={this.handleCheckChange} />
                <input type="radio" name="remember" defaultChecked={false} />
                <FunCom iptRef={funCom => {this.funCom = funCom;}}/>
                <FunCom2 iptRef={com => {this.funCom2 = com;}}/>
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
        this.setState({
            [event.target.name]: event.target.checked
        });
    }
    handleClick(event) {
        event.preventDefault();
        this.setState({
            data: this.state.data + 1
        });

        console.log(this.context.getNavbar());
        console.log(this.funCom.value);
        console.log(this.funCom2.value);
    }
};

Main.contextTypes = {
    getNavbar: PropTypes.node
};

export default Main;