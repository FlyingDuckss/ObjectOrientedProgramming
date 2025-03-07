/**
 * Represents an automobile in the parking management system.
 * The Auto class stores information about the vehicle, including
 * its license plate, owner, and whether the owner is an employee.
 * It also tracks when the auto was parked and provides methods
 * to compute parking duration and costs.
 */

import java.time.LocalDateTime;

class Auto {
    private String licensePlate;
    private String ownerName;
    private boolean isEmployee;
    private long startTime;
    private LocalDateTime parkedAt;

    /**
     * Constructs an Auto object with the specified license plate, owner name,
     * and employee status.
     *
     * @param licensePlate the unique license plate of the automobile
     * @param ownerName the name of the owner of the automobile
     * @param isEmployee true if the owner is an employee, false otherwise
     */
    public Auto(String licensePlate, String ownerName, boolean isEmployee) {
        this.licensePlate = licensePlate;
        this.ownerName = ownerName;
        this.isEmployee = isEmployee;
        this.startTime = System.currentTimeMillis();
        this.parkedAt = LocalDateTime.now();
    }

    /**
     * Retrieves the license plate of the automobile.
     *
     * @return the license plate of the auto
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Retrieves the owner name of the automobile.
     *
     * @return the name of the owner
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Checks if the owner of the automobile is an employee.
     *
     * @return true if the owner is an employee, false otherwise
     */
    public boolean isEmployee() {
        return isEmployee;
    }

    /**
     * Computes the total hours the automobile has been parked.
     *
     * @return the number of hours the auto has been parked
     */
    public long computeHoursParked() {
        long currentTime = System.currentTimeMillis();
        long duration = currentTime - startTime;
        return duration / (1000 * 60 * 60);
    }

    /**
     * Calculates the total cost for parking based on the number of hours parked.
     *
     * @return the total cost of parking
     */
    public double calculateCost() {
        long hoursParked = computeHoursParked();
        return hoursParked * 2.0;
    }
    
    /**
     * Computes the duration the automobile has been parked since it was parked.
     *
     * @return a formatted string representing the duration parked in HH:MM:SS format
     */
    public String computeDurationParked() {
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(parkedAt, now).getSeconds();
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
