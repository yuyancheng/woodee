import React, {Component} from 'react';

class MessageItem extends Component {
    constructor(props) {
        super();
        this.state = props;
    }
    render() {
        const dt = this.state.data;
        const date = dt.date;
        const isMe = dt.isMe;
        const senderName = dt.senderName;
        const senderIcon = dt.senderIcon;
        const images = dt.images;
        let msgStr = dt.text;
        let imgTagStr = '';
        // let msgArr = msgStr.split('#img#');

        images.map((img, i) => {
            imgTagStr = `<img src='${img}' />`;
            msgStr = msgStr.replace('#img#', imgTagStr);
        });

        return (
            <div>
                <div className="msg-item-time">{date}</div>
                <div className={isMe ? 'msg-item-rt' : 'msg-item-lt'}>
                    <div className="msg-item-info">
                        <div className="msg-item-pic">
                            <img src={senderIcon} />
                        </div>
                        <div className="msg-item-body">
                            {
                                !isMe ? <div className="msg-item-sender">{senderName}</div> : ''
                            }
                            <div className="msg-item-content">
                                <i className="fa fa-comment"></i>
                                <div className="msg-item-wrapper" dangerouslySetInnerHTML={{__html:msgStr}}>{/* {msgStr} */}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
    componentWillReciveProps(props, dt) {
        console.log(props, dt);
    }
    componentWillMount() {

    }
    componentDidMount() {

    }
    shouldComponentUpdate() {

    }
    componentWillUnmount() {
        
    }
}

export default MessageItem;