import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class CrossConveyorTest {
    @Test
    void testSingleConveyor() {
        Map<String, List<CrossConveyorImpl.Node>> structure = new HashMap<>();
        List<CrossConveyorImpl.Node> nodes = Arrays.asList(
                new CrossConveyorImpl.Node(),
                new CrossConveyorImpl.Node(),
                new CrossConveyorImpl.Node()
        );
        structure.put("A", nodes);

        CrossConveyor conveyor = new CrossConveyorImpl(structure);
        CrossConveyor.ConveyorAccessor a = conveyor.conveyor("A");

        assertEquals(5, a.put(5));
        assertEquals(10, a.put(10));
        assertEquals(15, a.put(15));
        assertEquals(20, a.put(20));
    }

    @Test
    void testMultipleConveyors() {
        Map<String, List<CrossConveyorImpl.Node>> structure = new HashMap<>();
        List<CrossConveyorImpl.Node> nodesA = Arrays.asList(
                new CrossConveyorImpl.Node(),
                new CrossConveyorImpl.Node()
        );
        List<CrossConveyorImpl.Node> nodesB = Arrays.asList(
                new CrossConveyorImpl.Node(),
                new CrossConveyorImpl.Node()
        );
        structure.put("A", nodesA);
        structure.put("B", nodesB);

        CrossConveyor conveyor = new CrossConveyorImpl(structure);
        CrossConveyor.ConveyorAccessor a = conveyor.conveyor("A");
        CrossConveyor.ConveyorAccessor b = conveyor.conveyor("B");

        assertEquals(3, a.put(3));
        assertEquals(5, b.put(5));
        assertEquals(8, a.put(8));
        assertEquals(10, b.put(10));
    }
}
