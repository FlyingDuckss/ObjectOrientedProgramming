# Swinburne Parking Management System

## Introduction
Welcome to the **Swinburne Parking Management System**! This GUI-based application, developed using Java and Swing, allows you to efficiently manage staff and visitor parking slots. With this system, you can:

- Add, remove, and view parking slots.
- Park and locate vehicles in designated slots.
- Filter and manage parking slots with ease.

## Features
- **Graphical User Interface (GUI)** using Java Swing.
- **Interactive Parking Slot Management** with color-coded status indicators.
- **Filtering System** to view only specific parking slots (vacant, occupied, staff, or visitor).
- **Event-Driven System** using ActionListeners for various functionalities.

## How to Use

### 1. Start the System
- Launch the application.
- Enter the number of staff and visitor parking slots when prompted.

### 2. Main Interface
Once the system starts, it will display a full-screen UI with two main panels:

#### Left Panel
- Options to **add/remove slots**, **park/remove vehicles**, and **view available slots**.

#### Right Panel
- Displays buttons representing parking slots for staff and visitors.
- Each button changes color based on slot status:
  - **Light Blue** - Vacant Staff Slot
  - **Dark Blue** - Occupied Staff Slot
  - **Light Yellow** - Vacant Visitor Slot
  - **Orange** - Occupied Visitor Slot

### 3. Parking and Removing Vehicles
- Click on a **vacant slot** to park a vehicle.
- Provide the **vehicle’s license plate** and **owner’s name**.
- To remove a vehicle, select the corresponding option and follow the prompts.

### 4. Filtering Slots
- Use the **filter dropdown** to view specific slot types: Vacant, Occupied, Staff, or Visitor.

## GUI Components
| Component    | Description |
|-------------|------------|
| `JFrame`    | Main application window. |
| `JLabel`    | Displays messages for user guidance. |
| `JTextField` | Captures numeric input for parking slots. |
| `JButton`   | Used for navigation and actions (e.g., next, slot selection). |
| `JComboBox` | Dropdown to filter parking slots by status. |
| `JPanel`    | Used for layout management (leftPanel, rightPanel, etc.). |
| `JScrollPane` | Enables vertical scrolling for parking slots display. |

## Event Handling Functions
| Function | Description |
|----------|------------|
| `ActionListener` for `nextButton` | Captures input and transitions to the main UI. |
| `ActionListener` for `filterDropdown` | Filters parking slots based on selection. |
| `ActionListener` for `optionButtons` | Handles actions like adding/removing slots, parking, and searching vehicles. |
| `ActionListener` for `slotButton` | Manages parking and viewing vehicle details. |
| `updateSlotButtonColor` | Dynamically updates the slot button color based on status. |
| `handleSlotClick` | Determines if a slot is vacant or occupied and performs appropriate actions. |

## Technologies Used
- **Java** (Core Logic)
- **Swing** (GUI Components)
- **Event Listeners** (User Interaction)

## Contributors
- **Osama Nadeem**

## License
This project is for educational purposes at Swinburne University.

---
This README provides an overview of the project, its functionality, and how users can interact with it.
