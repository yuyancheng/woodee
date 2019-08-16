import React, {Component} from 'react';
import io from 'socket.io';
// const Socket = io.connect('http://localhost/');

class MessageBanner extends Component {

    render() {
        // IO.on('chat', (msg) => {
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
        //     console.log(msg);
        // });
        // Socket.emit('chat', {
        //     'content': 'Hi',
        // });
    }
}

export default MessageBanner;