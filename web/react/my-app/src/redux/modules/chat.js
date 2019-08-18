
import React from 'react';


// action types
const actions = {
    recieveMessage: 'RECIEVE_MESSAGE',
    sendMessage: 'SEND_MESSAGE',
    updateMessage: 'UPDATE_MESSAGE'
};

// action creators
const actionCreators = () => {
    return {
        recieveMessage: (dispatch) => {
            dispatch(actions.recieveMessage);
        },
        sendMessage: () => {},
        updateMessage: () => {}
    };
};

// reducers
const 