import React, {Component} from 'react';

class ContactSearch extends Component {
    constructor(props) {
        super(props);
        this.state = props;
    }
    render() {
        return (
            <div>
                <input type="text" placeholder="关键字" />
                <i className="fa icon-magnifier"></i>
            </div>
        );
    }
}

export default ContactSearch;