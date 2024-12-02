import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CrossConveyorImpl implements CrossConveyor {
    private final Map<String, List<Node>> conveyorMap;
    private final Map<Node, ReentrantLock> nodeLocks;

    public CrossConveyorImpl(Map<String, List<Node>> conveyorStructure) {
        this.conveyorMap = new HashMap<>(conveyorStructure);
        this.nodeLocks = new HashMap<>();

        for (List<Node> nodes : conveyorStructure.values()) {
            for (Node node : nodes) {
                nodeLocks.put(node, new ReentrantLock());
            }
        }
    }

    @Override
    public ConveyorAccessor conveyor(String id) {
        List<Node> conveyorNodes = conveyorMap.get(id);
        if (conveyorNodes == null) {
            throw new IllegalArgumentException("conveyor with id " + id + " does not exist");
        }
        return new ConveyorAccessorImpl(conveyorNodes);
    }

    private class ConveyorAccessorImpl implements ConveyorAccessor {
        private final List<Node> nodes;

        public ConveyorAccessorImpl(List<Node> nodes) {
            this.nodes = nodes;
        }

        @Override
        public int put(int value) {
            Node lastNode = null;

            for (Node node : nodes) {
                ReentrantLock lock = nodeLocks.get(node);
                lock.lock();
                try {
                    if (node.value == null) {
                        node.value = value;
                        lastNode = node;
                        break;
                    }
                } finally {
                    lock.unlock();
                }
            }

            if (lastNode == null) {
                Node firstNode = nodes.get(0);
                ReentrantLock lock = nodeLocks.get(firstNode);
                lock.lock();
                try {
                    firstNode.value = null;
                } finally {
                    lock.unlock();
                }

                for (int i = 1; i < nodes.size(); i++) {
                    nodes.get(i - 1).value = nodes.get(i).value;
                }
                Node newLastNode = nodes.get(nodes.size() - 1);
                ReentrantLock lastLock = nodeLocks.get(newLastNode);
                lastLock.lock();
                try {
                    newLastNode.value = value;
                } finally {
                    lastLock.unlock();
                }
                lastNode = newLastNode;
            }

            return lastNode.value;
        }
    }

    static class Node {
        Integer value;

        public Node() {
            this.value = null;
        }
    }
}
