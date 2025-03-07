/**
 * Represents a parking slot in the parking management system.
 * Each slot can either be designated for staff or open to the public.
 * The Slot class manages the parking of an auto and ensures compliance with
 * parking rules based on the type of auto and the slot designation.
 */
class Slot {
    private String slotCode;
    private boolean staffOnly;
    private Auto parkedAuto;

    /**
     * Constructs a Slot with the specified slot code and designation.
     *
     * @param slotCode the unique identifier for the slot
     * @param staffOnly true if the slot is designated for staff only, false otherwise
     */
    public Slot(String slotCode, boolean staffOnly) {
        this.slotCode = slotCode;
        this.staffOnly = staffOnly;
        this.parkedAuto = null;
    }
    
    /**
     * Returns the unique identifier for this parking slot.
     *
     * @return the slot ID
     */
    public String getId() {
        return slotCode;
    }

    /**
     * Returns the slot code of this parking slot.
     *
     * @return the slot code
     */
    public String getSlotCode() {
        return slotCode;
    }

    /**
     * Checks if this slot is designated for staff only.
     *
     * @return true if the slot is staff-only, false otherwise
     */
    public boolean isStaffOnly() {
        return staffOnly;
    }

    /**
     * Returns the auto currently parked in this slot.
     *
     * @return the parked auto, or null if no auto is parked
     */
    public Auto getParkedAuto() {
        return parkedAuto;
    }

    /**
     * Parks an auto in this slot if the slot is available and the auto complies
     * with the slot's designated type.
     *
     * @param auto the auto to be parked
     * @return true if the auto is parked successfully, false otherwise
     */
    public boolean parkAuto(Auto auto) {
        if (this.parkedAuto != null) {
            return false;
        }
        if (auto.isEmployee() != this.staffOnly) {
            return false;
        }

        this.parkedAuto = auto;
        return true;
    }

    /**
     * Removes the auto from this slot.
     *
     * @return true if the auto was removed successfully, false if the slot was empty
     */
    public boolean removeAuto() {
        if (this.parkedAuto == null) {
            return false;
        }
        this.parkedAuto = null;
        return true;
    }
}
