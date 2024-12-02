public interface CrossConveyor {
    ConveyorAccessor conveyor(String id);

    interface ConveyorAccessor {
        int put(int value);
    }
}
