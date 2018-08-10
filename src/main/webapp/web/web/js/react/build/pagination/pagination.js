'use strict';

var Pagination = React.createClass({displayName: "Pagination",
    getInitialState: function () {
        //console.log('pagination: ' + this.props.total);
        this.index = 0;
        this.size = 0;
        this.links = [];
        this.linkSize = 10;

        if (this.props.options.linkSize) {
            this.linkSize = this.props.options.linkSize;
        }

        return {
            links: this.links
        };
    },
    turnTo: function (idx) {
        this.index = idx;
        this.props.host.updateDataSource(idx);
        this.rePaint();
    },
    prev: function () {
        if(this.index > 0){
            this.index --;
        }
        this.turnTo(this.index);
    },
    next: function () {
        if(this.index < this.size - 1){
            this.index ++;
        }
        this.turnTo(this.index);
    },
    init: function () {
        var total = 66;
        var start = 0;
        var pageIndex = 0;

        var total = this.props.total;
        var pageSize = this.props.options.pageSize;

        this.state.links = [];
        this.size = Math.ceil(total / pageSize);
        this.linkSize = this.linkSize > this.size ? this.size : this.linkSize;

        if(this.size > this.linkSize + 2){
            var len = this.linkSize + 2;
        }else{
            var len = this.size;
        }

        for (var i = 1; i <= len; i++) {
            if (this.size > this.linkSize + 2 && i == this.linkSize + 1) {
                this.state.links.push('.');
            }else if(i == this.linkSize + 2){
                this.state.links.push(this.size);
            }else{
                this.state.links.push(i);
            }
        }
    },
    rePaint: function (idx) {
        var links = [];
        for (var n = 1, i = 1; n <= this.linkSize + 2; i++) {
            if(n != 1 && this.index >= this.linkSize && i <= this.index + 3 - this.linkSize && i <= this.size - this.linkSize){
                if(links[1] != '.'){
                    links[1] = '.';
                    n ++;
                }
                continue;
            }
            if (i > this.size) {
                break;
            }
            if(n == this.linkSize + 1 && this.index < this.size - 3){
                links.push('.');
            }else if(n == this.linkSize + 2){
                links.push(this.size);
            }else{
                links.push(i);
            }
            n ++;
        }
        this.setState({
            links: links
        });
    },
    componentWillMount: function () {
        this.init();
    },
    componentDidMount: function () {

    },
    render: function () {
        var that = this;

        return (
            React.createElement("div", {className: "text-right"}, 
                React.createElement("ul", {className: "pagination"}, 
                    
                        (function () {
                            return (
                                React.createElement("li", {className: "paginate_button", onClick: that.prev}, 
                                    React.createElement("a", {href: "#"}, '<')
                                )
                            );
                        })(), 
                    
                    
                        that.state.links.map(function (dt, i) {
                            if (dt == '.') {
                                return (
                                    React.createElement("li", {className: "paginate_button disabled"}, 
                                        React.createElement("a", {href: "#"}, '...')
                                    )
                                );
                            } else if(that.index + 1 === dt){
                                return (
                                    React.createElement("li", {className: "paginate_button active", param: dt, onClick: that.turnTo.bind(that,dt-1)}, 
                                        React.createElement("a", {href: "#"}, dt)
                                    )
                                );
                            } else {
                                return (
                                    React.createElement("li", {className: "paginate_button", param: dt, onClick: that.turnTo.bind(that,dt-1)}, 
                                        React.createElement("a", {href: "#"}, dt)
                                    )
                                );
                            }
                        }), 
                    
                    
                        (function () {
                            return (
                                React.createElement("li", {className: "paginate_button", onClick: that.next}, 
                                    React.createElement("a", {href: "#"}, '>')
                                )
                            );
                        })()
                    
                )
            )
        );
    }
});

