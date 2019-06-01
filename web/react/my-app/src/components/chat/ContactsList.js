
import React from 'react';
import {Component} from 'react';

import dt from '../../assets/js/data/contacts';

class ContactList extends Component {
    constructor (props) {
        super();
        this.state = props;
        //this.showInfo = this.showInfo.bind(this);
    }

    render() {
        const contacts = dt.contacts;
        return (
            <div className="contacts-list">
                <ul>
                    {
                        contacts.map((item, idx) =>
                            <li className="contacts-item" onClick={this.showInfo.bind(this, item.id)}>
                                <i className="icon-close"></i>
                                <b className="msg-num">{item.msgNum > 99 ? '99+' : item.msgNum}</b>
                                <img src={item.icon} />
                                <span>{item.name}</span>
                            </li>
                        )
                    }
                </ul>
            </div>
        );
    }
    showInfo(id) {
        const readInfo = this.state.readInfo;
        readInfo(id);
    }
}

export default ContactList;