import React, {Component} from 'react';

export default (() => {
    return (
        <div className="win-top-bar move-point">
            <div className="win-opr-btn">
                <span className="min-view"><i></i></span>
                <span className="mid-view"><i></i></span>
                <span className="max-view"><i></i><b></b></span>
                <span className="cls-view"><i className="fa fa-times"></i></span>
            </div>
        </div>
    );
});