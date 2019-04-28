import React, {Component} from 'react';
import MessageItem from './MessageItem';

class MessagePanel extends Component {
    render() {
        const msgs = this.props['msg-data'];
        return (
            <div className="msg-panel">
                {
                    msgs.map((item, idx) => 
                        <MessageItem key={item.id} data={item}></MessageItem>
                    )
                }
            </div>    
        );
    }

    componentWillReciveProps(props, dt) {
        console.log(props, dt);
    }
}

export default MessagePanel;