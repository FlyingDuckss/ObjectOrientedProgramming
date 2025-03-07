/**
 * Represents a parking system that manages multiple parking slots.
 * The ParkingSystem class provides methods to add, remove, and retrieve slots,
 * as well as to find a slot based on an auto's license plate.
 */

import java.util.Map;
import java.util.HashMap;
class ParkingSystem {
    private Map<String, Slot> slots = new HashMap<>();

    /**
     * Adds a new parking slot to the system.
     *
     * @param slotCode the unique identifier for the new slot
     * @param staffOnly true if the slot is designated for staff only, false otherwise
     * @return true if the slot was added successfully, false if a slot with the same code already exists
     */
    public boolean addSlot(String slotCode, boolean staffOnly) {
        if (!slots.containsKey(slotCode)) {
            Slot slot = new Slot(slotCode, staffOnly);
            slots.put(slotCode, slot);
            return true;
        }
        return false;
    }

    /**
     * Removes a parking slot from the system.
     *
     * @param slotCode the unique identifier for the slot to be removed
     * @return true if the slot was removed successfully, false if no such slot exists
     */
    public boolean removeSlot(String slotCode) {
        return slots.remove(slotCode) != null;
    }

    /**
     * Retrieves a parking slot by its unique identifier.
     *
     * @param slotCode the unique identifier for the desired slot
     * @return the Slot object if found, null otherwise
     */
    public Slot getSlot(String slotCode) {
        return slots.get(slotCode);
    }

    /**
     * Finds a parking slot by the license plate of the auto parked in it.
     *
     * @param licensePlate the license plate of the auto to find
     * @return the Slot object containing the auto if found, null otherwise
     */
    public Slot findAutoByLicensePlate(String licensePlate) {
        for (Slot slot : slots.values()) { 
            if (slot.getParkedAuto() != null && slot.getParkedAuto().getLicensePlate().equals(licensePlate)) {
                return slot;
            }
        }
        return null; 
    }
}
