'use strict';

var Actable = React.createClass({
    getInitialState: function () {
        console.log(this.props.data);
        console.log(this.props.tHeadKey);

        this.data = [];
        this.pageSize = 10;
        this.total = this.props.data.length;

        if(this.props.options.lengthMenu && this.props.options.lengthMenu.length > 0){
            this.menu = this.props.options.lengthMenu;
            this.pageSize = this.menu[0];
        }

        this.updateDataSource(0, this.pageSize);

        if(this.props.pagination && this.props.pagination.linkSize){
            var linkSize = this.props.pagination.linkSize;
        }else{
            var linkSize = 5;
        }

        return {
            source: this.props.data,
            head: this.props.tHeadKey,
            pageSize: 10,
            linkSize: linkSize
        };
    },
    updateDataSource: function (index, pageSize) {
        console.log(index + ', ' + pageSize);
        this.data = [];
        if(pageSize) {
            this.pageSize = pageSize;
        }
        for (var i = index * this.pageSize; i < (index + 1) * this.pageSize; i++) {
            this.data.push(this.props.data[i]);
        }
        this.setState({source: this.data});
    },
    render: function () {
        var hData = this.state.head,
            hItem = [],
            bItem = [],
            n = 0,
            source = this.state.source;

        var that = this;

        this.props.onDataChange.setSt = function (dt) {
            that.updateDataSource(0, 1, {source: dt});
        };

        for (var k in hData) {
            bItem.push({
                ['bItemName' + n]: hData[k]
            });

            hItem.push(k);
            n++;
        }

        return (
            <div>
                {
                    (function () {
                        if (that.menu && that.menu.length > 0) {
                            return (
                                <div className="col-md-1">
                                    <LengthMenu options={that.menu} host={that}/>
                                </div>
                            );
                        }
                    })()
                }
                <table className={that.props.className}>
                    {
                        (function () {
                            return (that.props.tHeadName);
                        })()
                    }
                    {
                        (function () {
                            return (
                                <Actbody data={that.data} itemName={bItem}/>
                            );
                        })()
                    }
                </table>
                {
                    (function () {
                        // 是否添加分页控件
                        if (that.props.pagination && Pagination && typeof Pagination === 'function') {
                            if(that.pageSize){
                                that.props.pagination.pageSize = that.pageSize
                            }
                            return (
                                <Pagination total={that.total} options={that.props.pagination} host={that} />
                            );
                        }
                    })()
                }
            </div>
        );
    }
});

var LengthMenu = React.createClass({
    getInitialState: function () {
        return this.props;
    },
    change: function (e, d) {
        var evt = e || window.event,
            target = evt.target || evt.srcElement;
        this.props.host.updateDataSource(0, this.props.options[target.selectedIndex]);
    },
    render: function () {
        var that = this,
            data = this.props.options;

        function getVal(val) {
            that.value = val;
        }

        return (
            <select className="form-control" onChange={that.change}>
                {
                    data.map(function (dt, i) {
                        return (
                            <option value={dt} key={i}>{dt}</option>
                        );
                    })
                }
            </select>
        );
    }
});

var Acthead = React.createClass({
    getInitialState: function () {
        return {source: this.props};
    },
    render: function () {
        var data = this.state.source.data;

        return (
            <thead>
                <Actr dataSrc={data}/>
            </thead>
        );
    }
});

var Actbody = React.createClass({
    getInitialState: function () {
        return this.props;
    },
    render: function () {
        var dts = this.props.data,
            bItem = this.props.itemName;
        return (
            <tbody>
            {
                dts.map(function (dt, i) {
                    var data = [], idx = 0;
                    for (var k in dt) {
                        data.push(dt[bItem[idx]['bItemName' + idx++]]);
                    }
                    return (
                        <Actr data={data}/>
                    );
                })
            }
            </tbody>
        );
    }
});

var Actr = React.createClass({
    getInitialState: function () {
        return this.props;
    },
    render: function () {
        var dts = this.props.data;

        return (
            <tr>
                {
                    dts.map(function (dt, i) {
                        return (
                            <Actd data={dt}/>
                        );
                    })
                }
            </tr>
        );
    }
});

var Actd = React.createClass({
    getInitialState: function () {
        return this.props;
    },
    render: function () {
        return (
            <td key={this.props.data}>{this.props.data}</td>
        );
    }
});

var Pagination = React.createClass({
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
            <div className="text-right">
                <ul className="pagination">
                    {
                        (function () {
                            return (
                                <li className="paginate_button" onClick={that.prev}>
                                    <a href='#'>{'<'}</a>
                                </li>
                            );
                        })()
                    }
                    {
                        that.state.links.map(function (dt, i) {
                            if (dt == '.') {
                                return (
                                    <li className="paginate_button disabled">
                                        <a href='#'>{'...'}</a>
                                    </li>
                                );
                            } else if(that.index + 1 === dt){
                                return (
                                    <li className="paginate_button active" param={dt} onClick={that.turnTo.bind(that,dt-1)}>
                                        <a href='#'>{dt}</a>
                                    </li>
                                );
                            } else {
                                return (
                                    <li className="paginate_button" param={dt} onClick={that.turnTo.bind(that,dt-1)}>
                                        <a href='#'>{dt}</a>
                                    </li>
                                );
                            }
                        })
                    }
                    {
                        (function () {
                            return (
                                <li className="paginate_button" onClick={that.next}>
                                    <a href='#'>{'>'}</a>
                                </li>
                            );
                        })()
                    }
                </ul>
            </div>
        );
    }
});

