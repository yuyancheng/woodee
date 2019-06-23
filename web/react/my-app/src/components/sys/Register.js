import React, { Component } from 'react'

export default class Register extends Component {
    render() {
        return (
            <div>
                <form>
                    <input type="text" name="userName" value={this.state.data} onChange={this.handleUserNameChange} />
                    <input type="text" name="telephone" onChange={this.handleTelephoneChange} />
                    <input type="checkbox" name="autoCheckIn" defaultChecked={true} onChange={this.handleCheckChange} />
                    <input type="checkbox" name="autoCheckIn" defaultChecked={false} onChange={this.handleCheckChange} />
                    <input type="radio" name="remember" defaultChecked={false} />
                    <button type="button" value="CLICK" onClick={this.handleClick}>确 定</button>
                </form>
            </div>
        )
    }
    handleUserNameChange() {

    }
    handleCheckChange() {

    }
    handleTelephoneChange() {

    }
}
