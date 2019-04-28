import React, {Component} from 'react';

class MessageBanner extends Component {
    render() {
        return (
            <div className="msg-banner">
                <div className="msg-input" contenteditable="true"></div>
                <div className="btn-send">
                    <button type="button" className="btn btn-success">发送</button>
                </div>
            </div>
        );
    }
}

export default MessageBanner;