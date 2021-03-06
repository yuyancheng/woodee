import React, {Component} from 'react';
import ContactList from './ContactsList';
import FrameOpr from './FrameOpr';
import MessagePanel from './msg/MessagePanel';
import UserInfo from './menu/UserInfo';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';

import '../../libs/bootstrap/bootstrap.css';
import '../../libs/animate.css';
import '../../libs/font-awesome/css/font-awesome.min.css';
import '../../libs/simple-line-icons/css/simple-line-icons.css';
import '../../assets/css/chat.css';
import MessageBanner from './msg/MessageBanner';
import msgs from '../../assets/js/data/msgs';
import ContactSearch from './ContactSearch';

import Register from '../sys/Register';
import Setting from '../sys/Setting';

class MainFrame extends Component {
    constructor(props) {
        super();
        this.state = props;
    }
    render() {
        const msgData = msgs['100'];
        return (
            <div id="msg_window" className="msg-window full-screen none">
                <div className="win-lt move-point">
                    <div className="user-pic" onClick={this.showMenu.bind(this, 1234)}>
                        <img src={require('../../assets/img/chat/c4.jpg')} />
                    </div>
                    <UserInfo></UserInfo>
                </div>
                <div className="win-md">
                    <div className="contacts-find move-point">
                        <ContactSearch></ContactSearch>
                    </div>
                    <ContactList readInfo={this.readInfo}></ContactList>
                </div>
                <div className="win-rt">
                    <FrameOpr></FrameOpr>
                    <div className="main-panel">
                        <div className="main-panel-rt"></div>
                        <div className="main-panel-lt">
                            <MessagePanel msg-data={this.state.msgData}></MessagePanel>
                            <MessageBanner></MessageBanner>
                        </div>
                    </div>
                </div>
                
                {/* <Router>
                    <Switch>
                        <Route path="/chat/settings" component={Setting}></Route>
                        <Route path="/register" component={Register}></Route>
                    </Switch>
                </Router>  */}
            </div>
        );
    }
    componentWillMount() {
        this.setState({
            msgData: msgs['100']
        });
    }
    readInfo = (id) => {
        const msgData = msgs[id] || [];
        const p = new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve();
            }, 500);
        });
        p.then((v) => {
            this.setState({
                msgData
            });
        });
        
    }
    showMenu = (v) => {
        console.log(v);
    };
};

export default MainFrame;