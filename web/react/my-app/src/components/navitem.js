import React,{Component} from 'react';
import hightComponent from './HighComponent';
import {BrowserRouter as Router, Link} from 'react-router-dom';

function InputCom(props) {
    return(
        <Router>
            <Link to="/game">{props.labelName}</Link>
        </Router>
    )
}

const HighInputCom = hightComponent('react_key')(InputCom);

class Navitem extends Component {
    constructor(props) {
        super(props);
        // this.state = this.props;
        this.handleClick = this.handleClick.bind(this);
    }
    render() {
        const {labelName, customClass} = this.props;
        return (
            // <li>
            //     <a className={customClass} 
            //     // onClick={(event)=>{this.handleClick(event);}}
            //     onDoubleClick={this.handleClick}
            //     >{labelName}</a>
            // </li>
            <li>
                <HighInputCom labelName={labelName}/>
            </li>
        );
    }
    handleClick(event) {
        event.preventDefault();
        console.log(event);
    }
    handleDBClick = (event) => {
        event.preventDefault();
        console.log(event);
    }
    // 挂载阶段
    componentWillMount() {
        // console.log(`componentWillMount:`);
        // console.log(this.props);
        // console.log(this.state);
    }
    componentDidMount() {
        // console.log(`componentDidMount:`);
        // console.log(this.props);
        // console.log(this.state);
    }
    // 更新阶段
    componentWillReceiveProps(nextProps) {
        // console.log(`componentWillReceiveProps "nextProps":`);
        // console.log(nextProps);
    }
    shouldComponentUpdate(nextProps, nextState) {
        // console.log(`shouldComponentUpdate "nextProps":`);
        // console.log(nextProps);
        // console.log(nextState);
    }
    componentWillUpdate(nextProps, nextState) {
        // console.log(`componentWillUpdate "nextProps:"`);
        // console.log(nextProps);
        // console.log(nextState);
    }
    // render()
    componentDidUpate(prevProps, prevState) {
        // console.log(`componentDidUpate "prevProps":`);
        // console.log(prevProps);
        // console.log(prevState);
    }
    // 卸载阶段
    componentWillUnmount(param) {
        // console.log(`componentWillUnmount "param":`);
        // console.log(param);
    }
};

export default Navitem;