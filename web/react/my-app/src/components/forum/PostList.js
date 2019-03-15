import React from 'react';

class PostList extends React.Component {
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
            {
                lists.map((v) => <div key={v.id}>{v.postName}</div>)
            }
            </div>
        );
    }
}

export default PostList;