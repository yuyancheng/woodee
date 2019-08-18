
import React from 'react';

const initialState = {
    messageList: {
        title: '',
        content: '',
        author: '',
    },
    contacts: {}
};

// action types
export const types = {
    recieveMessage: 'RECIEVE_MESSAGE',
    sendMessage: 'SEND_MESSAGE',
    updateMessage: 'UPDATE_MESSAGE'
};

// actions creators
export const actions = {
    recieveMessage: (dispatch) => {
        dispatch(types.recieveMessage);
    },
    sendMessage: () => {},
    updateMessage: () => {}
};

// reducers
const getMsgList = (state = initialState.messageList, action) => {
    switch(action.type) {
        case types.recieveMessage:
            return Object.assign(state, {
                title: '周末安排',
                content: '周末一起去图书馆啊'
            });
        case types.sendMessage:
            return {
                ...state,
                sendTime: '2019-08-18'
            };    
    }
};

