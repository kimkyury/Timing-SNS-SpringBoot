import { Tree } from 'react-tree-graph';
import { AnimatedTree } from 'react-tree-graph';
import 'react-tree-graph/dist/style.css';
function TreeGraph() {
    const data = {
        name: 'Parent',
        children: [
            {
                name: 'Child One',
            },
            {
                name: 'Child Two',
                children: [{ name: '피자나라' }, { name: '치킨공주' }],
            },
        ],
    };
    return (
        <div>
            {/* <AnimatedTree data={data} height={360} width={360} /> */}
            <Tree
                data={data}
                height={360}
                width={360}
                svgProps={{
                    transform: 'rotate(90)',
                }}
                textProps={{
                    transform: 'rotate(270)',
                }}
                animated={true}
            />
        </div>
    );
}

export default TreeGraph;
