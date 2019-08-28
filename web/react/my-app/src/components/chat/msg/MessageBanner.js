import React, {Component} from 'react';
import IO from 'socket.io-client';
// console.log(IO);
const Socket = IO('http://localhost:3030/');

class MessageBanner extends Component {

    render() {
        // Socket.on('chat', (msg) => {
        //     console.log('/////////////////// 1');
        //     console.log(msg);
        // });
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
        // Socket.on('chat', (msg) => {
        //     console.log('/////////////////// 2');
        //     console.log(msg);
        // });
        Socket.emit('chat', {
            'content': 'Hi',
        });
    }
}

export default MessageBanner;