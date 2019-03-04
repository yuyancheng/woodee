import React from 'react';

const HighComponent = (key) => (TargetCompoment) => {
    return class Navitem extends React.Component {
        componentWillMount() {
            this.localData = window.localStorage.getItem(key);
        }
        render() {
            return (
                <TargetCompoment localData={this.localData}/>
            );
        }
    };
}

export default HighComponent;