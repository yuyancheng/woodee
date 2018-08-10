
//import {router, route, hasHistory} from 'react-router';

var Router = require('react-router').Router
var Route = require('react-router').Route
var Link = require('react-router').Link

// Then we delete a bunch of code from App and
// add some <Link> elements...
const App = React.createClass({
    render() {
        return (
            <div>
                <h1>App</h1>
                {/* change the <a>s to <Link>s */}
                <ul>
                    <li><Link to="/about">About</Link></li>
                    <li><Link to="/inbox">Inbox</Link></li>
                </ul>

                {/*
                 next we replace `<Child>` with `this.props.children`
                 the router will figure out the children for us
                 */}
                {this.props.children}
            </div>
        )
    }
})

// Finally, we render a <Router> with some <Route>s.
// It does all the fancy routing stuff for us.

render((
        <Router history={hashHistory}>
            <Route path="/" component={App}>
                <IndexRoute component={Home}/>
                <Route path="about" component={About}/>
                <Route path="inbox" component={Inbox}/>
            </Route>
        </Router>
    ), document.getElementById('dataTable2')
);

