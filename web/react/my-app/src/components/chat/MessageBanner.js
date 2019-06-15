import React, {Component} from 'react';

import Io from 'socket.io';

class MessageBanner extends Component {

    render() {
        
        
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
        Io.on('chat', (msg) => {
            console.log(msg);
        });
        Io.emit('chat', {
            'content': 'Hi',
        });
    }
}

export default MessageBanner;