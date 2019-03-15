import React from 'react';
import {BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';

import Home from './Home';
import PostList from './PostList';

class Index extends React.Component {
    render() {
        const lists = [{
            postName: '如何打造个性化服务平台1',
            id: 1
        }, {
            postName: '如何打造个性化服务平台2',
            id: 2
        }, {
            postName: '如何打造个性化服务平台3',
            id: 3
        }];
        return (
            <div>
                <Router>
                    <Switch>
                        <Route path="/forum" component={Home} exact></Route>
                        <Route path="/forum/home" component={Home}></Route>
                        <Route path="/forum/list" component={PostList}></Route>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default Index;