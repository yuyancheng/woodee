import React from 'react';

class Game extends React.Component {
    constructor(props) {
        super(props);
        this.state = props;
    }
    render() {
        return (
            <div style={{width: '400px', height: '300px', backgroundColor: '#999'}}>a game scence</div>
        );
    }
}

export default Game;