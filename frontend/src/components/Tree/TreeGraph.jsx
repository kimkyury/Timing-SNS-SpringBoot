import { Tree } from 'react-tree-graph';
import { AnimatedTree } from 'react-tree-graph';
import 'react-tree-graph/dist/style.css';
import { motion } from 'framer-motion';
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
        <motion.div
            initial={{ opacity: 0, x: 100 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -100 }}
            transition={{ duration: 0.4 }}
        >
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
        </motion.div>
    );
}

export default TreeGraph;
