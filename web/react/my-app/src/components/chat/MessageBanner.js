import React, {Component} from 'react';
import IO from 'socket.io';

import Io from 'socket.io';

class MessageBanner extends Component {

    render() {
        IO.on('chat', (msg) => {
            console.log(msg);
        });
        return (
            <div className="msg-banner">
                <div className="msg-input" contenteditable="true"></div>
                <div className="btn-send">
                    <button type="button" className="btn btn-success" onClick={this.send}>发送</button>
                </div>
            </div>
        );
    }
    send() {
        IO.emit('chat', {
            'content': 'hi',
        });
    }
}

export default MessageBanner;